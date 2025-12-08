package com.mercatosys.controller;

import com.mercatosys.annotation.RequireAuth;
import com.mercatosys.annotation.RequireRole;
import com.mercatosys.dto.user.UserRequestDTO;
import com.mercatosys.dto.user.UserResponseDTO;
import com.mercatosys.dto.user.UserUpdateDTO;
import com.mercatosys.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping

    public ResponseEntity<UserResponseDTO> createUser(@Validated @RequestBody UserRequestDTO dto) {
        return new ResponseEntity<>(userService.create(dto), HttpStatus.CREATED);
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Validated @RequestBody UserRequestDTO dto
    ) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
