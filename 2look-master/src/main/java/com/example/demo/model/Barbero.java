package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "barbero")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Barbero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "persona_id", nullable = false, unique = true)
    private Persona persona;

    @Column(length = 100)
    private String especialidad;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column
    private Double valoracion;

    @Column(length = 500)
    private String biografia;
}