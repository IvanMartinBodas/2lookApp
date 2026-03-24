package com.example.demo.mapper;

import com.example.demo.dto.ServicioDTO;
import com.example.demo.model.Servicio;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {

    public ServicioDTO toDTO(Servicio servicio) {
        ServicioDTO dto = new ServicioDTO();
        dto.setId(servicio.getId());
        dto.setNombre(servicio.getNombre());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setPrecio(servicio.getPrecio());
        dto.setDuracionMin(servicio.getDuracionMin());
        return dto;
    }

    public Servicio toEntity(ServicioDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setId(dto.getId());
        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setDuracionMin(dto.getDuracionMin());
        return servicio;
    }
}