package com.example.demo.controller.rest;

import com.example.demo.model.Barbero;
import com.example.demo.service.BarberoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/barberos")
@RequiredArgsConstructor
public class BarberoController {

    private final BarberoService barberoService;

    @GetMapping
    public ResponseEntity<List<Barbero>> getAll() {
        return ResponseEntity.ok(barberoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barbero> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(barberoService.getById(id));
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Barbero>> getByEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(barberoService.getByEspecialidad(especialidad));
    }

    @PostMapping
    public ResponseEntity<Barbero> create(@RequestBody Barbero barbero) {
        return ResponseEntity.ok(barberoService.create(barbero));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barbero> update(@PathVariable Integer id, @RequestBody Barbero barbero) {
        return ResponseEntity.ok(barberoService.update(id, barbero));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        barberoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
