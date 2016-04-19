package it.infn.mw.iam.oidc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mitre.openid.connect.client.OIDCAuthenticationFilter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

public class IamOidcAuthenticationFilter extends OIDCAuthenticationFilter {

  protected final static String ORIGIN_AUTH_REQUEST_SESSION_VARIABLE = "origin_auth_request";

  @Override
  protected void handleAuthorizationRequest(HttpServletRequest request,
    HttpServletResponse response) throws IOException {

    HttpSession session = request.getSession();

    // backup origin redirect uri and state
    DefaultSavedRequest savedRequest = (DefaultSavedRequest) session
      .getAttribute("SPRING_SECURITY_SAVED_REQUEST");
    session.setAttribute(ORIGIN_AUTH_REQUEST_SESSION_VARIABLE, savedRequest);

    super.handleAuthorizationRequest(request, response);
  }

}
