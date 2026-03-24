package com.example.demo.dto;

import lombok.Data;

@Data
public class BarberoDTO {
    private Integer id;
    private String especialidad;
    private String fotoUrl;
    private Double valoracion;
    private String biografia;
    private PersonaDTO persona;
}