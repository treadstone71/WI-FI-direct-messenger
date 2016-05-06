package prashanth.wifimessenger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import prashanth.wifimessenger.dummy.DummyContent;


public class ChatFragment extends ListFragment {

    private ArrayAdapter<String> mAdapter= null;
    private List<String> mMessageList = new ArrayList<String>();
    public static ChatActivity mActivity = null;


    public ChatFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.chat_frag, container, false);
        final EditText inputEditText = (EditText)contentView.findViewById(R.id.edit_input);
        final Button sendBtn = (Button)contentView.findViewById(R.id.btn_send);
        mAdapter = new ChatMessageAdapter(mActivity,R.layout.row_message, mMessageList);
        setListAdapter(mAdapter);
        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String string = inputEditText.getText().toString();
                appendChatMessage("ME : " + string);
                inputEditText.setText("");
                //inputEditText.clearFocus();
                mActivity.sendMessage(string);
            }
        });
        return contentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (ChatActivity)getActivity();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void appendChatMessage(String row) {
        mMessageList.add(row);
        getListView().smoothScrollToPosition(mMessageList.size()-1);
        mAdapter.notifyDataSetChanged();  // notify the attached observer and views to refresh.
        return;
    }

    final class ChatMessageAdapter extends ArrayAdapter<String>{

        private List<String> items;
        public ChatMessageAdapter(Context context,int textViewResourceId, List<String> objects){
            super(context, textViewResourceId, objects);
            items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_message, null);
            }
            TextView msgRow = (TextView)v.findViewById(R.id.message);
            msgRow.setText(this.getItem(position));
            return v;
        }
    }

    public interface test2{
        public boolean isGroupOwner();
        public void ctoast(boolean b,int port);
        public void sendMessage(String msg);
    }

}
