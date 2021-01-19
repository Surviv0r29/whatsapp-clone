package com.google.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.whatsappclone.Main.chats.ChatsActivity;
import com.google.whatsappclone.R;
import com.google.whatsappclone.model.chat.Chats;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chats> list;
    private Context context;
    private FirebaseUser firebaseUser;
    private static final int MSG_TYPE_LEFT =0;
    private static final int MSG_TYPE_RIGHT =1;

    public ChatAdapter(List<Chats> list, Context context) {
        this.list =list;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_layout_left, parent, false);
            return new ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_layout_right, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textmessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textmessage =itemView.findViewById(R.id.text_message);
        }
        void bind(Chats chats){
            textmessage.setText(chats.getTextMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(list.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
