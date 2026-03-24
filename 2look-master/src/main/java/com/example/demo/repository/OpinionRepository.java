package com.example.demo.repository;

import com.example.demo.model.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Integer> {
    List<Opinion> findByBarberoId(Integer barberoId);
    List<Opinion> findByClienteId(Integer clienteId);
}