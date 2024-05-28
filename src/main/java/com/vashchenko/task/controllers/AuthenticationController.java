package com.vashchenko.task.controllers;

import com.vashchenko.task.dto.requests.AuthorizationRequest;
import com.vashchenko.task.dto.requests.RefreshRequest;
import com.vashchenko.task.dto.responses.MapResponse;
import com.vashchenko.task.dto.responses.Response;
import com.vashchenko.task.services.AuthenticationService;
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
@RequestMapping("/api/v1/authorization")
@Service
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody AuthorizationRequest request){
        MapResponse response = new MapResponse();
        response.addData("tokens",authenticationService.authorize(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Response> refreshTokens(@RequestBody RefreshRequest request){
        MapResponse response = new MapResponse();
        response.addData("tokens",authenticationService.refresh(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
