package prashanth.wifimessenger;


import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import android.os.Handler;
import android.util.Log;

public class OwnerToClient {

    String owner;
    boolean isOwner,transfer = false, transferNext = false;
    ChatActivity chatActivity;
    Handler handler;
    ServerSocket serverSocket;
    Socket s;

    public OwnerToClient(String owner, boolean isOwner, ChatActivity chatActivity, Handler handler){
        this.owner = owner;
        this.isOwner = isOwner;
        this.chatActivity = chatActivity;
        this.handler = handler;
    }

    public void work(){
        if(isOwner)
        (new Thread() {
            public void run() {
                try {
                    serverSocket = new ServerSocket(7777);
                    Socket client = serverSocket.accept();
                    while (true){
                        if(transfer){
                            Log.d("ChatActivity :","transfering from owner");
                            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                            out.println(chatActivity.msg);
                            Log.d("ChatActivity :", "transferred from owner");
                            transferNext = true;
                            transfer = false;
                        }
                    }
                } catch (Exception e) {
                    Log.d("ChatActivity :","In server Exception: "+e.getMessage());
                }finally {
                    try {
                        serverSocket.close();
                    }catch (Exception e){}
                }
            }
        }).start();
        else
            (new Thread(){
                public void run(){
                    try {
                        sleep(2000);
                        s = new Socket(InetAddress.getByName(owner), 7777);
                        while (true){
                                Log.d("ChatActivity :","receiving from owner");
                                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                String ans = in.readLine();
                                Log.d("ChatActivity :","received from owner "+ans);
                                Bundle msgBundle = new Bundle();
                                msgBundle.putString("result", ans);
                                Message msg = new Message();
                                msg.setData(msgBundle);
                                handler.sendMessage(msg);
                                Log.d("ChatActivity :", "sent for handling");
                                transferNext = false;
                        }
                    }catch (Exception e){
                        Log.d("ChatActivity :","In client Exception :"+e.getMessage());
                    }
                    finally {
                        try {
                            s.close();
                        }catch (Exception e){}
                    }
                }
            }).start();
    }
}
