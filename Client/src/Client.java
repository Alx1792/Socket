import java.io.*; // Els buffered, els input i tal, entrada i sortida basicament
import java.net.*; // Socket i aquestes coses de xarxes
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Socket socket = null; // Per a despues
        PrintWriter enviar = null;
        BufferedReader rebre = null;


        if (args.length != 2) {
            System.out.println("Has d'entrar dos arguments, primer el port i despues la paraula clau");
            scan.close();
            return;
        }

        try { // Comprovar si hi ha 2 arguments

            // Entrar arguments per terminal
            int port = Integer.parseInt(args[0]);
            String clau = args[1];
            System.out.println("Iniciant connexio amb el servidor...OK");

            // Connexio
            socket = new Socket("127.0.0.1", port);
            System.out.println("Connectat al servidor...OK");

            // Serveix per enviar missatges al servidor, el true fa que s'envii automaticament i no faci falta el flush
            enviar = new PrintWriter(socket.getOutputStream(), true);

            // getInputStream: rep els bytes que envia el servidor
            // InputStreamReader: fa de conversor i converteix aquests bytes en lletra
            // BufferedReader: llegeix les linees
            rebre = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connexio preparada");

            String xat;
            String resposta;
            System.out.println("Si vols tancar la connexió, escriu la paraula clau");

            while (true) {

                xat = scan.nextLine();
                enviar.println(xat);
                System.out.println("Missatge enviat...OK");
                boolean clientTanca = conteParaulaPerEspais(xat, clau);

                resposta = rebre.readLine();

                if (resposta == null) {
                    if(clientTanca){
                        System.out.println("Has enviat la paraula clau. Tancant client...OK");
                    }
                    System.out.println("El servidor ha tancat la connexió.");
                    return;
                }

                // Si el servidor envia el missatge especial de tancament
                // el client el mostra i tanca
                if (resposta.startsWith("SERVIDOR_TANCA:")) {
                    String missatgeFinal = resposta.substring("SERVIDOR_TANCA:".length());
                    System.out.println("Servidor: " + missatgeFinal);
                    if(clientTanca){
                        System.out.println("Has enviat la paraula clau. Tancant client...OK");
                    }
                    System.out.println("El servidor ha indicat que tanca la connexió.");
                    return;
                }

                System.out.println("Servidor: " + resposta);

                // Si el client ha enviat la paraula clau, despres de rebre la resposta/avis, tanca
                if(clientTanca){
                    System.out.println("Has enviat la paraula clau. Tancant client...OK");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                scan.close();
                if (rebre != null) rebre.close();
                if (enviar != null) enviar.close();
                if (socket != null) socket.close();
                System.out.println("Tot tancat");
            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
        }
    }

    // Aquesta funcio mira si la paraula clau esta dins del text separada per espais
    private static boolean conteParaulaPerEspais(String text, String clau) {
        if (text == null || clau == null) {
            return false;
        }
        String t = " " + text.trim() + " ";
        String c = " " + clau.trim() + " ";
        return t.toLowerCase().contains(c.toLowerCase());
    }
}