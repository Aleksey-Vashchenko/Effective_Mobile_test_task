package com.vashchenko.task.controllers;

import com.vashchenko.task.dto.requests.ActionEmailRequest;
import com.vashchenko.task.dto.requests.ActionPhoneRequest;
import com.vashchenko.task.dto.requests.SendMoneyRequest;
import com.vashchenko.task.dto.responses.MapResponse;
import com.vashchenko.task.dto.responses.Response;
import com.vashchenko.task.dto.views.ClientBaseProjection;
import com.vashchenko.task.repositories.criteria.ClientCriteria;
import com.vashchenko.task.services.ClientService;
import com.vashchenko.task.services.JwtService;
import com.vashchenko.task.services.MoneyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@Service
@RequiredArgsConstructor
public class ClientController {

    private final JwtService jwtService;
    private final ClientService clientService;
    private final MoneyService moneyService;

    @PostMapping("/phones/delete")
    public ResponseEntity<Response> deletePhone(@RequestHeader("Authorization") String accessToken,
                                                ActionPhoneRequest request){

        clientService.deletePhone(jwtService.extractId(
                        accessToken.substring(accessToken.indexOf(' '))),
                request);
        return new ResponseEntity<>(new Response(HttpStatus.OK),HttpStatus.OK);
    }

    @PostMapping("/emails/delete")
    public ResponseEntity<Response> deleteEmail(@RequestHeader("Authorization") String accessToken,
                            @Valid @RequestBody ActionEmailRequest request){

        clientService.deleteEmail(jwtService.extractId(
                        accessToken.substring(accessToken.indexOf(' '))),
                request);
        return new ResponseEntity<>(new Response(HttpStatus.OK),HttpStatus.OK);
    }

    @PostMapping("/phones")
    public ResponseEntity<Response> addPhone(@RequestHeader("Authorization") String accessToken,
                               @Valid @RequestBody ActionPhoneRequest request){

        clientService.addPhone(jwtService.extractId(
                        accessToken.substring(accessToken.indexOf(' '))),
                request);
        return new ResponseEntity<>(new Response(HttpStatus.CREATED),HttpStatus.CREATED);
    }

    @PostMapping("/emails")
    public ResponseEntity<Response> addEmail(@RequestHeader("Authorization") String accessToken,
                            @Valid @RequestBody ActionEmailRequest request){

        clientService.addEmail(jwtService.extractId(
                        accessToken.substring(accessToken.indexOf(' '))),
                request);
        return new ResponseEntity<>(new Response(HttpStatus.CREATED),HttpStatus.CREATED);
    }
    @PostMapping

    public HttpEntity<Response> sendMoney(@RequestHeader("Authorization") String accessToken,
                                @Valid @RequestBody SendMoneyRequest request){
        moneyService.sendMoney(jwtService.extractId(
                        accessToken.substring(accessToken.indexOf(' '))),
                request);
        return new ResponseEntity<>(new Response(HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Response> searchClients(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Date birthDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc")
            @Pattern(regexp = "asc|desc", message = "Field must be either 'asc' or 'desc'")
            String sortOrder,
            @RequestParam(defaultValue = "fullName")
            @Pattern(regexp = "birthDate|fullName", message = "Field must be either 'birthDate' or 'fullName'")
            String sortField) {

        List<Specification<ClientBaseProjection>> specs = new ArrayList<>();

        if (fullName != null && !fullName.isEmpty()) {
            specs.add(ClientCriteria.fullNameLike(fullName));
        }

        if (phone != null && !phone.isEmpty()) {
            specs.add(ClientCriteria.phoneEquals(phone));
        }

        if (email != null && !email.isEmpty()) {
            specs.add(ClientCriteria.emailEquals(email));
        }

        if (birthDate != null) {
            specs.add(ClientCriteria.birthDateAfter(birthDate));
        }

        Specification<ClientBaseProjection> finalSpec = specs.stream()
                .reduce(Specification::and)
                .orElse(null);
        Sort.Direction sortDirection = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));
        MapResponse response = new MapResponse();
        response.addData("page",clientService.findAllForPage(finalSpec, pageable));
        return new ResponseEntity<>(response,HttpStatus.OK) ;
    }

}
