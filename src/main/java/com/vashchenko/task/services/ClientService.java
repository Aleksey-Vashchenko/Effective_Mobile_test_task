package com.vashchenko.task.services;

import com.vashchenko.task.dto.requests.CreateClientRequest;
import com.vashchenko.task.dto.requests.ActionEmailRequest;
import com.vashchenko.task.dto.requests.ActionPhoneRequest;
import com.vashchenko.task.dto.views.ClientBaseProjection;
import com.vashchenko.task.entities.BankAccount;
import com.vashchenko.task.entities.ClientUser;
import com.vashchenko.task.exceptions.*;
import com.vashchenko.task.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public void createClient(CreateClientRequest request){
        if(clientRepository.isLoginExists(request.login())){
            throw new UserIsAlreadyExistsException();
        }
        if(clientRepository.isEmailExists(request.email())){
            throw new EmailIsAlreadyInUsedException();
        }
        if(clientRepository.isPhoneNumberExists(request.phoneNumber())){
            throw new PhoneIsAlreadyUsedException();
        }
        ClientUser clientUser = ClientUser.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumbers(Collections.singletonList(request.phoneNumber()))
                .emails(Collections.singletonList(request.email()))
                .fullName(request.name())
                .birthDate(request.birthDate())
                .bankAccount(new BankAccount(request.startSum()))
                .build();
        clientRepository.save(clientUser);
    }

    public void deletePhone(String uuid, ActionPhoneRequest request) {
        ClientUser user = findClientByUuid(uuid);
        List<String> phoneNumbers = user.getPhoneNumbers();
        if(!phoneNumbers.contains(request.phoneNumber())){
            throw new PhoneNumberIsNotFoundException(request.phoneNumber(), uuid);
        }
        if(phoneNumbers.size()>1){
            phoneNumbers.removeIf(phoneNumber -> phoneNumber.equals(request.phoneNumber()));
            clientRepository.save(user);
        }
        else {
            throw new IllegalDeleteException("Cannot delete the last phone number");
        }
    }

    public void deleteEmail(String uuid, ActionEmailRequest request) {
        ClientUser user = findClientByUuid(uuid);
        List<String> emails = user.getEmails();
        if(!emails.contains(request.email())){
            throw new EmailIsNotFoundException(request.email(),uuid);
        }
        if(emails.size()>1){
            emails.removeIf(phoneNumber -> phoneNumber.equals(request.email()));
            clientRepository.save(user);
        }
        else {
            throw new IllegalDeleteException("Cannot delete the last email");
        }
    }
    private ClientUser findClientByUuid(String uuid){
        try {
            ClientUser user = clientRepository.findById(UUID.fromString(uuid)).get();
            return user;
        }
        catch (NoSuchElementException e){
            throw new UserIsNotFoundException(uuid);
        }
    }

    public void addPhone(String uuid, ActionPhoneRequest request) {
        ClientUser clientUser = findClientByUuid(uuid);
        if(clientRepository.isPhoneNumberExists(request.phoneNumber())){
            throw new PhoneIsAlreadyUsedException();
        }
        else {
            clientUser.addPhoneNumber(request.phoneNumber());
            clientRepository.save(clientUser);
        }
    }

    public void addEmail(String uuid, ActionEmailRequest request) {
        ClientUser clientUser = findClientByUuid(uuid);
        if(clientRepository.isPhoneNumberExists(request.email())){
            throw new EmailIsAlreadyInUsedException();
        }
        else {
            clientUser.addEmail(request.email());
            clientRepository.save(clientUser);
        }
    }

    @Query
    public Page<ClientBaseProjection> findAllForPage(Specification<ClientBaseProjection> finalSpec, Pageable pageable) {
        return clientRepository.findAll(finalSpec,pageable);
    }
}
