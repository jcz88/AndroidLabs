package com.example.jian0080.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import static com.example.jian0080.androidlabs.ChatDatabaseHelper.KEY_MESSAGE;
import static com.example.jian0080.androidlabs.ChatDatabaseHelper.TABLE_NAME;
import static com.example.jian0080.androidlabs.R.id.btn_send;
import static com.example.jian0080.androidlabs.R.id.list_view;
import static com.example.jian0080.androidlabs.R.id.text_message;
import static com.example.jian0080.androidlabs.R.layout.chat_row_incoming;
import static com.example.jian0080.androidlabs.R.layout.chat_row_outgoing;

public class ChatWindowActivity extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    EditText textMsg;
    ArrayList<String> chatMsg;
    LayoutInflater inflater;
    SQLiteDatabase writableDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        final ChatDatabaseHelper myDbHelper = new ChatDatabaseHelper(ChatWindowActivity.this);
        final ChatAdapter messageAdapter;
        ListView listView;

        final ContentValues cValues = new ContentValues();

        textMsg = findViewById(text_message);
        Button sendBtn = findViewById(btn_send);
        inflater = ChatWindowActivity.this.getLayoutInflater();
        chatMsg = new ArrayList<>();
        messageAdapter = new ChatAdapter(this);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatMsg.add(textMsg.getText().toString());
                cValues.put(KEY_MESSAGE, textMsg.getText().toString());
                writableDb.insert(TABLE_NAME, "NullColumn", cValues);
                messageAdapter.notifyDataSetChanged();
                textMsg.setText("");
            }
        });

        listView = findViewById(list_view);

        listView.setAdapter(messageAdapter);

        writableDb = myDbHelper.getWritableDatabase();
        Cursor c = writableDb.query(false, TABLE_NAME, new String[] {KEY_MESSAGE}, null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            chatMsg.add(c.getString(c.getColumnIndex(KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + c.getString(c.getColumnIndex(KEY_MESSAGE)));
            c.moveToNext();
        }
        Log.i(ACTIVITY_NAME,"Cursor's column count="+c.getColumnCount());
        for (int i=0; i<c.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME, "Cursor's column name="+c.getColumnName(0));
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        writableDb.close();
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx)
        {
            super(ctx, 0);
        }
        public int getCount()
        {
            return chatMsg.size();
        }
        public String getItem(int position)
        {
            return chatMsg.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            View result;
            if (position%2 == 0) {
                result = inflater.inflate(chat_row_incoming, null);
            } else {
                result = inflater.inflate(chat_row_outgoing, null);
            }
            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
        public long getId(int position) {
            return position;
        }
    }
}
