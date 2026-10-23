package com.smartfinance.resources;

import com.smartfinance.dto.MovementRequestDTO;
import com.smartfinance.dto.MovementResponseDTO;
import com.smartfinance.entities.Movement;
import com.smartfinance.services.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/movements")
public class MovementResource {

    @Autowired
    private MovementService service;

    @GetMapping
    public ResponseEntity<List<MovementResponseDTO>> findAll() {

        List<MovementResponseDTO> list = service.findAll().stream()
                .map(MovementResponseDTO::new)
                .toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MovementResponseDTO> findById(@PathVariable Long id) {
        Movement obj = service.findById(id);
        return ResponseEntity.ok().body(new MovementResponseDTO(obj));
    }

    @PostMapping
    public ResponseEntity<MovementResponseDTO> insert(@RequestBody MovementRequestDTO dto) {
        Movement obj = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(new MovementResponseDTO(obj));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MovementResponseDTO> update(@PathVariable Long id, @RequestBody MovementRequestDTO dto) {
        Movement obj = service.update(id, dto);
        return ResponseEntity.ok().body(new MovementResponseDTO(obj));
    }
}
