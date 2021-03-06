package it.infn.mw.iam.authn.saml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import it.infn.mw.iam.authn.saml.util.SamlUserIdentifierResolver;
import it.infn.mw.iam.persistence.model.IamAccount;
import it.infn.mw.iam.persistence.model.IamAuthority;
import it.infn.mw.iam.persistence.repository.IamAccountRepository;

public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

  final SamlUserIdentifierResolver resolver;
  final IamAccountRepository repo;

  @Autowired
  public SAMLUserDetailsServiceImpl(SamlUserIdentifierResolver resolver,
      IamAccountRepository repo) {
    this.resolver = resolver;
    this.repo = repo;
  }


  List<GrantedAuthority> convertAuthorities(IamAccount a) {

    List<GrantedAuthority> authorities = new ArrayList<>();
    for (IamAuthority auth : a.getAuthorities()) {
      authorities.add(new SimpleGrantedAuthority(auth.getAuthority()));
    }
    return authorities;
  }

  @Override
  public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

    String issuerId = credential.getRemoteEntityID();

    String userSamlId =
        resolver.getUserIdentifier(credential).orElseThrow(() -> new UsernameNotFoundException(
            "Could not extract a user identifier from the SAML assertion"));

    IamAccount account = repo.findBySamlId(issuerId, userSamlId).orElseThrow(() -> {
      String errorMessage =
          String.format("No local user found linked to SAML identity (%s) %s ", issuerId, userSamlId);
      
      return new UsernameNotFoundException(errorMessage);
    });

    return new User(account.getUsername(), account.getPassword(), convertAuthorities(account));

  }

}
