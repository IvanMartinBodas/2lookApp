package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_de_cortes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialDeCortes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Persona cliente;

    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @Column(name = "corte_sugerido_ia", length = 255)
    private String corteSugeridoIa;

    @Column(name = "corte_realizado", length = 255)
    private String corteRealizado;

    @Column(name = "fecha_corte", updatable = false)
    private LocalDateTime fechaCorte;

    @PrePersist
    protected void onCreate() {
        fechaCorte = LocalDateTime.now();
    }
}