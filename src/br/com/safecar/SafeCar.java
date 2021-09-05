package br.com.safecar;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

import java.util.Scanner;

public class SafeCar {
    public static void main(String[] args) {
        do {
            Integer opcao = SafeCar.createMenu();
            System.out.printf("Option1 %d%n", opcao);
        } while (true);
    }

    private static Integer createMenu() {
        System.out.println("\t\t\t Menu \n\n ");
        System.out.println("1 - List vacant parking spaces");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

}
