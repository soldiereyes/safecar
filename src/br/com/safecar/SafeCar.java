package br.com.safecar;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

        public Double getValueByVehicleType(Vehicle.VehicleType type) {
            return type == Vehicle.VehicleType.CAR ? getCar() : getMotorcycle();
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
        populateRegistry(6);
        do {
            System.out.println("\n\n\n\n");
            Integer option = SafeCar.createMenu();
            try {
                selectOption(option);
            } catch (Exception e) {
                e.printStackTrace();
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

    private static void selectOption(int option) {
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
                releaseVacancy();
                break;
            case 6:
                showPriceTable();
                break;
            case 7:
                calculateAndShowRevenues();
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
        DYNAMIC_PRICES.put(8, new HourPrice(6.0, 5.0));
        DYNAMIC_PRICES.put(11, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(12, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(13, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(14, new HourPrice(5.0, 3.0));
        DYNAMIC_PRICES.put(17, new HourPrice(6.0, 5.0));
        DYNAMIC_PRICES.put(18, new HourPrice(6.0, 5.0));
    }

    private static void populateVacancies(int vacanciesNumber) {
        for (int vacancy = 0; vacancy < vacanciesNumber; ++vacancy) {
            switch (vacancy) {
                case 2:
                    OccupationRegistry occupation2 = new OccupationRegistry(new Vehicle(Vehicle.VehicleType.CAR, "BCD-0101"), vacancy);
                    occupation2.setArrival(LocalTime.of(7, 31, 0));
                    VACANCIES.put(vacancy, occupation2);
                    break;
                case 4:
                    OccupationRegistry occupation4 = new OccupationRegistry(new Vehicle(Vehicle.VehicleType.CAR, "DEF-1111"), vacancy);
                    occupation4.setArrival(LocalTime.of(9, 0, 40));
                    VACANCIES.put(vacancy, occupation4);
                    break;
                case 6:
                    OccupationRegistry occupation6 = new OccupationRegistry(new Vehicle(Vehicle.VehicleType.CAR, "FGH-1212"), vacancy);
                    occupation6.setArrival(LocalTime.of(17, 12, 31));
                    VACANCIES.put(vacancy, occupation6);
                    break;
                default:
                    VACANCIES.put(vacancy, null);
                    break;
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
                            System.out.print(MessageFormat.format("{0}\t\t {1}", vacancy, "X"));

                        }
                        break;

                }
            }
            if (occupation != null) {
                Duration diff = occupation.getOccupationTime();
                System.out.println("\t\t" + String.format("%d:%02d:%02d", diff.toHours(), (diff.toMinutes()) % 60, (diff.getSeconds() % 60)));
            } else {
                System.out.print("\n");
            }

        }
    }

    private static void occupyVacancy() {
        showVacanciesByStatus(null);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the vehicle type: 0 for Car or 1 for Motorcycle ");
        Integer vehicleType = readInt(scanner);
        System.out.println("Enter the vehicle plate: ");
        String plate = readString(scanner);
        Integer vacancy;
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

    private static void releaseVacancy() {
        showVacanciesByStatus(null);

        // Read the vacancy slot number to be released
        Scanner scanner = new Scanner(System.in);
        System.out.println("Release vacancy\n\n");
        Integer vacancyNumber = readVacancy(scanner);

        // Get the occupation registry from the vacancies map and releases the vacancy slot.
        OccupationRegistry occupation = VACANCIES.get(vacancyNumber);
        VACANCIES.put(vacancyNumber, null);

        occupation.leave();
        BigDecimal occupationPrice = calculateOccupationPrice(occupation);

        System.out.print("The suggested charge for the vacancy rent was: " + occupationPrice.setScale(2, RoundingMode.CEILING) + "\n\n\n");
        System.out.println("Would you like to keep this charge? \t 1 - Yes \t 0 - No");
        Boolean keepCalculatedCharge = readInt(scanner) != 0;

        if (!keepCalculatedCharge) {
            System.out.println("Enter the charge Value:");
            String charge = readString(scanner);
            occupationPrice = new BigDecimal(charge);
        }

        occupation.setChargedValue(occupationPrice);
        OCCUPATION_REGISTRIES.add(occupation);

        System.out.println("\n\n\n");
        System.out.println("R$ " + occupationPrice.setScale(2, RoundingMode.HALF_UP) + " Payment Registered.");
        System.out.println("Vacancy released!");
    }

    private static BigDecimal calculateOccupationPrice(OccupationRegistry occupation) {
        // Get duration in hours and ceil it to the next integer value if it has some floating point ex: 1.2 or 1.0000002
        double duration = ((double) occupation.getOccupationTime().toMinutes())/60;
        int durationInHours = Double.valueOf(Math.ceil(duration)).intValue();

        // Initializes the price to be charged as zero.
        BigDecimal charge = BigDecimal.ZERO;
        /* For each spent hour in a parking slot, the price to be charged will be consulted in the price table
        and then added up to the charge total */
        for (int counted_hour = 1, hour = occupation.getArrival().getHour(); counted_hour <= durationInHours; counted_hour++, hour++) {
            // Get the price reference from the Map of prices
            HourPrice priceReference = DYNAMIC_PRICES.getOrDefault(hour, NORMAL_PRICE);
            // Get the price charge by the type of Vehicle
            Double hourCharge = priceReference.getValueByVehicleType(occupation.getVehicle().getType());
            // Add the hour charge to the total price to be charged
            charge = charge.add(BigDecimal.valueOf(hourCharge));
        }
        return charge;
    }


    private static void calculateAndShowRevenues() {
        /*TODO:
         *  Somar todos os valores pagos (OccupationRegistry.chargedValue) e exibir formatado
         *  Ex:
         *      `Total Arrecadado: R$ 530,00`
         *
         * Dica:
         *  Os valores arrecadados estão salvos em BigDecimal por ser o melhor tipo para lidar com
         *  Valores financeiros em java. Para somar BigDecimal você só precisa criar uma variável e
         *  atribuir o valor inicial de 0 a ela:
         *              `BigDecimal var = BigDecimal.ZERO`; ou `BigDecimal var = new BigDecimal(0)`
         *  e depois chamar o método add() do BigDecimal passando como parâmetro o valor a ser somado.
         *  Qualquer dúvida só olhar o método releaseVacancy.
         *
         */
    }


    private static Integer readVacancy(Scanner scanner) {
        System.out.println("Enter the vacancy number: ");
        return readInt(scanner);
    }

    private static int readInt(Scanner scanner) {
        int choice = 0;
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                break;
            } else {
                scanner.next(); // Just discard this, not interested...
            }
        }
        return choice;
    }

    private static String readString(Scanner scanner) {
        String choice = "";
        if (scanner.hasNext()) {
            choice = scanner.next();
        }
        return choice;
    }

    //TODO: Remover na versão final

    /**
     * Método utilitário para criar registros semi-aleatórios de registro de ocupações
     */
    private static void populateRegistry(int registryEntries) {
        Random rnd = new Random();
        for (int i = 0; i < registryEntries; i++) {
            // Random vacancy
            int vacancyNumber = rnd.nextInt(10);
            // Creates a random occupation, with a random vehicle in a random vacancy number
            OccupationRegistry o = new OccupationRegistry(new Vehicle(Vehicle.VehicleType.values()[rnd.nextInt(2)], "BCD-" + rnd.nextInt(9000) + 1000), vacancyNumber);
            o.setArrival(LocalTime.of(rnd.nextInt(7), rnd.nextInt(60), rnd.nextInt(60)));

            // sets a random spent time
            LocalTime departure = o.getArrival();
            departure = departure.plusHours(rnd.nextInt(10))
                    .plusMinutes(rnd.nextInt(60))
                    .plusSeconds(rnd.nextInt(60));

            o.setDeparture(departure);

            OCCUPATION_REGISTRIES.add(o);
        }
    }
}





