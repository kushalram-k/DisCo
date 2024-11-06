package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private ArrayList<ChatMessage> chatMessages;  // Use ChatMessage instead of String
//    private Networkservice networkService;  // Add a NetworkService object
    private Handler uihandler;
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

//        uihandler = new Handler();

        ((MyApplication) getApplication()).setMessageListener(this);


        // Initialize the chat messages list and adapter
        chatMessages = new ArrayList<>();  // Initialize as ArrayList<ChatMessage>
        chatAdapter = new ChatAdapter(chatMessages);  // Use ChatAdapter with ChatMessage
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Set onClickListener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = chatMessageInput.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    long currentTime = System.currentTimeMillis(); // Get current time
                    ChatMessage message = new ChatMessage(messageText, currentTime,true,grpName); // Create new ChatMessage
                    ChatMessage sentMessage = new ChatMessage(messageText,currentTime,false,grpName);
                    chatMessages.add(message); // Add the message to the list
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1); // Notify adapter
                    chatRecyclerView.scrollToPosition(chatMessages.size() - 1); // Scroll to the last message
                    chatMessageInput.setText(""); // Clear the input field

                //send message using Networkservice
                //networkService.sendMessage(messageText);  // Trigger sendMessage in NetworkService
                    Broadcast(sentMessage);
                }
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
        }else {

            if (peers.isEmpty()) Log.e("ChatPage", "No peers connected");


            for (ClientTask peer : peers) {
                peer.sendMessage(message);
            }

        }
    }

    @Override
    public void onMessageReceived(final ChatMessage incomingMessage){
        //This method is called when a new message is received
        long currentTime1 = System.currentTimeMillis();
        runOnUiThread(() ->{
            Log.d("chatPage",incomingMessage.getGrpName());

            chatMessages.add(incomingMessage);
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            chatRecyclerView.scrollToPosition(chatMessages.size() - 1);

        });
    }

}