package com.example.demo.repository;

import com.example.demo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByClienteId(Integer clienteId);
    List<Reserva> findByBarberoId(Integer barberoId);
    List<Reserva> findByEstado(String estado);
}