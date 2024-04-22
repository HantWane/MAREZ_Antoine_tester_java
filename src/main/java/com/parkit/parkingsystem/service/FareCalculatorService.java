package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    // Constante pour la réduction de tarif
    private static final double DISCOUNT_RATE = 0.95;

    // Méthode pour calculer le tarif du ticket
    public void calculateFare(Ticket ticket) {
        // Appelle la méthode de calcul avec le paramètre de réduction par défaut à false
        calculateFare(ticket, false);
    }

    // Méthode pour calculer le tarif du ticket avec la possibilité de spécifier une réduction
    public void calculateFare(Ticket ticket, boolean discount) {
        // Validation du ticket pour s'assurer que les données sont cohérentes
        validateTicket(ticket);
        // Calcul de la durée de stationnement en minutes
        double durationMinutes = calculateDurationInMinutes(ticket);
        // Calcul du tarif en fonction du type de véhicule
        double fareRate = calculateFareRate(ticket);
        // Si la durée de stationnement est inférieure ou égale à 30 minutes, le tarif est fixé à zéro
        if (durationMinutes <= 30) {
        ticket.setPrice(0);
        return;
        }
        // Si la réduction est activée, applique le taux de réduction
        if (discount) {
            fareRate *= DISCOUNT_RATE;
        }
        // Calcul du prix total du ticket
        ticket.setPrice(durationMinutes * fareRate / 60);
    }

    // Méthode pour valider le ticket et ses données
    private void validateTicket(Ticket ticket) {
        // Vérifie si l'heure de sortie est valide et postérieure à l'heure d'entrée
        if (ticket.getOutTime() == null || ticket.getOutTime().before(ticket.getInTime())) {
            // Lève une exception si l'heure de sortie est invalide
            throw new IllegalArgumentException("Invalid out time provided: " + ticket.getOutTime());
        }
    }

    // Méthode pour calculer la durée de stationnement en minutes
    private double calculateDurationInMinutes(Ticket ticket) {
        long inTimeMillis = ticket.getInTime().getTime();
        long outTimeMillis = ticket.getOutTime().getTime();
        return (outTimeMillis - inTimeMillis) / 60000.0;
    }

    // Méthode pour calculer le tarif en fonction du type de véhicule
    private double calculateFareRate(Ticket ticket) {
        switch (ticket.getParkingSpot().getParkingType()) {
            
            case CAR:
                return Fare.CAR_RATE_PER_HOUR;
            
            case BIKE:
                return Fare.BIKE_RATE_PER_HOUR;
            
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}
