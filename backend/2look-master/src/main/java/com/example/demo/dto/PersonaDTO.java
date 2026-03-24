package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PersonaDTO {
    private Integer id;
    private String nombre;
    private String email;
    private String rol;
    private LocalDateTime fechaRegistro;
}