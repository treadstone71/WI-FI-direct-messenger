package prashanth.wifimessenger;


import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Parcel;
import android.os.Parcelable;

public class MyParcel implements Parcelable{
    int isOwner;
    String msg;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(isOwner);
        dest.writeString(msg);
    }

    public static final Parcelable.Creator<MyParcel> CREATOR
            = new Parcelable.Creator<MyParcel>() {
        public MyParcel createFromParcel(Parcel in) {
            return new MyParcel(in);
        }

        public MyParcel[] newArray(int size) {
            return new MyParcel[size];
        }
    };

    public MyParcel(int i,String msg){
        isOwner = i;
        this.msg = msg;
    }

    public MyParcel(Parcel parcel){

        this.isOwner = parcel.readInt();
        this.msg = parcel.readString();
    }
}
