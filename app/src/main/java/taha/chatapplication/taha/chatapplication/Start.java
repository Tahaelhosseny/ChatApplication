package taha.chatapplication.taha.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Start extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


    }


    public void createNewAccount(View view)
    {
        startActivity(new Intent(this , RegisterActivity.class));
    }

    public void Login(View view)
    {
        startActivity(new Intent(this , LoginActivity.class));

    }
}
