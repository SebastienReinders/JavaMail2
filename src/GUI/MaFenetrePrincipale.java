package GUI;

import Controllers.*;

import Modele.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;


import Controllers.ControleurPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaFenetrePrincipale extends JFrame {
    private ControleurPrincipal controller;
    public JButton boutonEnvoyer; // Bouton "Envoyer"

    public JTextField dateMailTextField;
    public JTextField destinataireTextField;
    public JTextField destinataireTextField2;
    public JTextField ccTextField;
    public JTextField CCTextField2;
    public JTextField cciTextField;
    public JTextField sujetTextField;
    public JTextField objetTextField;
    public JTextArea emailTextArea;
    public JTextArea contenuTextArea;
    public JLabel piecejointe;

    public JButton piecesJointesButton;
    public JLabel piecesJointesLabel;

    public JTextField expediteurTextField;

    public int monMailEnCour;

    public JTree infoTree;
    private JComboBox<String> choixComboBox = new JComboBox<>();

    public JButton miseAJourButton;


    public MaFenetrePrincipale(ControleurPrincipal controller) {


        super("Application de Messagerie");

        this.controller = controller;
        this.controller.setMaFenetrePrincipale(this);
        piecejointe = new JLabel();



        JTabbedPane onglets = new JTabbedPane();

// Onglet "Envoyer Mail"
        JPanel envoyerMailPanel = new JPanel(new GridBagLayout());
        boutonEnvoyer = new JButton("Envoyer");
        boutonEnvoyer.setActionCommand("Envoyer");
        boutonEnvoyer.addActionListener(controller);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        envoyerMailPanel.add(boutonEnvoyer, gbc); // Bouton "Envoyer" en haut

        gbc.gridx = 0; // Pour que le label commence en colonne 0
        gbc.gridy = 1; // Pour que le label commence une ligne plus bas
        gbc.gridwidth = 2; // Pour que le label occupe 2 cellules en largeur
        gbc.gridheight = 1; // Pour que le label occupe 1 cellule en hauteur
        gbc.fill = GridBagConstraints.HORIZONTAL;
        piecesJointesLabel = new JLabel(" ");
        envoyerMailPanel.add(piecesJointesLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 2;
        gbc.gridx = 0;
        // Champ "Destinataire" (texte statique)
        JLabel destinataireLabel = new JLabel("Destinataire : ");
        envoyerMailPanel.add(destinataireLabel, gbc);

        gbc.gridx = 1;
        // Champ "Destinataire" (champ de texte)
        destinataireTextField = new JTextField(10);
        envoyerMailPanel.add(destinataireTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        envoyerMailPanel.add(new JLabel("CC :"), gbc);

        gbc.gridx = 1;
        ccTextField = new JTextField(10);
        envoyerMailPanel.add(ccTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        envoyerMailPanel.add(new JLabel("CCI :"), gbc);

        gbc.gridx = 1;
        cciTextField = new JTextField(10);
        envoyerMailPanel.add(cciTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        envoyerMailPanel.add(new JLabel("Sujet :"), gbc);

        gbc.gridx = 1;
        sujetTextField = new JTextField(10);
        envoyerMailPanel.add(sujetTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        envoyerMailPanel.add(new JLabel("Message :"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        emailTextArea = new JTextArea(10, 50);
        JScrollPane scrollPane = new JScrollPane(emailTextArea);
        envoyerMailPanel.add(scrollPane, gbc);

        gbc.gridx = 0; // Pour que le label commence en colonne 0
        gbc.gridy = 9; // Pour que le label commence une ligne plus bas
        gbc.gridwidth = 2; // Pour que le label occupe 2 cellules en largeur
        gbc.gridheight = 1; // Pour que le label occupe 1 cellule en hauteur
        gbc.fill = GridBagConstraints.HORIZONTAL;
        piecesJointesLabel = new JLabel(" ");
        envoyerMailPanel.add(piecesJointesLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Bouton "Pièces jointes"
        piecesJointesButton = new JButton("Pièces jointes");
        piecesJointesButton.setActionCommand("Pièce_jointe");
        piecesJointesButton.addActionListener(controller);
        envoyerMailPanel.add(piecesJointesButton, gbc);

        // Label pour afficher les noms des pièces jointes
        gbc.gridx = 0; // Pour que le label commence en colonne 0
        gbc.gridy = 12; // Pour que le label commence une ligne plus bas
        gbc.gridwidth = 2; // Pour que le label occupe 2 cellules en largeur
        gbc.gridheight = 1; // Pour que le label occupe 1 cellule en hauteur
        gbc.fill = GridBagConstraints.HORIZONTAL;
        piecesJointesLabel = new JLabel(" ");
        envoyerMailPanel.add(piecesJointesLabel, gbc);

        onglets.addTab("Envoyer Mail", envoyerMailPanel);




// Onglet "Recevoir Mail"
        JPanel recevoirMailPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();


        // Colonne de gauche : Liste des mails et label "Boîte de réception des mails"
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.gridheight = 2;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;
        gbc2.fill = GridBagConstraints.BOTH;

        // JPanel pour contenir le label et la liste des mails
        JPanel leftColumnPanel = new JPanel(new BorderLayout());

        // Label "Boîte de réception des mails"
        miseAJourButton = new JButton("MISE A JOUR");
        miseAJourButton.setActionCommand("MAJ");
        miseAJourButton.addActionListener(controller);
        leftColumnPanel.add(miseAJourButton, BorderLayout.NORTH);

        // Liste des mails (Exemple de liste de mails)
        //JList<String> mailList = new JList<>(new String[]{"Mail 1", "Mail 2", "Mail 3"});
        DefaultListModel<MesMails> mailListModel = new DefaultListModel<>();
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();

        for (MesMails mail : controller.MailsDemarrage()) {
            mailListModel.addElement(mail);
            comboBoxModel.addElement(mail.getObjet());
        }

        choixComboBox.setModel(comboBoxModel);
        JList<MesMails> mailList = new JList<>(mailListModel);

        mailList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    MesMails selectedMail = mailList.getSelectedValue();
                    if (selectedMail != null) {
                        expediteurTextField.setText(selectedMail.getExpediteur());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                        String formattedDate = selectedMail.getDate().format(formatter);
                        dateMailTextField.setText(formattedDate);
                        destinataireTextField2.setText(selectedMail.getDestinataire());
                        CCTextField2.setText((selectedMail.getCc()));
                        objetTextField.setText(selectedMail.getSujet());
                        contenuTextArea.setText(selectedMail.getContenu());
                        if(selectedMail.getPiecesJointes() != null) {
                            piecejointe.setText("OUI");
                        } else {
                            piecejointe.setText("NON");
                        }

                    }
                }
            }
        });


        controller.setMailList(mailList);
        mailList.setFixedCellWidth(200);



        mailList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                MesMails mail = (MesMails) value;
                setText(mail.getExpediteur() + " - " + mail.getObjet());
                return this;
            }
        });


        JScrollPane mailListScrollPane = new JScrollPane(mailList);
        mailListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mailListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        leftColumnPanel.add(mailListScrollPane, BorderLayout.CENTER);

        recevoirMailPanel.add(leftColumnPanel, gbc2);

        // Colonne de droite : Affichage du mail complet
        gbc2.gridx = 1;
        gbc2.gridheight = 1;
        gbc2.weightx = 2.0;
        gbc2.weighty = 1.0;

        // JPanel pour afficher les détails du mail
        JPanel mailDetailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0;
        gbc3.gridy = 0;
        gbc3.anchor = GridBagConstraints.WEST;

        // Label "Date"
        mailDetailsPanel.add(new JLabel("Date :"), gbc3);
        gbc3.gridx = 1;
        //JTextField dateTextField = new JTextField(20);
        dateMailTextField = new JTextField(20);
        dateMailTextField.setEditable(false); // Pour l'affichage uniquement
        mailDetailsPanel.add(dateMailTextField, gbc3);

        //gbc3.gridx = 2;
        //mailDetailsPanel.add(new JLabel("Pièce(s) jointe(s) :"), gbc3);

        gbc3.gridx = 0;
        gbc3.gridy = 1;
        mailDetailsPanel.add(new JLabel("Expéditeur :"), gbc3);
        gbc3.gridx = 1;
        expediteurTextField = new JTextField(20);
        expediteurTextField.setEditable(false); // Pour l'affichage uniquement
        mailDetailsPanel.add(expediteurTextField, gbc3);


        gbc3.gridx = 0;
        gbc3.gridy = 2;
        mailDetailsPanel.add(new JLabel("Destinataire :"), gbc3);
        gbc3.gridx = 1;
        destinataireTextField2 = new JTextField(20);
        destinataireTextField2.setEditable(false); // Pour l'affichage uniquement
        mailDetailsPanel.add(destinataireTextField2, gbc3);


        gbc3.gridx = 0;
        gbc3.gridy = 3;
        mailDetailsPanel.add(new JLabel("CC :"), gbc3);
        gbc3.gridx = 1;
        CCTextField2 = new JTextField(20);
        CCTextField2.setEditable(false); // Pour l'affichage uniquement
        mailDetailsPanel.add(CCTextField2, gbc3);


        gbc3.gridx = 0;
        gbc3.gridy = 4;
        mailDetailsPanel.add(new JLabel("Objet :"), gbc3);
        gbc3.gridx = 1;
        objetTextField = new JTextField(20);
        objetTextField.setEditable(false); // Pour l'affichage uniquement
        mailDetailsPanel.add(objetTextField, gbc3);



        gbc3.gridx = 0;
        gbc3.gridy = 5;
        //piecejointe.setText("Pièce(s) jointe(s) : " + piecejointe);
        mailDetailsPanel.add(piecejointe, gbc3);



        gbc3.gridx = 0;
        gbc3.gridy = 8;
        gbc3.gridwidth = 2;
        gbc3.weighty = 1.0;
        gbc3.fill = GridBagConstraints.BOTH;
        contenuTextArea = new JTextArea(20, 50);
        contenuTextArea.setEditable(false); // Pour l'affichage uniquement
        JScrollPane contenuScrollPane = new JScrollPane(contenuTextArea);
        mailDetailsPanel.add(contenuScrollPane, gbc3);
        // Créez un JTextArea pour les pièces jointes
        JTextArea textArea2 = new JTextArea(20, 50);
        textArea2.setEditable(false); // Pour l'affichage uniquement
        JScrollPane textArea2ScrollPane = new JScrollPane(textArea2);





        recevoirMailPanel.add(mailDetailsPanel, gbc2);

        onglets.addTab("Recevoir Mail", recevoirMailPanel);


        // Onglet "Analyser Mail"
// Onglet "Analyser Mail"
        JPanel analyserMailPanel = new JPanel();
        analyserMailPanel.setLayout(new BorderLayout());

// Liste de choix centrée


        String[] objets = controller.getObjetsMails().toArray(new String[0]);
        JComboBox<String> choixComboBox = new JComboBox<>(objets);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(choixComboBox);

        choixComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // recup  l'indice de l'élément sélectionné
                    int selectedIndex = choixComboBox.getSelectedIndex();

                    // utilisation de ca  pour accéder à l'élément correspondant dans la liste
                    MesMails selectedMail = mailList.getModel().getElementAt(selectedIndex);

                    // A getReceivedHeaders() de la classe MesMails
                    List<String> receivedHeaders = selectedMail.getReceivedHeaders();

                    // MAJ de la JTree avec les headers
                    updateHeadersInTree(receivedHeaders);
                }
            }
        });



        analyserMailPanel.add(centerPanel, BorderLayout.NORTH);




// Ajoutons une JTree en dessous
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Racine");
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        infoTree = new JTree(treeModel);
        infoTree.setRootVisible(true);

        JScrollPane treeScrollPane = new JScrollPane(infoTree);
        analyserMailPanel.add(treeScrollPane, BorderLayout.CENTER);

        onglets.addTab("Analyser Mail", analyserMailPanel);




        onglets.addTab("Analyser Mail", analyserMailPanel);



        getContentPane().add(onglets);

        // Config fenetre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void updateHeadersInTree(List<String> headers) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Racine");

        if (headers != null) {
            for (String header : headers) {
                DefaultMutableTreeNode headerNode = new DefaultMutableTreeNode(header);
                rootNode.add(headerNode);
            }
        } else {
            System.out.println("Headers is null");
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        infoTree.setModel(treeModel);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControleurPrincipal controller = new ControleurPrincipal();
            MaFenetrePrincipale fenetre = new MaFenetrePrincipale(controller);
            controller.setMaFenetrePrincipale(fenetre);


        });
    }

}