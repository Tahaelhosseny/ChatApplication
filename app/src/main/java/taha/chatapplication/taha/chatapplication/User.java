package taha.chatapplication.taha.chatapplication;

import android.content.Context;

/**
 * Created by Taha on 2/24/2018.
 */

public class User
{
    String name ;
    String image ;
    String status ;
    String thumb_image;

    public String getThumb_image() {
        return thumb_image;
    }



    public void setThumb_image(String thumb_image , Context context) {
        this.thumb_image = thumb_image;
    }

    public User()
    {
    }


    public User(String name, String image, String status)
    {
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
