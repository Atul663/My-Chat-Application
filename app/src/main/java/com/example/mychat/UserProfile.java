package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    CircleImageView profilePicture;
    TextView userName, description;

    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profilePicture = findViewById(R.id.profilePictureUserProfile);
        userName = findViewById(R.id.textViewUserNameUserProfile);
        description = findViewById(R.id.textViewDescriptionUserProfile);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        String id = getIntent().getStringExtra("id");

        reference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userName.setText(snapshot.child("Username").getValue().toString());
                        description.setText(snapshot.child("description").getValue().toString());
                        String profilePictue = snapshot.child("profilePictures").getValue().toString();

                        if(profilePicture.equals("null"))
                        {
                            profilePicture.setImageResource(R.drawable.ic_launcher_foreground);
                        }
                        else
                        {
                            Picasso.get().load(profilePictue).into(profilePicture);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}