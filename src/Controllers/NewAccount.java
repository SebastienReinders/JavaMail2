package Controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NewAccount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Création d'un nouveau compte :");
        System.out.print("Login : ");
        String login = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();

        // Enregistrement des données dans le fichier login.dat
        saveAccountData(login, password);

        System.out.println("Compte créé avec succès !");
    }

    private static void saveAccountData(String login, String password) {
        try {
            FileWriter fileWriter = new FileWriter("login.dat");
            fileWriter.write("Login : " + login + "\n");
            fileWriter.write("Mot de passe : " + password + "\n");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'enregistrement des données dans le fichier.");
            e.printStackTrace();
        }
    }
}
