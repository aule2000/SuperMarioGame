package mario.game.OtherWorldCreate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import mario.game.Worlds.MainCodeProgram;


public class GetFromAndroidApp implements  Runnable{

    private static ServerSocket ss;
    private static Socket s;
    private static InputStreamReader isr;
    private static BufferedReader br;
    static String message;
    static String parentStringValue;
    static  String messagegroup1;
    static String messageText;

    @Override
    public void run() {
        try {
            ss = new ServerSocket(5000);
            while(true){                  //for(;;)

                s = ss.accept();
                isr = new InputStreamReader(s.getInputStream());
                br = new BufferedReader(isr);
                message = br.readLine();
                System.out.println(message);

                MainCodeProgram.TextListener.setText(message);



                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            isr.close();
            br.close();
            ss.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static String getMessage(){
        return message;
    }

    public static void setMessage(String message){
        GetFromAndroidApp.message = message;
    }

}
