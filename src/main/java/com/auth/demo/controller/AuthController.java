package com.auth.demo.controller;

import com.auth.demo.dto.*;
import com.auth.demo.response.Response;
import com.auth.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API Documentation")
public class AuthController extends BaseController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Create a new user", description = "This method creates a new user and publishes the event for email verification")
    public Response<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return respond(authService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    @Operation(summary = "Login the existing user", description = "This method can be used to login operation for user and it returns the authentication tokens")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return respond(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Generate a new token", description = "This method creates a new token for the expired token authentication")
    public Response<LoginResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return respond(authService.refreshToken(tokenRefreshRequest));
    }

    @GetMapping("/confirm")
    @Operation(summary = "Confirms the email verification token", description = "This method confirm the email verification token belongs to the user")
    public Response<ConfirmResponse> confirmRegistration(@RequestParam("token") String token) {
        return respond(authService.confirmEmailRegistration(token));
    }

    @GetMapping("/checkEmailInUse")
    @Operation(summary = "Checks if the email exists", description = "This method checks whether the email already exists or not")
    public Response<EmailCheckResponse> checkEmailInUse(@RequestParam("email") String email) {
        return respond(new EmailCheckResponse(authService.isExistsByEmail(email), email));
    }

    @GetMapping("/checkUsernameInUse")
    @Operation(summary = "Checks if the username exists", description = "This method checks whether the username already exists or nor")
    public Response<UsernameCheckResponse> checkUsernameInUse(@RequestParam("username") String username) {
        return respond(new UsernameCheckResponse(authService.isExistsByUsername(username), username));
    }

    @GetMapping("/resendRegistrationToken")
    @Operation(summary = "Resend the email registration token", description = "This method resend the email registration token with updating expiry date if expired")
    public void resendRegistrationToken(@RequestParam("token") String token) {
        authService.resendRegistrationToken(token);
    }

    @Operation(summary = "", description = "")
    @PostMapping("/password/reset")
    public void resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        authService.resetPassword(passwordResetRequest);
    }

    @Operation(summary = "", description = "")
    @PostMapping("/password/resetlink")
    public void resetLink(@Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {
        authService.resetLink(passwordResetLinkRequest);
    }
}
