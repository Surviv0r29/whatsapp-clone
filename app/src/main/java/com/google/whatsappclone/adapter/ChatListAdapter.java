package com.google.whatsappclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.whatsappclone.Main.chats.ChatsActivity;
import com.google.whatsappclone.R;
import com.google.whatsappclone.model.ChatList;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {
    private List<ChatList> list;
    private Context context;

    public ChatListAdapter(List<ChatList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.layout_chat_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.Holder holder, int position) {

        ChatList chatList = list.get(position);
        holder.tvName.setText(chatList.getUserName());
        holder.tvDesc.setText(chatList.getDescription());
        holder.tvDate.setText(chatList.getDate());
        //holder.profile.
        Glide.with(context).load(chatList.getUrlProfile()).into(holder.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ChatsActivity.class)
                .putExtra("userID",chatList.getUserId())
                .putExtra("userName",chatList.getUserName())
                .putExtra("userProfile",chatList.getUrlProfile()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class Holder extends RecyclerView.ViewHolder {
        private TextView tvName,tvDesc,tvDate;
        private CircleImageView profile;
        public Holder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.date);
            tvDesc = itemView.findViewById(R.id.desc);
            tvName =itemView.findViewById(R.id.name);
            profile = itemView.findViewById(R.id.profile);

        }
    }
}
