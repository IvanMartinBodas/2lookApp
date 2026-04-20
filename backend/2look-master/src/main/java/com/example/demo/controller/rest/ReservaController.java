package com.example.demo.controller.rest;

import com.example.demo.model.Reserva;
import com.example.demo.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> getAll() {
        return ResponseEntity.ok(reservaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(reservaService.getById(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Reserva>> getByClienteId(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(reservaService.getByClienteId(clienteId));
    }

    @GetMapping("/barbero/{barberoId}")
    public ResponseEntity<List<Reserva>> getByBarberoId(@PathVariable Integer barberoId) {
        return ResponseEntity.ok(reservaService.getByBarberoId(barberoId));
    }

    @PostMapping
    public ResponseEntity<Reserva> create(@RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.create(reserva));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<java.util.Map<String, Object>> updateEstado(@PathVariable Integer id, @RequestParam String estado) {
        Reserva updated = reservaService.updateEstado(id, estado);
        return ResponseEntity.ok(java.util.Map.of("id", updated.getId(), "estado", updated.getEstado()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}