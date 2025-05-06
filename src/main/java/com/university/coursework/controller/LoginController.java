package com.university.coursework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class LoginController {

    @GetMapping("/login")
    @Operation(summary = "Show login form", description = "Returns the login page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login page returned successfully")
    })
    public String showLoginForm() {
        return "login.html";
    }
}