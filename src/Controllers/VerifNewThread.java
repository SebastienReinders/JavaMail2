package Controllers;

import Modele.Login;

import javax.swing.*;

public class VerifNewThread extends Thread {
    private int NbrMailsCharges;
    private int NewMessages;
    private boolean TuContinue;

    public VerifNewThread(int NbrMailsCharges) {
        this.NbrMailsCharges = NbrMailsCharges;
    }

    public void MAJNbrMailsCharges(int NbrMailsCharges) {
        this.NbrMailsCharges = NbrMailsCharges;
    }

    @Override
    public void run() {
        TuContinue = true;
        while (TuContinue) {
            try {




                    // Créer l'objet Login
                    Login login = new Login();

                    NewMessages = login.LoginVerif();
                    if (NbrMailsCharges < NewMessages) {
                        // Calculer le nombre de nouveaux messages
                        int nouveauxMessages = NewMessages - NbrMailsCharges;
                        TuContinue = false;
                        // Afficher la boîte de dialogue
                        String message = "Vous avez " + nouveauxMessages + " nouveaux messages. Mettez à jour votre boîte de réception.";
                        JOptionPane.showMessageDialog(null, message, "Nouveaux Messages", JOptionPane.INFORMATION_MESSAGE);

                    }
                Thread.sleep(10000);

                }

            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
