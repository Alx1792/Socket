import java.io.*; //Els buffered els input i tal, entrada i sortida basicament
import java.net.*; //Socket i aquestes cose sde xarxes
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scan= new Scanner(System.in);
        Socket socket=null;//Per a despues
        PrintWriter enviar=null;
        BufferedReader rebre=null;
        if(args.length !=2){
            System.out.println("Has d'entrar dos arguments, primer el port i despues la paraula clau");
            scan.close();
            return;
        }

        try{//Comprovar si hi ha 2 arguments

            //Entrar arguments per terminal
            int port= Integer.parseInt(args[0]);
            String clau=args[1];
            System.out.println("Iniciant connexio amb el servidor...OK");
            //Connexio
            socket= new Socket("127.0.0.1",port);
            System.out.println("Connectat al servidor...OK");
            //Serveix per enviar missatges al servidor, el true fa que s'envii automaticament i no faci falta el flush
            enviar=new PrintWriter(socket.getOutputStream(),true);
            //getInputStream: rep els bytes que envia el servidor
            //InputStreamReader: fa de conversor i converteix aquests bytes en lletra
            //BufferedR :Llegeix les linees
            rebre=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connexio preprarada");

            String xat;
            String resposta;
            System.out.println("Si vols tancar la connexió, escriu la paraula clau");
            while(true){

                xat= scan.nextLine();
                enviar.println(xat);
                System.out.println("Missatge enviat...OK");

                // Si el client ha usat la paraula clau, avisa i tanca
                if (conteParaulaPerEspais(xat, clau)) {
                    enviar.println("CLIENT_TANCA");
                    System.out.println("Tancant client...OK");
                    return;
                }

                resposta= rebre.readLine();
                if(resposta==null){
                    System.out.println("El servidor ha tancat la connexió.");
                    return;
                }

                if("SERVIDOR_TANCA".equals(resposta)){
                    System.out.println("El servidor ha indicat que tanca la connexió.");
                    return;
                }

                System.out.println("Servidor: " + resposta);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                scan.close();
                if(socket != null) socket.close();
                System.out.println("Tot tancat");
            }catch (Exception e){
                System.out.println("Error "+e.getMessage());
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
