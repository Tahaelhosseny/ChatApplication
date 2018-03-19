package taha.chatapplication.taha.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsers extends AppCompatActivity
{

    private RecyclerView recyclerView ;
    Query databaseReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
         getSupportActionBar().setTitle("All Users");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

         inti();
    }


    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions firebaseOptions = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(databaseReference,User.class)
                .build();

        FirebaseRecyclerAdapter<User , UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(firebaseOptions)
        {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.single_user, parent, false);
                return new UserViewHolder(v);
            }


            @Override
            protected void onBindViewHolder(UserViewHolder viewHolder, int position,User model) {
                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setCircleImageView(model.getThumb_image(),getApplicationContext());
                final String User_ID = getRef(position).getKey();
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        startActivity(new Intent(getApplicationContext(),UserProfileActivity.class).putExtra("name",User_ID));
                    }
                });
            }

        };



        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void inti ()
    {
        recyclerView = (RecyclerView) findViewById(R.id.rec);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }






    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        View view ;
        TextView name ;
        TextView status;
        CircleImageView circleImageView;

        public UserViewHolder(View itemView)
        {
            super(itemView);
            view = itemView ;

        }


        public void setName (String n)
        {
            name = (TextView) view.findViewById(R.id.displayName);
            name.setText(n);
        }
        public void setStatus (String s)
        {
            status = (TextView)view.findViewById(R.id.status);
            status.setText(s);
        }
        public void setCircleImageView (String i , Context context)
        {
            circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
            Picasso.with(context).load(i).into(circleImageView);
        }

    }




}
