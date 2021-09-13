package br.com.safecar;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SafeCar {
    static class HourPrice {
        Double car;
        Double motorcycle;

        public HourPrice(Double car, Double motorcycle) {
            super();
            this.car = car;
            this.motorcycle = motorcycle;
        }

        public Double getCar() {
            return this.car;
        }

        public void setCar(Double car) {
            this.car = car;
        }

        public Double getMotorcycle() {
            return this.motorcycle;
        }

        public void setMotorcycle(Double motorcycle) {
            this.motorcycle = motorcycle;
        }

    }

    private static final HourPrice NORMAL_PRICE = new HourPrice(4.0, 2.0);
    private static final Map<Integer, HourPrice> DYNAMIC_PRICES = new HashMap<>();
    private static final Map<Integer, Vehicle> VACANCIES = new HashMap<>();

    public static void main(String[] args){
        populateDynamicTable();
        populateVacancies(10);
        do {
            System.out.println("\n\n\n\n");
            Integer option = SafeCar.createMenu();
            selectOption(option);
            try {
                System.in.read();
            } catch (Exception e) {
            }
        } while (true);
    }

    private static Integer createMenu() {

        System.out.println("\t\t\t Menu \n\n ");
        System.out.println("1 - List vacant parking spaces");
        System.out.println("2 - List free vacancies");
        System.out.println("3 - List occupied vacancies");
        System.out.println("\n4 - Receive Payment and Release Vacancy");
        System.out.println("5 - Occupy vacancy");
        System.out.println("6 - Time per vacancy");
        System.out.println("7 - Price list");
        System.out.println("8 - Revenues");
        System.out.println("\n0 - Exit");


        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static void selectOption(int option) {
        switch (option) {

            case 1:
                showAllVacancies();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                showPriceTable();
                break;
            case 8:
                break;
            default:
                System.exit(0);
                break;
        }

    }

    private static void showAllVacancies() {
        System.out.println("Vacancy\t Vehicle\t\t");
        for (int vacancy : VACANCIES.keySet()) {
            System.out.println(MessageFormat.format("{0}\t\t {1}", vacancy,
                    VACANCIES.get(vacancy) != null ? VACANCIES.get(vacancy).toString() : "X"));
        }
    }

    private static void showPriceTable() {
        System.out.println("Hour\t Car Price\t\t Motorcycle Price");
        for (int hour = 7; hour <= 18; hour++) {
            HourPrice hourPrice = DYNAMIC_PRICES.getOrDefault(hour, NORMAL_PRICE);
            System.out.println(MessageFormat.format("{0} \t\t\t {1}\t\t\t\t {2}", hour, hourPrice.getCar(), hourPrice.getMotorcycle()));
        }
    }

    private static void populateDynamicTable() {
        DYNAMIC_PRICES.put(7, new HourPrice(6.0, 5.0));
        DYNAMIC_PRICES.put(8, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(11, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(12, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(13, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(14, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(17, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(18, new HourPrice(5.0, 3.0));
    }

    private static void populateVacancies(int vacanciesNumber) {
        for (int vacancy = 0; vacancy < vacanciesNumber; vacancy++) {
            VACANCIES.put(vacancy, vacancy == 4 ? new Vehicle(Vehicle.VehicleType.CAR, "jkf4460") : null);
        }

    }

}
