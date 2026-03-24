package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Persona cliente;

    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;

    @Column(name = "fecha_favorito", updatable = false)
    private LocalDateTime fechaFavorito;

    @PrePersist
    protected void onCreate() {
        fechaFavorito = LocalDateTime.now();
    }
}