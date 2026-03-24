package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistorialDeCortesDTO {
    private Integer id;
    private PersonaDTO cliente;
    private ReservaDTO reserva;
    private String corteSugeridoIa;
    private String corteRealizado;
    private LocalDateTime fechaCorte;
}