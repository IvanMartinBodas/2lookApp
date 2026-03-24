package com.example.demo.mapper;

import com.example.demo.dto.OpinionDTO;
import com.example.demo.model.Opinion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpinionMapper {

    private final PersonaMapper personaMapper;
    private final BarberoMapper barberoMapper;

    public OpinionDTO toDTO(Opinion opinion) {
        OpinionDTO dto = new OpinionDTO();
        dto.setId(opinion.getId());
        dto.setCliente(personaMapper.toDTO(opinion.getCliente()));
        dto.setBarbero(barberoMapper.toDTO(opinion.getBarbero()));
        dto.setPuntuacion(opinion.getPuntuacion());
        dto.setComentario(opinion.getComentario());
        dto.setFecha(opinion.getFecha());
        return dto;
    }

    public Opinion toEntity(OpinionDTO dto) {
        Opinion opinion = new Opinion();
        opinion.setId(dto.getId());
        opinion.setPuntuacion(dto.getPuntuacion());
        opinion.setComentario(dto.getComentario());
        return opinion;
    }
}