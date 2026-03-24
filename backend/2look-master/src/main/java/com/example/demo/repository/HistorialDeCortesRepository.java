package com.example.demo.repository;

import com.example.demo.model.HistorialDeCortes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialDeCortesRepository extends JpaRepository<HistorialDeCortes, Integer> {
    List<HistorialDeCortes> findByClienteId(Integer clienteId);
    List<HistorialDeCortes> findByReservaId(Integer reservaId);
}