package com.example.demo.controller.rest;

import com.example.demo.model.Persona;
import com.example.demo.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PersonaService personaService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        try {
            Persona persona = personaService.getByEmail(email);

            if (!persona.getPassword().equals(password)) {
                return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
            }

            return ResponseEntity.ok(Map.of(
                    "id", persona.getId(),
                    "nombre", persona.getNombre(),
                    "email", persona.getEmail(),
                    "rol", persona.getRol()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String nombre = body.get("nombre");
        String email = body.get("email");
        String password = body.get("password");

        if (nombre == null || email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Faltan campos obligatorios"));
        }

        try {
            Persona nueva = new Persona();
            nueva.setNombre(nombre);
            nueva.setEmail(email);
            nueva.setPassword(password);
            nueva.setRol("CLIENTE");

            Persona creada = personaService.create(nueva);

            return ResponseEntity.ok(Map.of(
                    "id", creada.getId(),
                    "nombre", creada.getNombre(),
                    "email", creada.getEmail(),
                    "rol", creada.getRol()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }
}