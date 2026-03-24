package com.example.demo.mapper;

import com.example.demo.dto.ReservaDTO;
import com.example.demo.model.Reserva;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservaMapper {

    private final PersonaMapper personaMapper;
    private final BarberoMapper barberoMapper;
    private final ServicioMapper servicioMapper;

    public ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setCliente(personaMapper.toDTO(reserva.getCliente()));
        dto.setBarbero(barberoMapper.toDTO(reserva.getBarbero()));
        dto.setServicio(servicioMapper.toDTO(reserva.getServicio()));
        dto.setFechaCita(reserva.getFechaCita());
        dto.setEstado(reserva.getEstado());
        dto.setPagoStripeId(reserva.getPagoStripeId());
        dto.setCantidadPagada(reserva.getCantidadPagada());
        return dto;
    }

    public Reserva toEntity(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setId(dto.getId());
        reserva.setFechaCita(dto.getFechaCita());
        reserva.setEstado(dto.getEstado());
        reserva.setPagoStripeId(dto.getPagoStripeId());
        reserva.setCantidadPagada(dto.getCantidadPagada());
        return reserva;
    }
}