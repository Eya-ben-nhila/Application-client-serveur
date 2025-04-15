import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientMachine {
    private static final int PORT = 1027;
    private static final int TIMEOUT = 5000; // 5 secondes de timeout

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Entrez l'adresse IP de la machine à vérifier: ");
        String ipAddress = scanner.nextLine().trim();
        
        try (Socket socket = new Socket()) {
            // Configurer le timeout de connexion
            socket.connect(new InetSocketAddress(ipAddress, PORT), TIMEOUT);
            
            // Configurer le timeout de lecture
            socket.setSoTimeout(TIMEOUT);
            
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                
                // Envoyer une requête vide (le serveur attend juste une connexion)
                out.println("PING");
                
                // Lire la réponse du serveur
                String response = in.readLine();
                System.out.println("La machine est active. Réponse du serveur: " + response);
                
            } catch (SocketTimeoutException e) {
                System.out.println("La machine est active mais n'a pas répondu dans le délai imparti.");
            }
        } catch (ConnectException e) {
            System.out.println("La machine n'est pas active ou le service n'est pas disponible.");
        } catch (IOException e) {
            System.out.println("Erreur de communication: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
