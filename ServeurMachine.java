import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServeurMachine {
    private static final int PORT = 1028;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Créer un nouveau thread pour chaque client
                new Thread(new GestionClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
        }
    }

    private static class GestionClient implements Runnable {
        private Socket clientSocket;

        public GestionClient(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                
                // Lire l'adresse IP du client pour le logging
                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                System.out.println("Nouvelle connexion de: " + clientAddress);

                // Envoyer l'heure locale au client
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String heureLocale = sdf.format(new Date());
                out.println("Serveur actif - Heure locale: " + heureLocale);

            } catch (IOException e) {
                System.err.println("Erreur avec le client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture du socket: " + e.getMessage());
                }
            }
        }
    }
}
