package com.example.demo.service;

import com.example.demo.model.Servicio;
import com.example.demo.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public List<Servicio> getAll() {
        return servicioRepository.findAll();
    }

    public Servicio getById(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + id));
    }

    public Servicio create(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public Servicio update(Integer id, Servicio servicio) {
        Servicio existing = getById(id);
        existing.setNombre(servicio.getNombre());
        existing.setDescripcion(servicio.getDescripcion());
        existing.setPrecio(servicio.getPrecio());
        existing.setDuracionMin(servicio.getDuracionMin());
        return servicioRepository.save(existing);
    }

    public void delete(Integer id) {
        getById(id);
        servicioRepository.deleteById(id);
    }
}