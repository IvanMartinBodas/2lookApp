package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicioDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer duracionMin;
}