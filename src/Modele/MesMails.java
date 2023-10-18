package Modele;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MesMails {
    private String sujet;

    private LocalDateTime date;
    private String expediteur;

    private String contenu;
    private List<PieceJointe> piecesJointes = null;
    private String destinataire;
    private String cc;

    private List<String> receivedHeaders; // Champ pour stocker les en-têtes "Received"

    public MesMails(String sujet, String contenu, List<PieceJointe> piecesJointes, String destinataire, String cc, String expediteur, List<String> receivedHeaders, LocalDateTime date)
    {
        this.sujet = sujet;
        this.contenu = contenu;
        this.piecesJointes = piecesJointes;
        this.destinataire = destinataire;
        this.cc = cc;
        this.receivedHeaders = receivedHeaders;
        this.expediteur = expediteur;
        this.date = date;

    }

    // Getters et setters pour les nouveaux champs

    public List<String> getReceivedHeaders() {
        return receivedHeaders;
    }

    public void setReceivedHeaders(List<String> receivedHeaders) {
        this.receivedHeaders = receivedHeaders;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public List<PieceJointe> getPiecesJointes() {
        return piecesJointes;
    }

    public void setPiecesJointes(List<PieceJointe> piecesJointes) {
        this.piecesJointes = piecesJointes;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }
    public String getObjet(){return sujet;}

    public LocalDateTime getDate() {
        return date;
    }



    public String toString() {
        return expediteur + "\n" + sujet;
    }
}