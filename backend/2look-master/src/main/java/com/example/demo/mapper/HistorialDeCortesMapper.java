package com.example.demo.mapper;

import com.example.demo.dto.HistorialDeCortesDTO;
import com.example.demo.model.HistorialDeCortes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistorialDeCortesMapper {

    private final PersonaMapper personaMapper;
    private final ReservaMapper reservaMapper;

    public HistorialDeCortesDTO toDTO(HistorialDeCortes historial) {
        HistorialDeCortesDTO dto = new HistorialDeCortesDTO();
        dto.setId(historial.getId());
        dto.setCliente(personaMapper.toDTO(historial.getCliente()));
        dto.setReserva(reservaMapper.toDTO(historial.getReserva()));
        dto.setCorteSugeridoIa(historial.getCorteSugeridoIa());
        dto.setCorteRealizado(historial.getCorteRealizado());
        dto.setFechaCorte(historial.getFechaCorte());
        return dto;
    }

    public HistorialDeCortes toEntity(HistorialDeCortesDTO dto) {
        HistorialDeCortes historial = new HistorialDeCortes();
        historial.setId(dto.getId());
        historial.setCorteSugeridoIa(dto.getCorteSugeridoIa());
        historial.setCorteRealizado(dto.getCorteRealizado());
        return historial;
    }
}