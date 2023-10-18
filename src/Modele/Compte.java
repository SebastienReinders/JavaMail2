package Modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Compte {
    private String nomUtilisateur;
    private String motDePasse;

    public Compte(String nomUtilisateur, String motDePasse) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public static Compte chargerDepuisFichier() {
        try (BufferedReader br = new BufferedReader(new FileReader("login.dat"))) {
            String ligne;
            String login = null;
            String motDePasse = null;

            while ((ligne = br.readLine()) != null) {
                if (ligne.startsWith("Login : ")) {
                    login = ligne.substring("Login : ".length()).trim();
                } else if (ligne.startsWith("Mot de passe : ")) {
                    motDePasse = ligne.substring("Mot de passe : ".length()).trim();
                }
            }

            if (login != null && motDePasse != null) {
                return new Compte(login, motDePasse);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static String lireLoginDepuisFichier() {
        try (BufferedReader br = new BufferedReader(new FileReader("login.dat"))) {
            String ligne;
            String login = null;

            while ((ligne = br.readLine()) != null) {
                if (ligne.startsWith("Login : ")) {
                    login = ligne.substring("Login : ".length()).trim();
                    return login; // Retourne le login dès qu'il est trouvé
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Si le login n'est pas trouvé
    }
}
