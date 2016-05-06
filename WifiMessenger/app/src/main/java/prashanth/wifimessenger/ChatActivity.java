package prashanth.wifimessenger;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import android.os.Handler;

public class ChatActivity extends AppCompatActivity implements ChatFragment.test2 {

    boolean isGOwner;
    String ownerAddr, msg;
    ChatFragment chatFragment;
    Handler handler;
    String clientIp;
    OwnerToClient otc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toast.makeText(ChatActivity.this, "before parcel", Toast.LENGTH_SHORT).show();
        Intent i = getIntent();
        MyParcel p = (MyParcel) i.getParcelableExtra("groupinfoparcel");
        Toast.makeText(ChatActivity.this, "after parcel", Toast.LENGTH_SHORT).show();
        chatFragment = (ChatFragment)getFragmentManager().findFragmentById(R.id.chat_fragment);
        if(p.isOwner == 1) isGOwner = true;
        else isGOwner = false;
        ownerAddr = p.msg;
        handler = new HandlerExtension(this);
        if(isGOwner){
            ServerThread serverThread = new ServerThread(2018,handler);
            new Thread(serverThread).start();
        }
        else{
            ServerThread serverThread = new ServerThread(2019,handler);
            new Thread(serverThread).start();
        }
        otc = new OwnerToClient(ownerAddr,isGOwner,this, handler);
        otc.work();
        PeerIp ip = new PeerIp(ownerAddr, isGOwner);
        ip.completeTask();
        while (isGOwner && true){
            if(ip.done){
                clientIp = ip.peerIp;
                break;
            }
        }
    }

    private static class HandlerExtension extends Handler {
        private final WeakReference<ChatActivity> currentActivity;

        public HandlerExtension(ChatActivity activity){
            currentActivity = new WeakReference<ChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message message){
            ChatActivity activity = currentActivity.get();
            if (activity!= null){
                activity.update(message.getData().getString("result"));
            }
        }

    }

    public void sendMessage(String msg){
        if(isGOwner){
           this.msg = msg;
           otc.transfer = true;
        }
        else{
            try {
                Log.d("ChatActivity : ", "before starting client");
                ClientThread clientThread = new ClientThread(InetAddress.getByName(ownerAddr), 2018, msg);
                new Thread(clientThread).start();
            }catch (Exception e){}
        }
    }

    public void update(String s){
        chatFragment.appendChatMessage(s);
    }

    public boolean isGroupOwner(){
        return isGOwner;
    }

    public void ctoast(boolean b,int port){
        if(b)
            Toast.makeText(ChatActivity.this, "StartedinOwner"+port, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(ChatActivity.this, "StartedinClient"+port, Toast.LENGTH_SHORT).show();
    }

    public void ct2(String s){
        Toast.makeText(ChatActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
