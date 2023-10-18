package Modele;

import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataSource;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.swing.JOptionPane;

public class MonEnvoi {
    public void envoyerEmail(String destinataire, String cc, String cci, String sujet, String corpsMessage, List<PieceJointe> piecesJointes) {
        Compte compte = Compte.chargerDepuisFichier(); // Créez une instance de Compte en chargeant depuis le fichier
        if (compte == null) {
            System.out.println("Impossible de charger les informations du compte depuis le fichier.");
            JOptionPane.showMessageDialog(null, "Impossible de charger les informations du compte depuis le fichier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final String username = compte.getNomUtilisateur();
        final String password = compte.getMotDePasse();
        System.out.println(username + " --- " + password);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(username));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            emailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            emailMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(cci));
            emailMessage.setSubject(sujet);

            // Création du corps du message
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(corpsMessage);

            // Création des pièces jointes
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (piecesJointes != null) {
                for (PieceJointe piece : piecesJointes) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(piece.getFichier());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(piece.getNomFichier());
                    multipart.addBodyPart(messageBodyPart);
                }
            }

            // Définir le contenu du message comme le corps multipart
            emailMessage.setContent(multipart);

            // Envoyer le message
            Transport.send(emailMessage);

            System.out.println("Courrier électronique envoyé avec succès.");
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi du courrier électronique : " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Votre envoi n'a pas abouti, \n Ceci est probablement du a une mauvaise piece-jointe ou a une erreur d'adresse..", "Erreur", JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();
        }
    }
}
