package taha.chatapplication.taha.chatapplication;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Taha on 2/25/2018.
 */

public class SchoolaChat extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        Picasso.Builder builder =new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this , Integer.MAX_VALUE));

        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }
}
