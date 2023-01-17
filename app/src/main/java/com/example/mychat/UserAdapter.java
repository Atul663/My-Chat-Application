package com.example.mychat;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter <UserAdapter.ViewHolder> {
    List<String> userList;
    String userName, lastMessage;
    Context mContext;

    String profilePictureUrl = null;

    Encryption encryption = new Encryption();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    public UserAdapter(List<String> userList, String userName, String lastMessage, Context mContext) {
        this.userList = userList;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card,parent,false);
//        System.out.println(viewType);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String id = userList.get(position);
        //System.out.println("id = " + id);


        reference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String otherName = snapshot.child("Username").getValue().toString();
                String otherNameId = id;
                try {
                    profilePictureUrl = snapshot.child("profilePictures").getValue().toString();
                }
                catch (Exception e)
                {

                }
                FirebaseDatabase.getInstance().getReference().child("Message")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(otherNameId).orderByChild("timeStamp")
                        .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChildren())
                                {
                                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                                    {
                                        try {
                                            holder.lastMessageUser.setText(encryption.decrypteMessage(snapshot1.child("message").getValue().toString()));
                                        } catch (UnsupportedEncodingException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                holder.textViewUser.setText(otherName);

                if(profilePictureUrl == null)
                {
                    holder.profilePictureUser.setImageResource(R.drawable.ic_launcher_foreground);
                }
                else
                {
                    Picasso.get().load(profilePictureUrl).into(holder.profilePictureUser);
                }

                holder.cardViewUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext,MyChatActivity.class);
//                        //intent.putExtra("UserName", userName);
                        intent.putExtra("OtherName",otherNameId);
                        intent.putExtra("profilePictureUrl", profilePictureUrl);
                        intent.putExtra("id",id);
                        mContext.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewUser, lastMessageUser;
        private ImageView profilePictureUser;
        private CardView cardViewUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUser = itemView.findViewById(R.id.textViewUser);
            lastMessageUser = itemView.findViewById(R.id.LastMessageUser);
            profilePictureUser = itemView.findViewById(R.id.profilePictureUserCard);
            cardViewUser = itemView.findViewById(R.id.cardViewUserCard);
        }
    }


}
