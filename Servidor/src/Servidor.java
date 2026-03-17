import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) {
        int port = 5000;//Es crear el port
        try {
            ServerSocket servidor = new ServerSocket(port);// Es crear el port a partir del server pocket
            System.out.println("Fent temps a que un client es conecti al servidor " + port );

            while (true) { //el bucle per fer un multiclient i que accepti clients sense parar
                //Accepta el client amb el socket
                Socket connexioClient = servidor.accept();
                System.out.println("Client connectat: " + connexioClient.getInetAddress());

                Clients c = new Clients(connexioClient); //Crea el fil amb el client per gestionar-lo
                c.start();//executa el fil
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//Representa el fil que gestiona a cada client
class Clients extends Thread {
    private Socket connexioClient;

    //Reb el socket i el guarda
    public Clients(Socket connexioClient) {
        this.connexioClient = connexioClient;
    }

    @Override
    public void run() {
        try {
            //Lector per llegir el text que envia el client
            BufferedReader lectorEntrada = new BufferedReader(
                    new InputStreamReader(connexioClient.getInputStream()));
            PrintWriter escriptorSortida = new PrintWriter(connexioClient.getOutputStream(), true);
            //Crear la un escriptor per llegir el missatge del client

            //llegeix la linea queenvia el client
            String missatge = lectorEntrada.readLine();
            //envia la resposta al client
            System.out.println("Client " + connexioClient.getInetAddress() + " diu: " + missatge);
            escriptorSortida.println("Hola des del servidor! (IP: " + connexioClient.getInetAddress() + ")");

            //tanca la conexio amb el client
            connexioClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

