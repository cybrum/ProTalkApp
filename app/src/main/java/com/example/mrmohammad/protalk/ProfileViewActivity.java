package com.example.mrmohammad.protalk;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileViewActivity extends AppCompatActivity {

    Button sendFriendRequestBtn;
    Button declineFriendRequestBtn;
    TextView profileViewUserNameTV;
    TextView profileViewUserStatusTV;
    CircleImageView profileViewImage;
    DatabaseReference userReference;

    Toolbar appBar;

    String CURRENT_FRIEND_STATE = "";
    DatabaseReference friendRequestRef;
    FirebaseAuth mAuth;
    String sender_userId;
    String receiver_userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        appBar = findViewById(R.id.profile_view_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        mAuth = FirebaseAuth.getInstance();
        sender_userId = mAuth.getCurrentUser().getUid();



        userReference = FirebaseDatabase.getInstance().getReference().child("User");

        sendFriendRequestBtn = findViewById(R.id.send_friend_rqst_btn);
        declineFriendRequestBtn = findViewById(R.id.decline_rqst_btn);
        profileViewUserNameTV = findViewById(R.id.tv_profile_view_display_name);
        profileViewUserStatusTV = findViewById(R.id.tv_profile_view_status);
        profileViewImage = findViewById(R.id.profile_view_image);

        receiver_userId = getIntent().getExtras().getString("profile_view_userId");

        CURRENT_FRIEND_STATE = "not_friends";


        userReference.child(receiver_userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String img = dataSnapshot.child("img").getValue().toString();

                profileViewUserNameTV.setText(name);
                profileViewUserStatusTV.setText(status);
                Picasso.get().load(img).into(profileViewImage);
                getSupportActionBar().setTitle(name);

                friendRequestRef.child(sender_userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(receiver_userId)){
                            String req_type = dataSnapshot.child(receiver_userId).child("request_type").getValue().toString();

                            if(req_type.equals("sent")){
                                CURRENT_FRIEND_STATE = "request_sent";
                                sendFriendRequestBtn.setText("Cancel Friend Request");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendFriendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendFriendRequestBtn.setEnabled(false);

                if(CURRENT_FRIEND_STATE.equals("not_friends")) {

                    sendARequestToSomeone();
                }
                    if(CURRENT_FRIEND_STATE.equals("request_sent")){
                        cancelFriendRequest();
                    }


                }



        });

    }

    private void cancelFriendRequest() {

        friendRequestRef.child(sender_userId).child(receiver_userId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            friendRequestRef.child(receiver_userId).child(sender_userId).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                sendFriendRequestBtn.setEnabled(true);
                                                CURRENT_FRIEND_STATE = "not_friends";
                                                sendFriendRequestBtn.setText("Send Friend Request");
                                            }

                                        }
                                    });

                        }

                    }
                });

    }

    private void sendARequestToSomeone() {

        friendRequestRef.child(sender_userId).child(receiver_userId).child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        friendRequestRef.child(receiver_userId).child(sender_userId).child("request_type")
                                .setValue("received")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            sendFriendRequestBtn.setEnabled(true);
                                            CURRENT_FRIEND_STATE = "request_sent";
                                            sendFriendRequestBtn.setText("Cancel Friend Request");
                                        }
                                    }
                                });
                    }
                });




    }
}
