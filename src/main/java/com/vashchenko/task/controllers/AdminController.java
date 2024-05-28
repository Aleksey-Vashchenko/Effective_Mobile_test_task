package com.vashchenko.task.controllers;

import com.vashchenko.task.dto.requests.CreateClientRequest;
import com.vashchenko.task.dto.responses.Response;
import com.vashchenko.task.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@Service
@RequiredArgsConstructor
public class AdminController {

    private final ClientService clientService;

    @PostMapping("/clients")
    public ResponseEntity<Response> registration(@Valid @RequestBody CreateClientRequest request){
        clientService.createClient(request);
        return new ResponseEntity<>(new Response(HttpStatus.CREATED),HttpStatus.CREATED);
    }
}
