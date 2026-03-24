package com.example.demo.mapper;

import com.example.demo.dto.BarberoDTO;
import com.example.demo.model.Barbero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarberoMapper {

    private final PersonaMapper personaMapper;

    public BarberoDTO toDTO(Barbero barbero) {
        BarberoDTO dto = new BarberoDTO();
        dto.setId(barbero.getId());
        dto.setEspecialidad(barbero.getEspecialidad());
        dto.setFotoUrl(barbero.getFotoUrl());
        dto.setValoracion(barbero.getValoracion());
        dto.setBiografia(barbero.getBiografia());
        dto.setPersona(personaMapper.toDTO(barbero.getPersona()));
        return dto;
    }

    public Barbero toEntity(BarberoDTO dto) {
        Barbero barbero = new Barbero();
        barbero.setId(dto.getId());
        barbero.setEspecialidad(dto.getEspecialidad());
        barbero.setFotoUrl(dto.getFotoUrl());
        barbero.setValoracion(dto.getValoracion());
        barbero.setBiografia(dto.getBiografia());
        return barbero;
    }
}