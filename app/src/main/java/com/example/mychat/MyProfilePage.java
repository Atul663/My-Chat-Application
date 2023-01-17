package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfilePage extends AppCompatActivity {

    CircleImageView profileProfilePicture;
    TextView profileUserName, profileDescription;
    Button profileSubmit;
    boolean imgControl = false;
    Uri imageUri;

    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profileProfilePicture = findViewById(R.id.profileProfilePicture);
        profileUserName = findViewById(R.id.editTextTextProfileUserName);
        profileDescription = findViewById(R.id.editTextTextProfileDescription);
        profileSubmit = findViewById(R.id.buttonProfileSubmit);

        getUserInfo();

        profileProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        profileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    public void updateProfile()
    {
        String userName = profileUserName.getText().toString();
        String description = profileDescription.getText().toString();
        reference.child("Users").child(auth.getUid()).child("Username").setValue(userName);
        reference.child("Users").child(auth.getUid()).child("description").setValue(description);

        if(imgControl)
        {
            UUID randomId = UUID.randomUUID();
            String imgName = "images/" + randomId + ".jpg";
            storageReference.child(imgName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference myStorageRef = storage.getReference(imgName);
                    myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String filePath = uri.toString();
                            reference.child("Users").child(auth.getUid()).child("profilePictures").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MyProfilePage.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MyProfilePage.this, "Fail", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyProfilePage.this, "this fail", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void getUserInfo()
    {
        reference.child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String UserName = snapshot.child("Username").getValue().toString();
                String Description = snapshot.child("description").getValue().toString();
                String profilePictue = snapshot.child("profilePictures").getValue().toString();

                profileUserName.setText(UserName);
                profileDescription.setText(Description);
                if(profilePictue.equals("null"))
                {
                    profileProfilePicture.setImageResource(R.drawable.ic_launcher_foreground);
                }
                else
                {
                    Picasso.get().load(profilePictue).into(profileProfilePicture);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profileProfilePicture);
            imgControl = true;
        }
        else
        {
            imgControl = false;
        }
    }

}