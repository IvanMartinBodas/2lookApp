package com.example.demo.controller.rest;

import com.example.demo.model.Servicio;
import com.example.demo.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<Servicio>> getAll() {
        return ResponseEntity.ok(servicioService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(servicioService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Servicio> create(@RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioService.create(servicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> update(@PathVariable Integer id, @RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioService.update(id, servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        servicioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}