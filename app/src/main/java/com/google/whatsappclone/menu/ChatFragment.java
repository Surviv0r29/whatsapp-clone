package com.google.whatsappclone.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.whatsappclone.R;
import com.google.whatsappclone.adapter.ChatAdapter;
import com.google.whatsappclone.adapter.ChatListAdapter;
import com.google.whatsappclone.databinding.FragmentChatBinding;
import com.google.whatsappclone.model.ChatList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private FragmentChatBinding binding;
    private List<ChatList> list = new ArrayList<>();

    private Handler handler = new Handler();

    private ArrayList<String> allUserID = new ArrayList<>();

    private ChatListAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }


    private RecyclerView recyclerView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat, container, false);



        //getChatlist();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        adapter=new ChatListAdapter(list,getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);


        if(firebaseUser!=null){
            getChatlist();
        }

        return binding.getRoot();
    }

    private void getChatlist(){
        list.clear();
        allUserID.clear();
        database.child("ChatList").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 :snapshot.getChildren()){
                    String userId = Objects.requireNonNull(snapshot1.child("chatid").getValue()).toString();
                    allUserID.add(userId);

                }
                getUserData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (String userID : allUserID){
                    firestore.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("Exception", "onSuccess: ddd"+documentSnapshot.getString("userName"));
                            try {
                                ChatList chat = new ChatList(
                                        documentSnapshot.getString("userId"),
                                        documentSnapshot.getString("userName"),
                                        "tbd",
                                        "",
                                        ""
                                );
                                list.add(chat);
                            }catch (Exception e){
                                Log.d("except", "onSuccess: "+e.getMessage());
                            }
                            if (adapter!=null){
                                adapter.notifyItemInserted(0);
                                adapter.notifyDataSetChanged();

                                Log.d(TAG, "onSuccess: adapter "+adapter.getItemCount());
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Error L"+e.getMessage());
                        }
                    });
                }
            }
        });

    }

 }