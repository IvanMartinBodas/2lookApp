package com.example.demo.service;

import com.example.demo.model.Reserva;
import com.example.demo.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }

    public Reserva getById(Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    public List<Reserva> getByClienteId(Integer clienteId) {
        return reservaRepository.findByClienteId(clienteId);
    }

    public List<Reserva> getByBarberoId(Integer barberoId) {
        return reservaRepository.findByBarberoId(barberoId);
    }

    public List<Reserva> getByEstado(String estado) {
        return reservaRepository.findByEstado(estado);
    }

    public Reserva create(Reserva reserva) {
        reserva.setEstado("PENDIENTE");
        return reservaRepository.save(reserva);
    }

    public Reserva updateEstado(Integer id, String estado) {
        Reserva existing = getById(id);
        existing.setEstado(estado);
        return reservaRepository.save(existing);
    }

    public Reserva marcarPagada(Integer id, String pagoStripeId) {
        Reserva reserva = getById(id);
        reserva.setEstado("PAGADA");
        reserva.setPagoStripeId(pagoStripeId);
        return reservaRepository.save(reserva);
    }

    public void delete(Integer id) {
        getById(id);
        reservaRepository.deleteById(id);
    }
}