package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private ArrayList<ChatMessage> chatMessages;

    public ChatAdapter(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public int getItemViewType(int position) {
        // Check if the message is sent or received
        ChatMessage message = chatMessages.get(position);
        if (message.isSent()) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_received, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        holder.chatMessageText.setText(message.getText());
        holder.chatSenderName.setText("~ "+message.getUserName());
        // Format the timestamp and display it
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(message.getTimestamp()));
        holder.chatMessageTime.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView chatMessageText;
        TextView chatMessageTime;
        TextView chatSenderName;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatMessageText = itemView.findViewById(R.id.chat_message_text);
            chatMessageTime = itemView.findViewById(R.id.chat_message_time);
            chatSenderName=itemView.findViewById(R.id.chat_sender_name);
        }
    }
}