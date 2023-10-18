package Modele;

import java.io.File;

public class PieceJointe {
    private String nomFichier;
    private File fichier;

    public PieceJointe(String nomFichier, File fichier) {
        this.nomFichier = nomFichier;
        this.fichier = fichier;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public File getFichier() {
        return fichier;
    }

    @Override
    public String toString() {
        return nomFichier;
    }
}
