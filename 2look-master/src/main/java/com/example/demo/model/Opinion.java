package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "opinion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Opinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Persona cliente;

    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;

    @Column(nullable = false)
    private Integer puntuacion;

    @Column(length = 500)
    private String comentario;

    @Column(updatable = false)
    private LocalDateTime fecha;

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }
}