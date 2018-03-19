package taha.chatapplication.taha.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{


    EditText display;
    EditText mail;
    EditText password;
    EditText cPassword;


    String str_display;
    String str_mail;
    String str_password;
    String str_cPassword;

    Toolbar toolbar ;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;



    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inti();
    }

    private void inti()
    {
        display = (EditText) findViewById(R.id.displayName);
        mail = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        cPassword = (EditText) findViewById(R.id.cpassword);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("register");
        mProgressBar = new ProgressDialog(this);
    }


    public void Register(View view)
    {
        str_display = display.getText().toString();
        str_mail = mail.getText().toString();
        str_password = password.getText().toString();
        str_cPassword = cPassword.getText().toString();

        if(!TextUtils.isEmpty(str_display)||!TextUtils.isEmpty(str_mail)||!TextUtils.isEmpty(str_password))
        {
            mProgressBar.setTitle("Register User");
            mProgressBar.setMessage("please wait while creating your account ");
            mProgressBar.setCanceledOnTouchOutside(false);
            mProgressBar.show();

            register_user(str_mail , str_password,str_display);



        }
    }



    private void register_user(String mail , String password , final String str_display)
    {
        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {

                if(task.isSuccessful())
                {
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uId = current_user.getUid();

                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);

                    HashMap<String , String> user = new HashMap<>();
                    user.put("name",str_display);
                    user.put("status","Hi there , am using schoola chat");
                    user.put("image","default");
                    user.put("thumb_image","default");

                    mDatabaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                mProgressBar.dismiss();
                                startActivity(new Intent(getApplicationContext() , MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        }
                    });
                }
                else
                {
                    mProgressBar.hide();
                    Toast.makeText(getApplicationContext() , "Can't register in , tray again " , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
