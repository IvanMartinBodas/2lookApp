package com.example.demo.mapper;

import com.example.demo.dto.PersonaDTO;
import com.example.demo.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public PersonaDTO toDTO(Persona persona) {
        PersonaDTO dto = new PersonaDTO();
        dto.setId(persona.getId());
        dto.setNombre(persona.getNombre());
        dto.setEmail(persona.getEmail());
        dto.setRol(persona.getRol());
        dto.setFechaRegistro(persona.getFechaRegistro());
        return dto;
    }

    public Persona toEntity(PersonaDTO dto) {
        Persona persona = new Persona();
        persona.setId(dto.getId());
        persona.setNombre(dto.getNombre());
        persona.setEmail(dto.getEmail());
        persona.setRol(dto.getRol());
        return persona;
    }
}