package taha.chatapplication.taha.chatapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingaActivity extends AppCompatActivity

{

    CircleImageView imageView ;
    TextView displayName ;
    TextView status ;


    String str_name;
    String str_imageLink;
    String str_status;
    String str_thumb_image;



    DatabaseReference databaseReference;
    StorageReference mImagesStorage;



    String current_user;


    private static final int Gallery_Pick =1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        inti();
    }


    private void inti()
    {
        imageView = (CircleImageView) findViewById(R.id.imageView);
        displayName = (TextView) findViewById(R.id.dis_name);
        status = (TextView) findViewById(R.id.status);

        mImagesStorage = FirebaseStorage.getInstance().getReference();
        current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                str_name = dataSnapshot.child("name").getValue().toString();
                str_imageLink = dataSnapshot.child("image").getValue().toString();
                str_status = dataSnapshot.child("status").getValue().toString();
                str_thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                displayName.setText(str_name);
                status.setText(str_status);
                if(!str_imageLink.equals("default"))
                {
                    //Picasso.with(getApplicationContext()).load(str_imageLink).placeholder(R.mipmap.default_avatr).into(imageView);
                    Picasso.with(getApplicationContext()).load(str_imageLink).placeholder(R.drawable.defaultperson).networkPolicy(NetworkPolicy.OFFLINE)
                            .into(imageView, new Callback()
                            {
                                @Override
                                public void onSuccess()
                                {

                                }

                                @Override
                                public void onError()
                                {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(getApplicationContext() , "some error " , Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void changeImage(View view)
    {
        Intent gallaryIntent = new Intent();
        gallaryIntent.setType("image/*");
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallaryIntent , "Choose image "),Gallery_Pick);

       /* CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);*/

    }

    public void changeStatus(View view)
    {
        startActivity(new Intent(this , UpdateStatus.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }



        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                final Uri resultUri = result.getUri();

                StorageReference fileBath = mImagesStorage.child("profiles_images").child(current_user.concat(".jpg"));


                fileBath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {

                        if(task.isSuccessful())
                        {
                            final String downloadLink = task.getResult().getDownloadUrl().toString();
                            databaseReference.child("image").setValue(downloadLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        File thumb_nail = new File(resultUri.getPath());

                                        try {
                                            Bitmap thumb_nail_bitmab = new Compressor(getApplicationContext())
                                                    .setMaxWidth(200)
                                                    .setMaxHeight(200)
                                                    .setQuality(75)
                                                    .compressToBitmap(thumb_nail);

                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            thumb_nail_bitmab.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                            byte[] thumb_data = baos.toByteArray();
                                            StorageReference thumb_fileBath = mImagesStorage.child("profiles_images").child("thumb").child(current_user.concat(".jpg"));
                                            UploadTask uploadTask = thumb_fileBath.putBytes(thumb_data);
                                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                                                {
                                                    String thumb_downloadLink = task.getResult().getDownloadUrl().toString();

                                                    if(task.isSuccessful())
                                                    {
                                                        HashMap hashMap = new HashMap<>();
                                                        hashMap.put("thumb_image",thumb_downloadLink);
                                                        hashMap.put("image" , downloadLink);

                                                        databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>()
                                                        {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if(task.isSuccessful())
                                                                    Toast.makeText(getApplicationContext() , "done" , Toast.LENGTH_SHORT).show();
                                                                else
                                                                    Toast.makeText(getApplicationContext() , "error" , Toast.LENGTH_SHORT).show();

                                                            }
                                                        });


                                                        Toast.makeText(getApplicationContext() , "done" , Toast.LENGTH_SHORT).show();



                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplication() , "Uploading error" , Toast.LENGTH_SHORT).show();

                                                    }


                                                }
                                            });
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                        Toast.makeText(getApplicationContext() , "done" , Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                        else
                            {
                                Toast.makeText(getApplication() , "Uploading error" , Toast.LENGTH_SHORT).show();

                            }

                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }
}
