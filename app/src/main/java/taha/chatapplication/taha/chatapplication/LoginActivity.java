package taha.chatapplication.taha.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


public class LoginActivity extends AppCompatActivity
{


    EditText username ;
    EditText password ;

    String strusername ;
    String strpassword;


    private ProgressDialog mprProgressDialog ;

    private FirebaseAuth firebaseAuth ;
    private String firebaseUser;
    private DatabaseReference users;

    private ProgressDialog mProgressBar;


    private String token ;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inti();
    }

    void inti ()
    {
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
        mProgressBar = new ProgressDialog(this);

        users = FirebaseDatabase.getInstance().getReference().child("Users");


    }
    public void Login(View view)
    {
        strusername = username.getText().toString();
        strpassword = password.getText().toString();
        mProgressBar.setTitle("Register User");
        mProgressBar.setMessage("please wait while creating your account ");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();
        if(!TextUtils.isEmpty(strpassword)||!TextUtils.isEmpty(strusername))
        {
            loginUser(strusername , strpassword);
        }
    }

    private void loginUser(String strusername, String strpassword)
    {
        firebaseAuth.signInWithEmailAndPassword(strusername , strpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    token = FirebaseInstanceId.getInstance().getToken();
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    users.child(firebaseUser).child("device_token").setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            startActivity(new Intent(getApplicationContext() , MainActivity.class ));

                        }
                    });

                }
                else
                {
                    mProgressBar.hide();
                    Toast.makeText(getApplicationContext() , "Can't sign in , tray again " , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
