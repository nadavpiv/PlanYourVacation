package com.nadavpiv.vacation.model;

public class Flight {
    private String companyName;
    private String inboundFlight;
    private String outboundFlight;
    private Integer flightPrice;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInboundFlight() {
        return inboundFlight;
    }

    public void setInboundFlight(String inboundFlight) {
        this.inboundFlight = inboundFlight;
    }

    public String getOutboundFlight() {
        return outboundFlight;
    }

    public void setOutboundFlight(String outboundFlight) {
        this.outboundFlight = outboundFlight;
    }

    public Integer getFlightPrice() {
        return flightPrice;
    }

    public void setFlightPrice(Integer flightPrice) {
        if (flightPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.flightPrice = flightPrice;
    }

    @Override
    public String toString() {
        return
                "Company Name: " + companyName + '\n' +
                        "Inbound Flight: " + inboundFlight + '\n' +
                        "Outbound Flight: " + outboundFlight + '\n' +
                        "Flight Price: " + flightPrice + '$' + '\n';
    }
}

