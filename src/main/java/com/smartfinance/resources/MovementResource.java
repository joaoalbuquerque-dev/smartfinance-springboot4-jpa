package com.smartfinance.resources;

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
    public ResponseEntity<List<Movement>> findAll() {

        List<Movement> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Movement> findById(@PathVariable Long id) {
        Movement obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Movement> insert(@RequestBody Movement obj) {
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Movement> update(@PathVariable Long id, @RequestBody Movement obj) {
        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }
}
