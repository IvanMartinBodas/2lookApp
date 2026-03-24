package com.example.demo.controller.rest;

import com.example.demo.model.Favorito;
import com.example.demo.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Favorito>> getByClienteId(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(favoritoService.getByClienteId(clienteId));
    }

    @PostMapping
    public ResponseEntity<Favorito> create(@RequestBody Favorito favorito) {
        return ResponseEntity.ok(favoritoService.create(favorito));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        favoritoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}