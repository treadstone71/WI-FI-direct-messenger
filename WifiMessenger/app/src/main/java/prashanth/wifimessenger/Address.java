package prashanth.wifimessenger;


import java.io.Serializable;
import java.net.InetAddress;

public class Address implements Serializable{
    InetAddress inetAddress;
    public Address(InetAddress inetAddress){
        this.inetAddress = inetAddress;
    }
}
