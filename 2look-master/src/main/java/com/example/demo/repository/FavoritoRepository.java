package com.example.demo.repository;

import com.example.demo.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {
    List<Favorito> findByClienteId(Integer clienteId);
    Optional<Favorito> findByClienteIdAndBarberoId(Integer clienteId, Integer barberoId);
    boolean existsByClienteIdAndBarberoId(Integer clienteId, Integer barberoId);
}