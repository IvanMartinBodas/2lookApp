package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class ReservaDTO {
    private Integer id;
    private PersonaDTO cliente;
    private BarberoDTO barbero;
    private ServicioDTO servicio;
    private LocalDateTime fechaCita;
    private String estado;
    private String pagoStripeId;
    private BigDecimal cantidadPagada;
}