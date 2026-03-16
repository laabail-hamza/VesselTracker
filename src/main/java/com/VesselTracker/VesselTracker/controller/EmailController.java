package com.VesselTracker.VesselTracker.controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.VesselTracker.VesselTracker.service.DistanceService;

import reactor.core.publisher.Mono;

@RestController

public class EmailController {

    private final JavaMailSender mailSender;
    private final DistanceService distanceService;

    public EmailController(JavaMailSender mailSender, DistanceService distanceService) {
        this.mailSender = mailSender;
        this.distanceService = distanceService;
    }

    @GetMapping("/send-distance-email")
public Mono<String> sendDistanceEmail(
        @RequestParam String imo,
        @RequestParam String apiKey,
        @RequestParam String place,
        @RequestParam String email) {

    return distanceService.calculateDistanceFromPlace(imo, apiKey, place)
            .map(distance -> {
                String distanceText = String.format("%.2f km", distance);

                if (distance < 20000) {
                    try {
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setFrom("hamza.laabail.uhp@gmail.com");
                        message.setTo(email);
                        message.setSubject("Navire proche de " + place);
                        message.setText("Le navire est proche de " + place +
                                        "\nDistance actuelle : " + distanceText);
                        mailSender.send(message);
                        return "Email envoyé ✔ Distance : " + distanceText;
                    } catch (Exception e) {
                        e.printStackTrace(); // <- Ajoute cette ligne
                        return "Erreur email : " + e.getMessage();
                    }
                } else {
                    return "Distance trop grande (" + distanceText + ") → email non envoyé";
                }
            })
            .onErrorResume(e -> {
                e.printStackTrace(); // <- Ajoute cette ligne
                return Mono.just("Erreur serveur : " + e.getMessage());
            });
}
}