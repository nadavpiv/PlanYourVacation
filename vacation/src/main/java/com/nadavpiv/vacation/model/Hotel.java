package com.nadavpiv.vacation.model;

public class Hotel {
    private String hotelName;
    private String hotelAddress;
    private Integer hotelPrice;

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public Integer getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(Integer hotelPrice) {
        if (hotelPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.hotelPrice = hotelPrice;
    }

    @Override
    public String toString() {
        return
                "Hotel Name: " + hotelName + '\n' +
                        "Hotel Address: " + hotelAddress + '\n' +
                        "Hotel Price: " + hotelPrice + '$' + '\n';
    }
}
