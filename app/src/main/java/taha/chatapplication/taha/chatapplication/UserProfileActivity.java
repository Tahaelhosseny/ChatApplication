package taha.chatapplication.taha.chatapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class UserProfileActivity extends Activity
{

    DatabaseReference databaseReference ;
    DatabaseReference mRequestFreindRef;
    DatabaseReference mFriendDataBase;
    DatabaseReference mNotificationDataBase;
    FirebaseAuth mFirebaseAuth;



    String userID;

    String current;

    String mCurrnt_state;


    ImageView imageView ;
    TextView displayName;
    TextView status ;
    TextView matual ;
    TextView allFrinds;
    Button btnSendRequest;
    Button mDispLine ;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userID = getIntent().getStringExtra("name");
        Toast.makeText(getApplicationContext(),userID,Toast.LENGTH_SHORT).show();


        inti();
    }



    public void inti ()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        mRequestFreindRef = FirebaseDatabase.getInstance().getReference().child("Friend_REQ");
        mFriendDataBase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDataBase = FirebaseDatabase.getInstance().getReference().child("notifications");





        current = FirebaseAuth.getInstance().getCurrentUser().getUid();


        imageView = (ImageView) findViewById(R.id.imageView);
        displayName = (TextView) findViewById(R.id.displayName);
        status = (TextView) findViewById(R.id.status);
        matual = (TextView) findViewById(R.id.matual);
        allFrinds = (TextView) findViewById(R.id.totalFriends);
        btnSendRequest = (Button) findViewById(R.id.button4);
        mDispLine = (Button) findViewById(R.id.disPline);



        mCurrnt_state = "not_Friends" ;


        mRequestFreindRef.child(current).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild(userID))
                {
                    String req_type = dataSnapshot.child(userID).child("request_type").getValue().toString();

                    if(req_type.equals("received"))
                    {
                         mCurrnt_state = "req_received" ;
                         btnSendRequest.setText("Accept Friend Request");
                        mDispLine.setVisibility(View.VISIBLE);
                        mDispLine.setEnabled(true);

                    }
                    else if (req_type.equals("sent"))
                    {
                        mCurrnt_state = "request_send" ;
                        btnSendRequest.setText("Cancel Friend Request");

                        mDispLine.setVisibility(View.INVISIBLE);
                        mDispLine.setEnabled(false);

                    }


                }
                else
                {
                     mFriendDataBase.child(current).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot)
                         {
                             if(dataSnapshot.hasChild(userID))
                             {
                                 btnSendRequest.setText("UnFriend this Person");
                                 mCurrnt_state="Friends";
                             }
                         }

                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });



        getData();


    }



    private void getData()
    {
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String dis = dataSnapshot.child("name").getValue().toString();
                String statuss = dataSnapshot.child("status").getValue().toString();
                String img = dataSnapshot.child("image").getValue().toString();
                displayName.setText(dis);
                status.setText(statuss);
                Picasso.with(getApplicationContext()).load(img).placeholder(R.drawable.defaultperson).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }



    public void SendRequest(View view)
    {
        btnSendRequest.setEnabled(false);




        //-------------------not_Friends_state----------

        if(mCurrnt_state.equals("not_Friends"))
        {
            mRequestFreindRef.child(current).child(userID).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        mRequestFreindRef.child(userID).child(current).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                    {

                                        HashMap<String,String> hashMap = new HashMap<>();
                                        hashMap.put("from",current);
                                        hashMap.put("type","request");
                                        hashMap.put("date",DateFormat.getDateTimeInstance().format(new Date()));

                                        mNotificationDataBase.child(userID).push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>()
                                        {
                                            @Override
                                            public void onSuccess(Void aVoid)
                                            {

                                                btnSendRequest.setText("Cancel Request");
                                                mCurrnt_state="request_send";
                                                Toast.makeText(getApplicationContext() , "Request sent successfully",Toast.LENGTH_SHORT).show();
                                                mDispLine.setVisibility(View.INVISIBLE);
                                                mDispLine.setEnabled(false);

                                            }
                                        });



                                    }
                                else
                                    {
                                        Toast.makeText(getApplicationContext() , "failed sending request",Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });

                        }

                }
            });

            btnSendRequest.setEnabled(true);

        }


        //-------------------request_send----------

        if(mCurrnt_state.equals("request_send"))
        {
            mRequestFreindRef.child(current).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    mRequestFreindRef.child(userID).child(current).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            btnSendRequest.setEnabled(true);
                            btnSendRequest.setText("Send friend Request");
                            mCurrnt_state="not_Friends";
                            mDispLine.setVisibility(View.INVISIBLE);
                            mDispLine.setEnabled(false);
                        }
                    });

                }
            });
        }


        //-------------------request_rec_state----------

        if(mCurrnt_state.equals("req_received"))
        {
            final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
            mFriendDataBase.child(current).child(userID).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid)
                {
                    mFriendDataBase.child(userID).child(current).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            mRequestFreindRef.child(current).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    mRequestFreindRef.child(userID).child(current).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            btnSendRequest.setEnabled(true);
                                            btnSendRequest.setText("UnFriend this Person");
                                            mCurrnt_state="Friends";
                                            mDispLine.setVisibility(View.INVISIBLE);
                                            mDispLine.setEnabled(false);
                                        }
                                    });

                                }
                            });

                        }
                    });

                }
            });

        }



    }
}
