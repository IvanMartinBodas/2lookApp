package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FavoritoDTO {
    private Integer id;
    private PersonaDTO cliente;
    private BarberoDTO barbero;
    private LocalDateTime fechaFavorito;
}