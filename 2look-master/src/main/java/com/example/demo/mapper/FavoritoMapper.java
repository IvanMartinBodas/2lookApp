package com.example.demo.mapper;

import com.example.demo.dto.FavoritoDTO;
import com.example.demo.model.Favorito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoritoMapper {

    private final PersonaMapper personaMapper;
    private final BarberoMapper barberoMapper;

    public FavoritoDTO toDTO(Favorito favorito) {
        FavoritoDTO dto = new FavoritoDTO();
        dto.setId(favorito.getId());
        dto.setCliente(personaMapper.toDTO(favorito.getCliente()));
        dto.setBarbero(barberoMapper.toDTO(favorito.getBarbero()));
        dto.setFechaFavorito(favorito.getFechaFavorito());
        return dto;
    }

    public Favorito toEntity(FavoritoDTO dto) {
        Favorito favorito = new Favorito();
        favorito.setId(dto.getId());
        favorito.setFechaFavorito(dto.getFechaFavorito());
        return favorito;
    }
}