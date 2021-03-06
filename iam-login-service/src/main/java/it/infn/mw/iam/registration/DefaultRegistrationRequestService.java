package it.infn.mw.iam.registration;

import static it.infn.mw.iam.core.IamRegistrationRequestStatus.APPROVED;
import static it.infn.mw.iam.core.IamRegistrationRequestStatus.CONFIRMED;
import static it.infn.mw.iam.core.IamRegistrationRequestStatus.NEW;
import static it.infn.mw.iam.core.IamRegistrationRequestStatus.REJECTED;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import it.infn.mw.iam.api.scim.exception.IllegalArgumentException;
import it.infn.mw.iam.api.scim.exception.ScimResourceNotFoundException;
import it.infn.mw.iam.api.scim.model.ScimUser;
import it.infn.mw.iam.api.scim.provisioning.ScimUserProvisioning;
import it.infn.mw.iam.core.IamRegistrationRequestStatus;
import it.infn.mw.iam.persistence.model.IamAccount;
import it.infn.mw.iam.persistence.model.IamRegistrationRequest;
import it.infn.mw.iam.persistence.repository.IamRegistrationRequestRepository;

@Service
public class DefaultRegistrationRequestService implements RegistrationRequestService {

  @Autowired
  private IamRegistrationRequestRepository requestRepository;

  @Autowired
  private ScimUserProvisioning userService;

  @Autowired
  private RegistrationConverter converter;

  @Autowired
  private TokenGenerator tokenGenerator;

  private static Table<IamRegistrationRequestStatus, IamRegistrationRequestStatus, Boolean> transictions;

  {
    transictions = HashBasedTable.create();
    transictions.put(NEW, CONFIRMED, true);
    transictions.put(CONFIRMED, APPROVED, true);
    transictions.put(CONFIRMED, REJECTED, true);
  }

  @Override
  public RegistrationRequestDto create(final ScimUser user) {

    IamAccount newAccount = userService.createAccount(user);
    newAccount.setConfirmationKey(tokenGenerator.generateToken());
    newAccount.setActive(false);

    IamRegistrationRequest request = new IamRegistrationRequest();
    request.setUuid(UUID.randomUUID().toString());
    request.setCreationTime(new Date());
    request.setAccount(newAccount);
    request.setStatus(NEW);

    requestRepository.save(request);

    return converter.fromEntity(request);
  }

  @Override
  public List<RegistrationRequestDto> list(final IamRegistrationRequestStatus status) {

    List<IamRegistrationRequest> result = requestRepository.findByStatus(status).get();

    List<RegistrationRequestDto> requests = new ArrayList<>();
    
    for (IamRegistrationRequest elem : result) {
      RegistrationRequestDto item = converter.fromEntity(elem);
      requests.add(item);
    }

    return requests;
  }

  @Override
  public RegistrationRequestDto updateStatus(final String uuid,
      final IamRegistrationRequestStatus status) {

    IamRegistrationRequest reg =
        requestRepository.findByUuid(uuid).orElseThrow(() -> new ScimResourceNotFoundException(
            String.format("No request mapped to uuid [%s]", uuid)));

    if (!checkStateTransiction(reg.getStatus(), status)) {
      throw new IllegalArgumentException(
          String.format("Bad status transition from [%s] to [%s]", reg.getStatus(), status));
    }

    reg.setStatus(status);
    reg.setLastUpdateTime(new Date());
    requestRepository.save(reg);

    if (APPROVED.equals(status)) {
      IamAccount account = reg.getAccount();
      account.setActive(true);
      account.setLastUpdateTime(new Date());
    } else if (CONFIRMED.equals(status)) {
      reg.getAccount().getUserInfo().setEmailVerified(true);
    }

    requestRepository.save(reg);

    return converter.fromEntity(reg);
  }

  @Override
  public RegistrationRequestDto confirmRequest(final String confirmationKey) {

    IamRegistrationRequest reg = requestRepository.findByAccountConfirmationKey(confirmationKey)
        .orElseThrow(() -> new ScimResourceNotFoundException(String
            .format("No registration request found for registration_key [%s]", confirmationKey)));

    reg.setStatus(CONFIRMED);
    reg.setLastUpdateTime(new Date());
    reg.getAccount().getUserInfo().setEmailVerified(true);

    requestRepository.save(reg);

    return converter.fromEntity(reg);
  }

  private boolean checkStateTransiction(final IamRegistrationRequestStatus currentStatus,
      final IamRegistrationRequestStatus newStatus) {

    return transictions.contains(currentStatus, newStatus);
  }

}
