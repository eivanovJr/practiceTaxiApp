package com.example.taxiapp.model;


public class TaxiDriver implements Comparable<TaxiDriver>{
    private String name = "Driver";
    private Long price;
    private String rideClass;

    public TaxiDriver(String name, Long price, String rideClass) {
        if (name != null)
            this.name = name;
        this.price = price;
        this.rideClass = rideClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getRideClass() {return rideClass;}

    public String toString() {
        return new String(price + " " + rideClass + " " + name + "\n");
    }

    @Override
    public int compareTo(TaxiDriver taxiDriver) {
        if (this.price < taxiDriver.price)
            return -1;
        else if (this.price.equals(taxiDriver.price))
            return 0;
        else
            return 1;
    }
}
