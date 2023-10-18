package Controllers;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.*;
import java.io.File;
import java.util.Collections;
import java.util.Vector;
import java.util.Comparator;


import Modele.*;
import GUI.MaFenetrePrincipale;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ControleurPrincipal implements ActionListener  {
    private MaFenetrePrincipale vue;
    private JList<MesMails> mailList;

    // Pour les pièces jointes
    private List<Modele.PieceJointe> piecesJointes = new ArrayList<>();
    private String nomFichier;
    private File fichier;
    public int NbrMailsCharges;
    public VerifNewThread verifNewThread;

    public List<MesMails> MaListe = new ArrayList<>();

    private JTextField expediteurTextField;



    public void setMaFenetrePrincipale(MaFenetrePrincipale vue) {
        this.vue = vue;
    }

    public void setMailList(JList<MesMails> mailList) {
        this.mailList = mailList;
    }

    // Fonction pour charger les e-mails au démarrage
    public List<MesMails> MailsDemarrage() {
        Login login = new Login();
        MaListe = login.ChargerTousLesMails();
        NbrMailsCharges = MaListe.size();

        VerifNewThread verifThread = new VerifNewThread(NbrMailsCharges);
        verifThread.setDaemon(true); // Définir en tant que daemon thread
        verifThread.start();


        //On fait le tri par date si non c'est dans le mauvais sens (ordre chronologique décroissant)
        Collections.sort(MaListe, new Comparator<MesMails>() {
            @Override
            public int compare(MesMails mail1, MesMails mail2) {
                return mail2.getDate().compareTo(mail1.getDate()); // Ordre inverse
            }
        });

        return MaListe;
    }



    public void informationsMail(String destinataire, String cc, String cci, String sujet, String message) {
//On les affiche en console (vrai que ca sert à rien mais ca passe le temps pendant le chargement
        System.out.println("Expéditeur : " );
        System.out.println("Destinataire : " + destinataire);
        System.out.println("CC : " + cc);
        System.out.println("CCI : " + cci);
        System.out.println("Sujet : " + sujet);
        System.out.println("Message : " + message);

    }






    //Méthode pour recuperer les objets des mails afin de les afficher dans la jcombobox de l onglet 3 "analyser mails"
    public List<String> getObjetsMails() {
        List<String> objets = new ArrayList<>();
        for (MesMails mail : MailsDemarrage()) {
            objets.add(mail.getObjet());
        }
        return objets;
    }




    public List<String> getReceivedHeadersForMail(MesMails selectedMail) {

        return selectedMail.getReceivedHeaders();
    }











    @Override
    public void actionPerformed(ActionEvent e) {
        // C'est ici que l'evenement se traite
        String command = e.getActionCommand();


        if(command == "Envoyer")
        {
            String destinataire = vue.destinataireTextField.getText();
            String cc = vue.ccTextField.getText();
            String cci = vue.cciTextField.getText();
            String sujet = vue.sujetTextField.getText();
            String message = vue.emailTextArea.getText();
          //On previent le controleur
            this.informationsMail(destinataire, cc, cci, sujet, message);

            Modele.MonEnvoi envoi = new Modele.MonEnvoi();
            this.informationsMail(destinataire, cc, cci, sujet, message);
            envoi.envoyerEmail(destinataire, cc, cci, sujet, message, piecesJointes);
        }

        if(command == "Pièce_jointe")
        {
            //ouvrir fenetre pour introduire données fichiers
            //insérer la pièce jointe dans la List créee plus haut
            JFileChooser fileChooser = new JFileChooser();
            int resultat = fileChooser.showOpenDialog(null); // Affiche la boîte de dialogue de sélection de fichier


            if (resultat == JFileChooser.APPROVE_OPTION)
            {
                fichier = fileChooser.getSelectedFile(); // Récupère le fichier sélectionné
                nomFichier = fichier.getName(); // Récupère le nom du fichier

                // Création de la piece jointe
                //Faudra pas oublier de vider d'une fois à l'autre
                PieceJointe nouvellePieceJointe = new PieceJointe(nomFichier, fichier);


                // Ajout à la liste
                piecesJointes.add(nouvellePieceJointe);
                System.out.println("Piece jointe " + nomFichier);
            }
            else
            {
                // L'utilisateur a annulé la sélection du fichier
                nomFichier = null;
                fichier = null;
            }



        }

        if ( command == "MAJ")
        {
            List<MesMails> updatedMails = MailsDemarrage();

            DefaultListModel<MesMails> model = new DefaultListModel<>();

            for (MesMails mail : updatedMails) {
                model.addElement(mail);
            }

            mailList.setModel(model);
            System.out.println("BLABLA ICI");
        }

    }





}
