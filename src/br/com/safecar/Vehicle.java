package br.com.safecar;

public class Vehicle {
    enum VehicleType {
        CAR, MOTORCYCLE;
    }

    public Vehicle(VehicleType type, String licensePlate) {
        this.type = type;
        this.licensePlate = licensePlate;
    }

    private VehicleType type;
    private String licensePlate;

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String toString() {
        return "" + this.getType() + "(" + this.getLicensePlate() + ")";
    }


}

