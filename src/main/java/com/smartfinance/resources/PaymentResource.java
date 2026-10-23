package com.smartfinance.resources;

import com.smartfinance.dto.PaymentRequestDTO;
import com.smartfinance.dto.PaymentResponseDTO;
import com.smartfinance.entities.Payment;
import com.smartfinance.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentResource {

    @Autowired
    private PaymentService service;

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> findAll() {
        List<PaymentResponseDTO> payments = service.findAll().stream()
                .map(PaymentResponseDTO::new)
                .toList();
        return ResponseEntity.ok(payments);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new PaymentResponseDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> insert(@RequestBody PaymentRequestDTO dto) {
        Payment obj = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(new PaymentResponseDTO(obj));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
