package com.ecommerce.user.service.login;

import com.ecommerce.user.controller.login.dto.LoginRequest;
import com.ecommerce.user.controller.login.dto.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request);
}
