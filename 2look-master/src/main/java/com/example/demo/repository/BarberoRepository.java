package com.example.demo.repository;

import com.example.demo.model.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BarberoRepository extends JpaRepository<Barbero, Integer> {
    List<Barbero> findByEspecialidad(String especialidad);
}