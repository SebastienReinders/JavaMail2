package Modele;

import Modele.Compte;

import java.time.LocalDateTime;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.time.ZoneId;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.mail.internet.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;







public class Login {

    public int LoginVerif()     //Va compter le nombre de messages
    {
        Compte compte = Compte.chargerDepuisFichier();
        if (compte != null) {
            String username = compte.getNomUtilisateur();
            String password = compte.getMotDePasse();


            int messageCount = countMessages(username, password);


            System.out.println("Nombre de messages dans la boîte de réception : " + messageCount);



            return messageCount;
        } else {
            System.out.println("Impossible de charger les informations de connexion depuis le fichier.");
        }

        return 0; // Il n'a pas réussi.
    }

    private int countMessages(String username, String password) {
        int messageCount = 0;
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Store store = session.getStore("imaps");
            store.connect();


            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            messageCount = inbox.getMessageCount();

            inbox.close(false);
            store.close();
        } catch (MessagingException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        }

        return messageCount;
    }




    public List<MesMails> ChargerTousLesMails() {
        String texte = "";
        List<MesMails> mailsList = new ArrayList<>();
        Compte compte = Compte.chargerDepuisFichier();
        List<PieceJointe> pieceJointes = new ArrayList<>();

        if (compte != null) {
            String username = compte.getNomUtilisateur();
            String password = compte.getMotDePasse();

            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imaps.host", "imap.gmail.com");
            props.put("mail.imaps.port", "993");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Store store = session.getStore("imaps");
                store.connect();

                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                Message[] messages = inbox.getMessages();

                for (Message message : messages) {
                    String sujet = message.getSubject();
                    // Récupération de l'expéditeur
                    String expediteur = "";
                    Address[] expediteurs = message.getFrom();
                    if (expediteurs != null && expediteurs.length > 0) {
                        expediteur = expediteurs[0].toString();
                    }
                    System.out.println("Expéditeur : " + expediteur);


                    LocalDateTime date = message.getSentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    System.out.println("Date : " + date.toString());

                    Object content = message.getContent();

                    if (content instanceof Multipart) {
                        Multipart multipart = (Multipart) content;


                        for (int i = 0; i < multipart.getCount(); i++) {
                            BodyPart bodyPart = multipart.getBodyPart(i);

                           //On prend le texte du mail
                            if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {

                                texte = (String) bodyPart.getContent();
                                System.out.println("Contenu du message : " + texte);
                            }

                            else if (bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
                                // C'est une pièce jointe
                                String nomFichier = bodyPart.getFileName();
                                if (nomFichier != null && !nomFichier.isEmpty()) {

                                    File fichier = new File("piecesjointes/" + nomFichier);
                                    try (InputStream inputStream = bodyPart.getInputStream();
                                         FileOutputStream fileOutputStream = new FileOutputStream(fichier)) {

                                        byte[] buffer = new byte[8192]; //Il fallait bien qu'on choisisse une taille
                                        int bytesRead;

                                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                                            fileOutputStream.write(buffer, 0, bytesRead);
                                        }

                                        System.out.println("Pièce jointe enregistrée : " + nomFichier);

                                    } catch (IOException | MessagingException e) {
                                        System.out.println("Erreur lors de l'enregistrement de la pièce jointe : " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    } else if (content instanceof String) {
                        // Si le contenu est une chaîne simple, c'est probablement du texte
                        texte = (String) content;
                        System.out.println("Contenu du message : " + texte);
                    }

                    String contenu = " ";

                    List<PieceJointe> piecesJointes = new ArrayList<>();
                    String destinataire = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
                    String cc = InternetAddress.toString(message.getRecipients(Message.RecipientType.CC));
                    List<String> receivedHeaders = new ArrayList<>();

                    String[] receivedHeadersArray = message.getHeader("Received");
                    if (receivedHeadersArray != null) {
                        for (String receivedHeader : receivedHeadersArray) {
                            receivedHeaders.add(receivedHeader);
                        }
                    }

                    MesMails mail = new MesMails(sujet, texte, pieceJointes, destinataire, cc, expediteur, receivedHeaders, date);
                    mailsList.add(mail);
                }

                inbox.close(false);
                store.close();
            } catch (Exception e) {
                System.out.println("Erreur lors de la récupération des e-mails : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Impossible de charger les informations de connexion depuis le fichier.");
        }

        return mailsList;
    }





    private void sendEmail(String username, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // Port SMTP pour Gmail

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("sebastien.reinders@student.hepl.be"));
            message.setSubject("Sujet du message");
            message.setText("Contenu du message");

            Transport.send(message);

            System.out.println("Courrier électronique envoyé avec succès.");
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi du courrier électronique : " + e.getMessage());
            e.printStackTrace();
        }
    }

}