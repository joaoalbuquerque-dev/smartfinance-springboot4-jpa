package com.smartfinance.resources;

import com.smartfinance.dto.AccountRequestDTO;
import com.smartfinance.dto.AccountResponseDTO;
import com.smartfinance.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/accounts")
public class AccountResource {

    @Autowired
    private AccountService service;

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> findAll() {

        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> insert(@RequestBody AccountRequestDTO dto) {
        AccountResponseDTO response = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
         service.delete(id);
         return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable Long id, @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }
}
