//package com.example.myapplication;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
//public class chatPage extends AppCompatActivity implements MessageListener{
//    private EditText chatMessageInput;
//    private ServerTask serverTask;
//    private ImageButton sendButton;
//    private RecyclerView chatRecyclerView;
//    private ChatAdapter chatAdapter;
//    private Intent intent;
//    TextView groupName;
//    private ArrayList<ChatMessage> chatMessages;  // Use ChatMessage instead of String
////    private Networkservice networkService;  // Add a NetworkService object
//    private Handler uihandler;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_chat_page);
//
//        chatMessageInput = findViewById(R.id.chat_message_input);
//        sendButton = findViewById(R.id.message_send_btn);
//        chatRecyclerView = findViewById(R.id.chat_recycler_view);
//
//        intent=getIntent();
//        String grpName = intent.getStringExtra(groupFragment.GROUP_NAME);
//
////        uihandler = new Handler();
//
//        ((MyApplication) getApplication()).setMessageListener(this);
//
//
//        // Initialize the chat messages list and adapter
//        chatMessages = new ArrayList<>();  // Initialize as ArrayList<ChatMessage>
//        chatAdapter = new ChatAdapter(chatMessages);  // Use ChatAdapter with ChatMessage
//        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        chatRecyclerView.setAdapter(chatAdapter);
//
//        // Set onClickListener for send button
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String messageText = chatMessageInput.getText().toString().trim();
//                if (!messageText.isEmpty()) {
//                    long currentTime = System.currentTimeMillis(); // Get current time
//                    ChatMessage message = new ChatMessage(messageText, currentTime,true,grpName); // Create new ChatMessage
//                    ChatMessage sentMessage = new ChatMessage(messageText,currentTime,false,grpName);
//                    chatMessages.add(message); // Add the message to the list
//                    chatAdapter.notifyItemInserted(chatMessages.size() - 1); // Notify adapter
//                    chatRecyclerView.scrollToPosition(chatMessages.size() - 1); // Scroll to the last message
//                    chatMessageInput.setText(""); // Clear the input field
//
//                //send message using Networkservice
//                //networkService.sendMessage(messageText);  // Trigger sendMessage in NetworkService
//                    Broadcast(sentMessage);
//                }
//            }
//        });
//
//        // Reference to the back button
//        ImageButton backButton = findViewById(R.id.back_btn);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish(); // Close the current activity and go back
//            }
//        });
//
//
//        groupName=findViewById(R.id.other_username);
//        groupName.setText(grpName);
//
//
//
//    }
//
//    private void Broadcast(ChatMessage message){
//
//
//        List<ClientTask> peers = ClientManager.getAll();
//
//        if(ClientManager.getServerTask() != null){
//            ClientManager.getServerTask().sendMessage(message);
//        }
//
//            if (peers.isEmpty()) Log.e("ChatPage", "No peers connected");
//
//
//            for (ClientTask peer : peers) {
//                peer.sendMessage(message);
//            }
//
//
//    }
//
//    @Override
//    public void onMessageReceived(final ChatMessage incomingMessage){
//        //This method is called when a new message is received
//        long currentTime1 = System.currentTimeMillis();
//        runOnUiThread(() ->{
//            Log.d("chatPage",incomingMessage.getGrpName());
//            Log.d("chatPage", incomingMessage.getUserId());
//
//            chatMessages.add(incomingMessage);
//            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
//            chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
//
//        });
//    }
//
//}






package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class chatPage extends AppCompatActivity implements MessageListener{
    private EditText chatMessageInput;
    private ServerTask serverTask;
    private ImageButton sendButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private Intent intent;
    TextView groupName;
    private String senderUserID;
    private ArrayList<ChatMessage> chatMessages;  // Use ChatMessage instead of String
    //    private Networkservice networkService;  // Add a NetworkService object
    private Handler uihandler;
    private String selectedPerson;

    //Database changes
    MyDatabaseHelper myDB2;
    ArrayList<String>id_array,group_array,sender_array,message_array;
    ArrayList<Long>time_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_page);

        chatMessageInput = findViewById(R.id.chat_message_input);
        sendButton = findViewById(R.id.message_send_btn);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);

        intent=getIntent();
        String grpName = intent.getStringExtra(groupFragment.GROUP_NAME);
        selectedPerson = intent.getStringExtra(personFragment.GROUP_NAME);
