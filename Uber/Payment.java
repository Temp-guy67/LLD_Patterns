package Uber;

import java.time.LocalDateTime;

import Uber.Constants.PaymentStatus;

public class Payment {
    private String paymentId;
    private Ride ride;
    private double amount;
    private PaymentStatus status;
    private LocalDateTime timestamp;

    public Payment(String paymentId, Ride ride, double amount) {
        this.paymentId = paymentId;
        this.ride = ride;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.timestamp = LocalDateTime.now();
    }

    public void processPayment() {
        // Payment processing logic
        this.status = PaymentStatus.COMPLETED;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}