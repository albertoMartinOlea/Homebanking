package com.Minhub.homebanking.dtos;

public class PaymentApplicationDTO {


    private String numberCard;

    private long cvv;

    private double amount;

    private String description;

    public PaymentApplicationDTO() {
    }

    public PaymentApplicationDTO(String numberCard, long cvv, double amount, String description) {
        this.numberCard = numberCard;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public long getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
