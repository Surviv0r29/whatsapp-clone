package com.google.whatsappclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.whatsappclone.Main.chats.ChatsActivity;
import com.google.whatsappclone.R;
import com.google.whatsappclone.model.user.User;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<User> list;
    private Context context;

    public ContactAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);

        holder.UserName.setText(user.getUserName());
        holder.Desc.setText(user.getBio());

        //Glide.with(context).load(user.getImageProfile()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             context.startActivity(new Intent(context, ChatsActivity.class).putExtra("userID",user.getUserId())
             .putExtra("userName",user.getUserName())
             .putExtra("userProfile",user.getImageProfile()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView UserName,Desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profilepic);
            Desc=itemView.findViewById(R.id.description);
            UserName =itemView.findViewById(R.id.username);
        }
    }
}
