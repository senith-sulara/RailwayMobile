package com.example.railridemate.models;

public class BookingModel {
    private String from;
    private String to;
    private String date;
    private boolean returnOption; // Existing field for the return option
    private int passengers; // New field for the passengers count

    // Constructors
    public BookingModel() {
        // Default constructor
    }

    public BookingModel(String from, String to, String date, boolean returnOption, int passengers) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.returnOption = returnOption;
        this.passengers = passengers;
    }

    // Getters and setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isReturnOption() {
        return returnOption;
    }

    public void setReturnOption(boolean returnOption) {
        this.returnOption = returnOption;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
}