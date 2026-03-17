import java.io.*; //Els buffered els input i tal, entrada i sortida basicament
import java.net.*; //Socket i aquestes cose sde xarxes

public class Client {
    public static void main(String[] args) {
        try {
            //Conecta al servidor
            Socket connexio = new Socket("127.0.0.1", 5000);
            System.out.println("Connectat al servidor");

            //Escriptor per enviar el text al servidor
            PrintWriter escriptor = new PrintWriter(
                    connexio.getOutputStream(), true);

            escriptor.println("Hola patatilles");
            System.out.println("Missatge enviat: 'Me encante que te encanti entrar al servidor'");

            //Lector per rebre el missatge del servidor
            BufferedReader lector = new BufferedReader(new InputStreamReader(connexio.getInputStream()));

            //Llegeix la respostadel servidor
            String resposta = lector.readLine();
            System.out.println("Servidor respon: '" + resposta + "'");

            //Es tanque per a no gastar mes recursos, es podria deixar obert pero seria inutil
            connexio.close();
            System.out.println("Connexió tancada.");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
