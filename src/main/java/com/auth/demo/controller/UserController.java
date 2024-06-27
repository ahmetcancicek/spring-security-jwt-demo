package com.auth.demo.controller;

import com.auth.demo.annotation.AuthenticatedUser;
import com.auth.demo.dto.ProfileRequest;
import com.auth.demo.dto.ProfileResponse;
import com.auth.demo.response.Response;
import com.auth.demo.security.AuthUser;
import com.auth.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User API Documentation")
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Returns the authenticated user profile", description = "This method returns the authenticated user profile")
    public Response<ProfileResponse> getUserProfile(@AuthenticatedUser AuthUser authenticatedUser) {
        return respond(userService.getProfile(authenticatedUser));
    }

    @PostMapping("/me")
    @Operation(summary = "Update the authenticated user profile", description = "This method updates the authenticated user profile and returns new values")
    public Response<ProfileResponse> updateProfile(@AuthenticatedUser AuthUser authenticatedUser,
                                                   @Valid @RequestBody ProfileRequest profileRequest) {
        System.out.println("Hello World");
        return respond(userService.updateProfile(authenticatedUser, profileRequest));
    }
}
