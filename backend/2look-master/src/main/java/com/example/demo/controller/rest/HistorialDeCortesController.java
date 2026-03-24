package com.example.demo.controller.rest;

import com.example.demo.model.HistorialDeCortes;
import com.example.demo.service.HistorialDeCortesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
public class HistorialDeCortesController {

    private final HistorialDeCortesService historialService;

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<HistorialDeCortes>> getByClienteId(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(historialService.getByClienteId(clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialDeCortes> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(historialService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HistorialDeCortes> create(@RequestBody HistorialDeCortes historial) {
        return ResponseEntity.ok(historialService.create(historial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        historialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}