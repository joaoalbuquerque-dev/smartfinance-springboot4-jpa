package com.smartfinance.controllers;

import com.smartfinance.dto.UserRequestDTO;
import com.smartfinance.dto.UserResponseDTO;
import com.smartfinance.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService service;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {

        return ResponseEntity.ok().body(service.findAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@Validated({Default.class, UserRequestDTO.OnCreate.class}) @RequestBody UserRequestDTO dto) {
        UserResponseDTO response = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }
}