//        uihandler = new Handler();

        ((MyApplication) getApplication()).setMessageListener(this);



        myDB2=new MyDatabaseHelper(chatPage.this);
        id_array=new ArrayList<>();
        group_array=new ArrayList<>();
        sender_array=new ArrayList<>();
        message_array=new ArrayList<>();
        time_array=new ArrayList<>();





        // Initialize the chat messages list and adapter
        chatMessages = new ArrayList<>();  // Initialize as ArrayList<ChatMessage>
        chatAdapter = new ChatAdapter(chatMessages);  // Use ChatAdapter with ChatMessage
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);


        storeDataInArrays(grpName);
        if(!chatMessages.isEmpty())chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        if(!chatMessages.isEmpty())chatRecyclerView.scrollToPosition(chatMessages.size() - 1);



        // Set onClickListener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MyDatabaseHelper myDB=new MyDatabaseHelper(chatPage.this);
                Cursor curs=myDB.readUserIdFromDb();
                if(curs.getCount()==0){
                    Toast.makeText(chatPage.this, "No userId in DB", Toast.LENGTH_SHORT).show();
                }else{
                    curs.moveToFirst();
                    senderUserID=curs.getString(1);
                }


                String messageText = chatMessageInput.getText().toString().trim();
                long currentTime=0;
                if (!messageText.isEmpty()) {
                    currentTime = System.currentTimeMillis(); // Get current time
                    ChatMessage message = new ChatMessage(messageText, currentTime,true,grpName,senderUserID,null); // Create new ChatMessage
                    ChatMessage sentMessage = new ChatMessage(messageText,currentTime,false,grpName,senderUserID,null);
                    chatMessages.add(message); // Add the message to the list
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1); // Notify adapter
                    chatRecyclerView.scrollToPosition(chatMessages.size() - 1); // Scroll to the last message
                    chatMessageInput.setText(""); // Clear the input field

                    //send message using Networkservice
                    //networkService.sendMessage(messageText);  // Trigger sendMessage in NetworkService
                    if(selectedPerson!=null)sentMessage.setDestination(selectedPerson);
                    Broadcast(sentMessage);
                }

                myDB.addMessage(intent.getStringExtra(groupFragment.GROUP_NAME),senderUserID,messageText,currentTime);
            }
        });

        // Reference to the back button
        ImageButton backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity and go back
            }
        });


        groupName=findViewById(R.id.other_username);
        groupName.setText(grpName);



    }



    private void Broadcast(ChatMessage message){

        List<ClientTask> peers = ClientManager.getAll();

        if(ClientManager.getServerTask() != null){
            ClientManager.getServerTask().sendMessage(message);
        }

        if (peers.isEmpty()) Log.e("ChatPage", "No peers connected");


        for (ClientTask peer : peers) {
            peer.sendMessage(message);
        }


    }


    @Override
    public void onMessageReceived(final ChatMessage incomingMessage){
        //This method is called when a new message is received
        long currentTime1 = System.currentTimeMillis();

        if (!incomingMessage.getUserId().equals(senderUserID) ) {
            if (selectedPerson==null || incomingMessage.getDestination().equals(ClientManager.deviceName)) {
                runOnUiThread(() ->{
                    Log.d("chatPage",incomingMessage.getGrpName());
                    Log.d("chatPage", incomingMessage.getUserId());


                    MyDatabaseHelper myDB=new MyDatabaseHelper(chatPage.this);
                    myDB.addMessage(intent.getStringExtra(groupFragment.GROUP_NAME),incomingMessage.getUserId(),incomingMessage.getText(),incomingMessage.getTimestamp());




                    chatMessages.add(incomingMessage);
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    chatRecyclerView.scrollToPosition(chatMessages.size() - 1);

                });
            }
        }
    }


    void storeDataInArrays(String grpName){
        Intent intent=getIntent();
        Cursor cursor=myDB2.readAllDate();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No Messages...", Toast.LENGTH_SHORT).show();
        }else{

            MyDatabaseHelper myDB=new MyDatabaseHelper(chatPage.this);
            Cursor curs=myDB.readUserIdFromDb();
            String senderUserID="";
            if(curs.getCount()==0){
            }else{
                curs.moveToFirst();
                senderUserID=curs.getString(1);
            }




            while(cursor.moveToNext()) {
                String s1=cursor.getString(1).toString().trim();
                String s2=intent.getStringExtra(groupFragment.GROUP_NAME).toString().trim();
                if (s1.equals(s2)) {
//                    Toast.makeText(this, "Messages are there...", Toast.LENGTH_SHORT).show();
                    ChatMessage MSG;
                    if(cursor.getString(2).equals(senderUserID)){
                        MSG= new ChatMessage(cursor.getString(3), cursor.getLong(4),true,grpName, cursor.getString(2),null);
                    }
                    else{
                        MSG= new ChatMessage(cursor.getString(3), cursor.getLong(4),false,grpName, cursor.getString(2),null);
                    }
                    chatMessages.add(MSG);
                    id_array.add(cursor.getString(0));
                    group_array.add(cursor.getString(1));
                    sender_array.add(cursor.getString(2));
                    message_array.add(cursor.getString(3));
                    time_array.add(cursor.getLong(4));
                }
            }
        }
    }
}