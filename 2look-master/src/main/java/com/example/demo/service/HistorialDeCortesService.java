package com.example.demo.service;

import com.example.demo.model.HistorialDeCortes;
import com.example.demo.repository.HistorialDeCortesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialDeCortesService {

    private final HistorialDeCortesRepository historialRepository;

    public List<HistorialDeCortes> getByClienteId(Integer clienteId) {
        return historialRepository.findByClienteId(clienteId);
    }

    public HistorialDeCortes getById(Integer id) {
        return historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con id: " + id));
    }

    public HistorialDeCortes create(HistorialDeCortes historial) {
        return historialRepository.save(historial);
    }

    public void delete(Integer id) {
        getById(id);
        historialRepository.deleteById(id);
    }
}