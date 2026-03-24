package com.example.demo.service;

import com.example.demo.model.Opinion;
import com.example.demo.repository.OpinionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpinionService {

    private final OpinionRepository opinionRepository;

    public List<Opinion> getAll() {
        return opinionRepository.findAll();
    }

    public Opinion getById(Integer id) {
        return opinionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opinion no encontrada con id: " + id));
    }

    public List<Opinion> getByBarberoId(Integer barberoId) {
        return opinionRepository.findByBarberoId(barberoId);
    }

    public List<Opinion> getByClienteId(Integer clienteId) {
        return opinionRepository.findByClienteId(clienteId);
    }

    public Opinion create(Opinion opinion) {
        if (opinion.getPuntuacion() < 1 || opinion.getPuntuacion() > 5) {
            throw new RuntimeException("La puntuación debe estar entre 1 y 5");
        }
        return opinionRepository.save(opinion);
    }

    public void delete(Integer id) {
        getById(id);
        opinionRepository.deleteById(id);
    }
}