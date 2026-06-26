package com.auth.auth.services;

import com.auth.auth.dto.LoginRequest;
import com.auth.auth.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}

