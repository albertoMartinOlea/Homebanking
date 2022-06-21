package com.Minhub.homebanking.dtos;

public class LoanApplicationDTO {


    private long loanId;

    private Double amount;

    private int payments;

    private String numberAccountDestiny;


    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long loanId, Double amount, int payments, String numberAccountDestiny) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.numberAccountDestiny = numberAccountDestiny;
    }

    public long getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getNumberAccountDestiny() {
        return numberAccountDestiny;
    }
}
