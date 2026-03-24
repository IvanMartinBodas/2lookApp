package com.example.demo.controller.rest;

import com.example.demo.model.Opinion;
import com.example.demo.service.OpinionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/opiniones")
@RequiredArgsConstructor
public class OpinionController {

    private final OpinionService opinionService;

    @GetMapping
    public ResponseEntity<List<Opinion>> getAll() {
        return ResponseEntity.ok(opinionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opinion> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(opinionService.getById(id));
    }

    @GetMapping("/barbero/{barberoId}")
    public ResponseEntity<List<Opinion>> getByBarberoId(@PathVariable Integer barberoId) {
        return ResponseEntity.ok(opinionService.getByBarberoId(barberoId));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Opinion>> getByClienteId(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(opinionService.getByClienteId(clienteId));
    }

    @PostMapping
    public ResponseEntity<Opinion> create(@RequestBody Opinion opinion) {
        return ResponseEntity.ok(opinionService.create(opinion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        opinionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}