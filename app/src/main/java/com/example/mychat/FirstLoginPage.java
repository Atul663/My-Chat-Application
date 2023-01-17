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

public class FirstLoginPage extends AppCompatActivity {


    CircleImageView profilePicture;
    TextView userName, description;
    Button submit;
    boolean imgControl = false;
    Uri imageUri;
    View firstLoginPagelayout;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login_page);
        database = FirebaseDatabase.getInstance();


        firstLoginPagelayout = findViewById(R.id.firstLoginPageLayout);
        profilePicture = findViewById(R.id.profilePicture);
        userName = findViewById(R.id.editTextTextUserName);
        description = findViewById(R.id.editTextTextDescription);
        submit = findViewById(R.id.buttonSubmit);

        firstLoginPagelayout.setVisibility(View.INVISIBLE);
        checkUserRegistration();

        reference = database.getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userName.getText() == null && description.getText() == null)
                {
                    Toast.makeText(FirstLoginPage.this, "Please enter username and Description", Toast.LENGTH_SHORT).show();
                }

                else if(userName.getText() == null)
                {
                    Toast.makeText(FirstLoginPage.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }

                else if(description.getText() == null)
                {
                    Toast.makeText(FirstLoginPage.this, "Please enter a description", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    reference.child("Users").child(auth.getUid()).child("Username").setValue(userName.getText().toString());
                    reference.child("Users").child(auth.getUid()).child("description").setValue(description.getText().toString());

                    if(imgControl == true)
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
                                                Toast.makeText(FirstLoginPage.this, "Success", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(FirstLoginPage.this, "Fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FirstLoginPage.this, "this fail", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    else
                    {
                        reference.child("Users").child(auth.getUid()).child("profilePictures").setValue("null");
                    }

                    Intent intent = new Intent(FirstLoginPage.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        });
    }

    public void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profilePicture);
            imgControl = true;
        }
        else
        {
            imgControl = false;
        }
    }

    protected void checkUserRegistration() {
        DatabaseReference checkUserRef = database.getReference();

        checkUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userNameReg = null;
                try {
                    userNameReg = snapshot.child("Users").child(auth.getUid()).child("Username").getValue().toString();
                    if(userNameReg != null)
                    {
                        Intent i = new Intent(FirstLoginPage.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
                catch (Exception e)
                {
                    firstLoginPagelayout.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}