package com.vashchenko.task.services;

import com.vashchenko.task.dto.helpers.JwtTokenPair;
import com.vashchenko.task.dto.requests.AuthorizationRequest;
import com.vashchenko.task.dto.requests.RefreshRequest;
import com.vashchenko.task.entities.User;
import com.vashchenko.task.exceptions.InvalidTokenPairException;
import com.vashchenko.task.exceptions.UserIsNotFoundException;
import com.vashchenko.task.exceptions.WrongLoginOrPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenPair authorize(AuthorizationRequest request){
        try {
            User user = userService.findUserByLogin(request.login());
            if(passwordEncoder.matches(request.password(), user.getPassword())){
                JwtTokenPair tokenPair = jwtService.generateTokens(user.getId());
                user.setRefreshToken(tokenPair.refreshToken());
                userService.save(user);
                return tokenPair;
            }
            else {
                throw new WrongLoginOrPasswordException();
            }
        }
        catch (UserIsNotFoundException e){
            throw new WrongLoginOrPasswordException();
        }
    }

    public JwtTokenPair refresh(RefreshRequest refreshRequest){
        String userId = jwtService.extractId(refreshRequest.accessToken());
        User user = userService.findUserByUuid(userId);
        if(user.getRefreshToken().equals(refreshRequest.refreshToken())&&jwtService.validatePair(refreshRequest)){
            JwtTokenPair tokenPair = jwtService.generateTokens(user.getId());
            user.setRefreshToken(tokenPair.refreshToken());
            userService.save(user);
            return tokenPair;
        }
        else {
            throw new InvalidTokenPairException();
        }
    }
}
