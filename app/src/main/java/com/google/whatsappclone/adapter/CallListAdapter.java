package com.google.whatsappclone.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.whatsappclone.R;
import com.google.whatsappclone.model.Calllist;
import com.google.whatsappclone.model.ChatList;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.Holder>{
    private List<Calllist> list;
    private Context context;

    public CallListAdapter(List<Calllist> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CallListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CallListAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.layout_calllist, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CallListAdapter.Holder holder, int position) {

        Calllist calllist = list.get(position);
        try{
            holder.tvName.setText(calllist.getUserName());
        }catch (Exception e){
            Toast.makeText(context, "e", Toast.LENGTH_SHORT).show();
        }
        holder.tvName.setText(calllist.getUserName());
        if(calllist.getCalltype().equals("recieved")){
            holder.calltype.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_downward_24));
            holder.calltype.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));

        }else if(calllist.getCalltype().equals("outgone"))
        {
            holder.calltype.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_call_made_24));
        }else{
            holder.calltype.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_call_missed_24));
            holder.calltype.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        holder.tvDate.setText(calllist.getDate());
        //holder.profile.
        Glide.with(context).load(calllist.getUrlProfile()).into(holder.profile);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public static class Holder extends RecyclerView.ViewHolder {
        private TextView tvName,tvDate;
        private CircleImageView profile;
        private ImageView calltype;
        public Holder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.calldate);
            calltype = itemView.findViewById(R.id.calltype);
            tvName =itemView.findViewById(R.id.callername);
            profile = itemView.findViewById(R.id.profilepic);

        }
    }
}
