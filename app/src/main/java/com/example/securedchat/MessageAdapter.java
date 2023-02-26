package com.example.securedchat;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.securedchat.Models.Message;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        public interface MessageListener {
            void decryptMessage(Message message, int position) throws Exception;
        }

        private Activity activity;
        private ArrayList<Message> messages;
        private MessageListener listener;



        public MessageAdapter(Activity activity, ArrayList<Message> messages){
            this.activity = activity;
            this.messages = messages;
        }

        public void setListener(MessageListener listener) {
            this.listener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
            MessageHolder messageHolder = new MessageHolder(view);
            return messageHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            final MessageHolder holder = (MessageHolder) viewHolder;
            Message message = getItem(position);

            holder.sender_name.setText(message.getSenderName()+ " :");
            holder.message.setText(message.getDescription());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }
        public Message getItem(int position) {
            return messages.get(position);
        }


        class MessageHolder extends RecyclerView.ViewHolder {
            private MaterialTextView sender_name;
            private MaterialTextView message;
            private MaterialButton decrypt;

            public MessageHolder(View itemView) {
                super(itemView);
                sender_name = itemView.findViewById(R.id.sender_name);
                message = itemView.findViewById(R.id.message);
                decrypt = itemView.findViewById(R.id.decrypt);


                decrypt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            try {
                                listener.decryptMessage(getItem(getAdapterPosition()), getAdapterPosition());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
            }
        }
    }

