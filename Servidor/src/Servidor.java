import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ServerSocket socketS = null;
        Socket socketC = null;
        PrintWriter enviar = null;
        BufferedReader rebre = null;

        if (args.length != 2) {
            System.out.println("Has d'entrar dos arguments, primer el port i despues la paraula clau");
            scan.close();
            return;
        }

        int port = Integer.parseInt(args[0]);
        String clau = args[1];
        System.out.println("Iniciant servidor en : " + port);

        try {//connexio i tal
            socketS = new ServerSocket(port);
            System.out.println("Servidor esperant al client");
            socketC = socketS.accept();
            System.out.println("Client connectat...OK");

            rebre = new BufferedReader(new InputStreamReader(socketC.getInputStream()));
            enviar = new PrintWriter(socketC.getOutputStream(), true);

            String missatgeC;
            String missatgeS;
            System.out.println("Servidor, escriu la paraula clau per tancar la connexio");
            while (true) {
                missatgeC = rebre.readLine();

                if (missatgeC == null) {
                    System.out.println("Client ha tancat la connexio");
                    return;
                }
                else if (missatgeC.startsWith("CLIENT_TANCA:")) {
                    String missatgeFinal = missatgeC.substring("SERVIDOR_TANCA:".length());
                    System.out.println("Client diu: " + missatgeFinal);
                    System.out.println("El client ha indicat que tanca la connexió.");
                    return;
                }

                System.out.println("Client diu: " + missatgeC);

                // Si el client envia la paraula clau dins del missatge, el servidor ho mostra i tanca
                if (conteParaulaPerEspais(missatgeC, clau)) {
                    System.out.println("Client ha enviat la paraula clau dins del missatge");
                    System.out.println("Tancant connexio...OK");
                    return;
                }
                missatgeS = scan.nextLine();

                // Si el servidor envia la paraula clau, es tanca
                if (conteParaulaPerEspais(missatgeS, clau)) {
                    System.out.println("Servidor ha enviat la seva paraula clau dins del missatge.");
                    enviar.println("SERVIDOR_TANCA:El servidor ha enviat la paraula clau dins del missatge");
                    System.out.println("Tancant connexió");
                    return;
                }

                // Si no es paraula clau, enviem normal
                enviar.println(missatgeS);
                System.out.println("Missatge enviat...OK");
            }
        } catch (Exception e) {
            System.out.println("Error al servidor " + e.getMessage());
        } finally {//Tancar recursos
            try {
                scan.close();
                if (rebre != null) rebre.close();
                if (enviar != null) enviar.close();
                if (socketC != null) socketC.close();
                if (socketS != null) socketS.close();
                System.out.println("Servidor tancat.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean conteParaulaPerEspais(String text, String clau) {
        if (text == null || clau == null) {
            return false;
        }
        String t = " " + text.trim() + " ";
        String c = " " + clau.trim() + " ";
        return t.toLowerCase().contains(c.toLowerCase());
    }
}