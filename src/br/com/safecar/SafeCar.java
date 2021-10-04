package br.com.safecar;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

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

    enum VacancyStatus {
        FREE("Free"), OCCUPIED("Occupied");
        public String label;

        VacancyStatus(String label) {
            this.label = label;
        }
    }

    private static final HourPrice NORMAL_PRICE = new HourPrice(4.0, 2.0);
    private static final Map<Integer, HourPrice> DYNAMIC_PRICES = new HashMap<>();
    private static final Map<Integer, OccupationRegistry> VACANCIES = new HashMap<>();
    private static final List<OccupationRegistry> OCCUPATION_REGISTRIES = new ArrayList<>();


    public static void main(String[] args) {
        populateDynamicTable();
        populateVacancies(10);
        do {
            System.out.println("\n\n\n\n");
            Integer option = SafeCar.createMenu();
            try {
                selectOption(option);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    private static Integer createMenu() {

        System.out.println("\t\t\t Menu \n\n ");
        System.out.println("1 - List vacant parking spaces");
        System.out.println("2 - List free vacancies");
        System.out.println("3 - List occupied vacancies");
        System.out.println("4 - Occupy vacancy");
        System.out.println("5 - Receive Payment and Release Vacancy");
        System.out.println("6 - Price list");
        System.out.println("7 - Revenues"); // Para cada um dos registros da lista OccupationRegistry calcule o valor x horas e somar
        System.out.println("\n0 - Exit");

//  Metodo de entrada
        Scanner scanner = new Scanner(System.in);
        return readInt(scanner); // captura de inteiros
    }

    private static void selectOption(int option) throws Exception { // throws Exception - If option five throw error throw it again
        switch (option) {

            case 1:
                showVacanciesByStatus(null);
                break;
            case 2:
                showVacanciesByStatus(VacancyStatus.FREE);
                break;
            case 3:
                showVacanciesByStatus(VacancyStatus.OCCUPIED);
                break;
            case 4:
                occupyVacancy();
                break;
            case 5:
                break;
            case 6:
                showPriceTable();
                break;
            case 7:
                break;
            default:
                System.exit(0);
                break;
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
            if(vacancy == 3){
                OccupationRegistry occupation = new OccupationRegistry(new Vehicle(Vehicle.VehicleType.CAR,"JKL-0102"),vacancy);
                occupation.setArrival(LocalTime.of(2,33,0));
                VACANCIES.put(vacancy, occupation);
            }
            else{
                VACANCIES.put(vacancy, null);
            }
        }

    }

    private static void showVacanciesByStatus(VacancyStatus status) {
        System.out.printf("%s Vacancies\n", status == null ? "All" : status.label);
        if (status == null) {
            System.out.println("Vacancy\t Vehicle\t\t");
        }
        for (int vacancy : VACANCIES.keySet()) {
            OccupationRegistry occupation = VACANCIES.get(vacancy);
            if (status == null) {
                System.out.print(MessageFormat.format("{0}\t\t {1}", vacancy,
                        occupation != null ? occupation.getVehicle().toString() : "X"));
            } else {
                switch (status) {
                    case OCCUPIED:
                        if (occupation != null) {
                            System.out.print(MessageFormat.format("{0}\t\t {1}", vacancy,
                                    occupation.getVehicle().toString()));

                        }
                        break;
                    case FREE:
                        if (occupation == null) {
                            System.out.print(MessageFormat.format("{0}\t\t {1}", vacancy,"X"));

                        }
                        break;

                }
            }
            if( occupation != null){
                Duration  diff = occupation.getOccupationTime();
                System.out.println("\t\t" + String.format("%d:%02d:%02d", diff.toHours(), (diff.toMinutes()) % 60, (diff.getSeconds() %60)));
            }else {
                System.out.print("\n");
            }

        }
    }

    private static void occupyVacancy() throws Exception {
        showVacanciesByStatus(null);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the vehicle type: 0 for Car or 1 for Motorcycle ");
        Integer vehicleType = readInt(scanner);
        System.out.println("Enter the vehicle plate: ");
        String plate = readString(scanner);
        Integer vacancy = null;
        while (true) {
            vacancy = readVacancy(scanner);
            if (VACANCIES.get(vacancy) != null) {
                System.out.println("This vacancy is occupied, inset valid vacancy! ");
                continue;
            }
            break;


        }
        Vehicle vehicle = new Vehicle(Vehicle.VehicleType.values()[vehicleType], plate);
        OccupationRegistry occupation = new OccupationRegistry(vehicle, vacancy);
        VACANCIES.put(vacancy, occupation);


    }

    private static Integer readVacancy(Scanner scanner) {
        System.out.println("Enter the vacancy number: ");
        return readInt(scanner);
    }
    private static int readInt(Scanner scanner){
        int choice = 0;
        while (scanner.hasNext()){
            if (scanner.hasNextInt()){
                choice = scanner.nextInt();
                break;
            } else {
                scanner.next(); // Just discard this, not interested...
            }
        }
        return choice;
    }
    private static String readString(Scanner scanner){
        String choice = "";
        if(scanner.hasNext()){
            choice = scanner.next();
        }
        return choice;
    }

}





