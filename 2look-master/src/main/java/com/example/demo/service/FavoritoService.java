package com.example.demo.service;

import com.example.demo.model.Favorito;
import com.example.demo.repository.FavoritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;

    public List<Favorito> getByClienteId(Integer clienteId) {
        return favoritoRepository.findByClienteId(clienteId);
    }

    public Favorito create(Favorito favorito) {
        if (favoritoRepository.existsByClienteIdAndBarberoId(
                favorito.getCliente().getId(), favorito.getBarbero().getId())) {
            throw new RuntimeException("Este barbero ya está en favoritos");
        }
        return favoritoRepository.save(favorito);
    }

    public void delete(Integer id) {
        if (!favoritoRepository.existsById(id)) {
            throw new RuntimeException("Favorito no encontrado con id: " + id);
        }
        favoritoRepository.deleteById(id);
    }
}