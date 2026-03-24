package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OpinionDTO {
    private Integer id;
    private PersonaDTO cliente;
    private BarberoDTO barbero;
    private Integer puntuacion;
    private String comentario;
    private LocalDateTime fecha;
}