package com.smartfinance.controllers;

import com.smartfinance.dto.AccountRequestDTO;
import com.smartfinance.dto.AccountResponseDTO;
import com.smartfinance.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/accounts")
public class AccountResource {

    @Autowired
    private AccountService service;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> findAll() {

        return ResponseEntity.ok().body(service.findAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<AccountResponseDTO> insert(@Valid @RequestBody AccountRequestDTO dto) {
        AccountResponseDTO response = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
         service.delete(id);
         return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }
}
