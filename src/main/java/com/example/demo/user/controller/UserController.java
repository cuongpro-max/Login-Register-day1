package com.example.demo.user.controller;

import com.example.demo.user.dto.response.UserProfileResponse;
import com.example.demo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
@Tag(name = "2. User Resource", description = "Các API thao tác với thông tin người dùng")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Lấy thông tin tài khoản hiện tại",
            description = "Truyền Access Token qua Header Authorization: Bearer <token>",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        return ResponseEntity.ok(userService.getCurrentUserProfile(authHeader));
    }
}
