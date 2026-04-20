package com.example.demo.controller.rest;

import com.example.demo.model.Persona;
import com.example.demo.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> getAll() {
        return ResponseEntity.ok(personaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(personaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Persona> create(@RequestBody Persona persona) {
        return ResponseEntity.ok(personaService.create(persona));
    }

    @PutMapping("/{id}")
    public ResponseEntity<java.util.Map<String, Object>> update(@PathVariable Integer id, @RequestBody Persona persona) {
        Persona updated = personaService.update(id, persona);
        return ResponseEntity.ok(java.util.Map.of(
            "id", updated.getId(),
            "nombre", updated.getNombre(),
            "email", updated.getEmail(),
            "rol", updated.getRol()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        personaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}