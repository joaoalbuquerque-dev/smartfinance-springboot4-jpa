package com.smartfinance.resources;

import com.smartfinance.entities.Movement;
import com.smartfinance.services.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
