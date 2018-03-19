package taha.chatapplication.taha.chatapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);
    }

    public void ubdateStatus(View view)
    {

        EditText editText = (EditText) findViewById(R.id.status);
        String str = editText.getEditableText().toString();
        String currentuId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuId).child("status");
        databaseReference.setValue(str).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext() , "Done " , Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    {
                        Toast.makeText(getApplicationContext() , " please try again " , Toast.LENGTH_SHORT).show();
                    }

            }
        });

    }
}
