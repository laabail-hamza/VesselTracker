package com.VesselTracker.VesselTracker.controller;

import com.VesselTracker.VesselTracker.service.DistanceService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

                    String text = "Distance entre " + place +
                            " et le navire : " +
                            String.format("%.2f km", distance);

                    try {

                        SimpleMailMessage message = new SimpleMailMessage();

                        message.setFrom("hamza.laabail.uhp@gmail.com");
                        message.setTo(email);
                        message.setSubject("Distance du navire");
                        message.setText(text);

                        mailSender.send(message);

                        return "Email envoyé avec succès : " + text;

                    } catch (Exception e) {
                        return "Erreur lors de l'envoi : " + e.getMessage();
                    }

                });
    }
}