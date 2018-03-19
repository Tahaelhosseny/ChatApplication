package taha.chatapplication.taha.chatapplication;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{

    FirebaseAuth mAuth;
    private SectionsPageAdapter mSectionsPageAdapter ;
    ViewPager viewPager ;


    private TabLayout tabLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiat();

    }



    private void intiat()
    {
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("SchoolhChat");
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.tab_pager);
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendToStart();
        }
    }


    private void sendToStart()
    {
        startActivity(new Intent(this ,Start.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu ,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if(item.getTitle().equals("logOut"))
        {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
            Toast.makeText(getApplicationContext() , "signOut" , Toast.LENGTH_SHORT).show();
        }

        if(item.getTitle().equals("account settings"))
        {
            startActivity(new Intent(getApplicationContext() , SettingaActivity.class));
        }

        if(item.getTitle().equals("All Users"))
        {
            startActivity(new Intent(getApplicationContext() , AllUsers.class));
        }

        return true;
    }
}
