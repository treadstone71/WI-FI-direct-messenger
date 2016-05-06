package prashanth.wifimessenger;


import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import android.os.Handler;

public class ServerThread implements Runnable{

    private int port;
    public String sol = "";
    ServerSocket serverSocket;
    Handler handler;

    public ServerThread(int port,Handler ca){
        this.port = port;
        this.handler = ca;
    }

    public void run(){
        try {
            while (true) {
                serverSocket = new ServerSocket(port);
                Log.d("ChatActivity : ", "inside server thread waiting");
                Socket client = serverSocket.accept();
                Log.d("ChatActivity : ","accepted a client");
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                Log.d("ChatActivity : ","took into reader");
                sol = in.readLine();
                Bundle msgBundle = new Bundle();
                msgBundle.putString("result", sol);
                Message msg = new Message();
                msg.setData(msgBundle);
                handler.sendMessage(msg);
                Log.d("ChatActivity : ", "end with" + sol);
                try {
                    serverSocket.close();
                }catch (Exception e){}
            }
        }catch (Exception e){
            Log.d("ChatActivity : ","in server exception"+e.getMessage());
        }
        finally {
            Log.d("ChatActivity : ","in server finally");
            try {
                serverSocket.close();
            }catch (Exception e){}
        }
    }


}
