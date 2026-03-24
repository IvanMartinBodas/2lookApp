package com.example.demo.service;

import com.example.demo.model.Barbero;
import com.example.demo.repository.BarberoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberoService {

    private final BarberoRepository barberoRepository;

    public List<Barbero> getAll() {
        return barberoRepository.findAll();
    }

    public Barbero getById(Integer id) {
        return barberoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado con id: " + id));
    }

    public List<Barbero> getByEspecialidad(String especialidad) {
        return barberoRepository.findByEspecialidad(especialidad);
    }

    public Barbero create(Barbero barbero) {
        return barberoRepository.save(barbero);
    }

    public Barbero update(Integer id, Barbero barbero) {
        Barbero existing = getById(id);
        existing.setEspecialidad(barbero.getEspecialidad());
        existing.setFotoUrl(barbero.getFotoUrl());
        existing.setValoracion(barbero.getValoracion());
        existing.setBiografia(barbero.getBiografia());
        return barberoRepository.save(existing);
    }

    public void delete(Integer id) {
        getById(id);
        barberoRepository.deleteById(id);
    }
}