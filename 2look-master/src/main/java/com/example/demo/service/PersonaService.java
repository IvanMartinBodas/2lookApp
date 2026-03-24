package com.example.demo.service;

import com.example.demo.model.Persona;
import com.example.demo.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;

    public List<Persona> getAll() {
        return personaRepository.findAll();
    }

    public Persona getById(Integer id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id));
    }

    public Persona getByEmail(String email) {
        return personaRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con email: " + email));
    }

    public Persona create(Persona persona) {
        if (personaRepository.existsByEmail(persona.getEmail())) {
            throw new RuntimeException("Ya existe una persona con ese email");
        }
        return personaRepository.save(persona);
    }

    public Persona update(Integer id, Persona persona) {
        Persona existing = getById(id);
        existing.setNombre(persona.getNombre());
        existing.setEmail(persona.getEmail());
        existing.setRol(persona.getRol());
        return personaRepository.save(existing);
    }

    public void delete(Integer id) {
        getById(id);
        personaRepository.deleteById(id);
    }
}