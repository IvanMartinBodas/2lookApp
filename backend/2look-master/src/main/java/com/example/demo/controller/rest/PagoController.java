package com.example.demo.controller.rest;

import com.example.demo.service.ReservaService;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    private final ReservaService reservaService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> body) {
        try {
            Stripe.apiKey = stripeSecretKey;

            Number amountNum = (Number) body.getOrDefault("amount", 10);
            long amountCents = Math.round(amountNum.doubleValue() * 100);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountCents)
                    .setCurrency("eur")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            return ResponseEntity.ok(Map.of(
                    "clientSecret", intent.getClientSecret(),
                    "paymentIntentId", intent.getId(),
                    "amount", amountCents
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestBody Map<String, Object> body) {
        try {
            Number reservaIdNum = (Number) body.get("reservaId");
            String paymentIntentId = (String) body.get("paymentIntentId");

            if (reservaIdNum != null && paymentIntentId != null) {
                reservaService.marcarPagada(reservaIdNum.intValue(), paymentIntentId);
            }

            return ResponseEntity.ok(Map.of(
                    "ok", true,
                    "reservaId", reservaIdNum != null ? reservaIdNum.intValue() : 0
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
