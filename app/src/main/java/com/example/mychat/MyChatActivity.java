package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyChatActivity extends AppCompatActivity {

    ImageView imageViewBack;
    CircleImageView sendMessage, profilePictureChatActivity;
    EditText enterMessage;
    TextView UserNameChatActivity;
    RecyclerView recyclerViewChat;

    String userName, otherName, profilePictureUser, id, name;

    FirebaseDatabase database;
    DatabaseReference reference;

    ModelClass modelclass ;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    //FirebaseUser user = auth.getCurrentUser();
    Encryption encryption = new Encryption();

    MessageAdapter adapter;
    List <ModelClass> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);

        imageViewBack = findViewById(R.id.imageViewBack);
        enterMessage = findViewById(R.id.editTextEnterMessage);
        UserNameChatActivity = findViewById(R.id.textViewUserNameChatActivity);
        profilePictureChatActivity = findViewById(R.id.profilePictureChatActivity);
        sendMessage = findViewById(R.id.sendMessage);
        recyclerViewChat = findViewById(R.id.recyclerViewChatScreen);

        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        modelclass = new ModelClass();

        id = getIntent().getStringExtra("id");
        otherName = getIntent().getStringExtra("OtherName");
        profilePictureUser = getIntent().getStringExtra("profilePictureUrl");
        reference.child("Users").child(otherName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               name = snapshot.child("Username").getValue().toString();
               UserNameChatActivity.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userName = auth.getUid();

        if(profilePictureUser.equals(""))
        {
            profilePictureChatActivity.setImageResource(R.drawable.ic_launcher_foreground);
        }
        else
        {
            Picasso.get().load(profilePictureUser).into(profilePictureChatActivity);
        }

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = enterMessage.getText().toString();
                if(!message.equals("")) {
                    sendMessageFun(message);
                    enterMessage.setText("");
                }
            }
        });

        profilePictureChatActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyChatActivity.this, UserProfile.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        UserNameChatActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyChatActivity.this, UserProfile.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        getMessage();

    }

    public void getMessage() {
        reference.child("Message").child(userName).child(otherName)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                modelclass = snapshot.getValue(ModelClass.class);
                try {
                    modelclass.setMessage(encryption.decrypteMessage(modelclass.getMessage()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                list.add(modelclass);
                modelclass.setMessageId(snapshot.getKey());
                adapter.notifyDataSetChanged();
                recyclerViewChat.scrollToPosition(list.size()-1);

            }
                    @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new MessageAdapter(list, userName, otherName);
        recyclerViewChat.setAdapter(adapter);
    }

    public void sendMessageFun(String message) {
        final String key = reference.child("Message").child(userName).child(otherName).push().getKey();
        final Map<String, Object> messageMap = new HashMap<>();
        Long timeStamp = new Date().getTime();

        messageMap.put("message", encryption.encrypteMessage(message));
        messageMap.put("from", userName);
        messageMap.put("timeStamp",timeStamp);
//        System.out.println("hii");
        reference.child("Message").child(userName).child(otherName).child(key).setValue(messageMap)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        reference.child("Message").child(otherName).child(userName).child(key).setValue(messageMap);
                    }

                });
    }
}