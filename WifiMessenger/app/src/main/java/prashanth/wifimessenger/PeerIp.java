package prashanth.wifimessenger;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class PeerIp {

    String owner;
    String peerIp;
    boolean done = false, isOwner;

    public PeerIp(String owner, boolean isOwner){
        this.owner = owner;
        this.isOwner = isOwner;
    }

    public void completeTask(){
        if(isOwner)
        (new Thread(){
            public void run(){
                try {
                    ServerSocket serverSocket = new ServerSocket(8888);
                    Socket client = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    peerIp = in.readLine();
                    serverSocket.close();
                    done = true;
                }catch (Exception e){}
            }
        }).start();
        else
        (new Thread(){
            public void run(){
                try{
                    sleep(2000);
                    Socket s = new Socket(InetAddress.getByName(owner),8888);
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
                    out.println(getLocalIpAddress());
                }catch (Exception e){}
            }
        }).start();
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

}
