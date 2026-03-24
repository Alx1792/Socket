import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) {
        Scanner scan= new Scanner(System.in);
        ServerSocket socketS=null;
        Socket socketC=null;
        PrintWriter enviar=null;
        BufferedReader rebre=null;
        if(args.length !=2){
            System.out.println("Has d'entrar dos arguments, primer el port i despues la paraula clau");
            scan.close();
            return;
        }
        int port =Integer.parseInt(args[0]);
        String clau=args[1];
        System.out.println("Iniciant servidor en : "+port);
        try{
            socketS =new ServerSocket(port);
            System.out.println("Servidor esperant al client");
            socketC = socketS.accept();
            System.out.println("Client connectat...OK");

            rebre=new BufferedReader(new InputStreamReader(socketC.getInputStream()));
            enviar =new PrintWriter(socketC.getOutputStream(),true);

            String missatgeC;
            String missatgeS;
            while(true){
                missatgeC=rebre.readLine();
                if(missatgeC==null){
                    System.out.println("Client ha tancat la connexio");
                    return;
                }

                if("CLIENT_TANCA".equals(missatgeC)){
                    System.out.println("El client ha indicat que tanca la connexió.");
                    return;
                }

                System.out.println("Client diu: "+missatgeC);
                if (conteParaulaPerEspais(missatgeC, clau)) {
                    System.out.println("Client ha enviat la paraula clau dins del missatge");
                    System.out.println("Tancant connexio...OK");
                    return;
                }

                System.out.println("Servidor, escriu la paraula clau per tancar la connexio");
                missatgeS= scan.nextLine();

                // sempre enviem la frase
                enviar.println(missatgeS);

                if (conteParaulaPerEspais(missatgeS, clau)) {
                    System.out.println("Servidor ha enviat la seva paraula clau dins del missatge.");
                    System.out.println("Tancant connexió ");
                    // AVÍS explícit perquè el client mostri el missatge de tancament
                    enviar.println("SERVIDOR_TANCA");
                    return;
                }

            }
        }catch (Exception e){
            System.out.println("Error al servidor "+e.getMessage());
        }finally {
            try{
                scan.close();
                if(socketC != null) socketC.close();
                if(socketS != null) socketS.close();
                System.out.println("Servidor tancat.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    private static boolean conteParaulaPerEspais(String text, String clau) {
        if (text == null || clau == null){
            return false;
        }
        String t = " " + text.trim() + " ";
        String c = " " + clau.trim() + " ";

        return t.toLowerCase().contains(c.toLowerCase());
    }

}
