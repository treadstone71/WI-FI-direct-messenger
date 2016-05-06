package prashanth.wifimessenger;


import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientThread implements Runnable{

    InetAddress addr ;
    int port;
    String msg;
    Socket s;

    public ClientThread(InetAddress addr, int port, String msg){
        this.addr = addr;
        this.port = port;
        this.msg = msg;
    }

    public void run(){
        try{
            Log.d("ChatActivity : ", "inside client");
            s = new Socket(addr, port);
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
            out.println(msg);
            s.close();
            Log.d("ChatActivity : ", "completed client");
        }catch (Exception e){
            Log.d("ChatActivity : ", "completed client exception" + e.getMessage());
        }
        finally {
            Log.d("ChatActivity : ", "completed client finally");
        }
    }
}
