package taha.chatapplication.taha.chatapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Switch;

/**
 * Created by Taha on 2/21/2018.
 */

class SectionsPageAdapter extends FragmentPagerAdapter
{
    public SectionsPageAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {

            case 0 :
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;


            case 1 :
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;


            case 2 :
                FrindsFragment frindsFragment = new FrindsFragment();
                return frindsFragment;

                default:
                return null;

        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }



    public CharSequence getPageTitle (int position)
    {
        switch (position)
        {
            case 0 :
                return "Requests";
            case 1 :
                return "Chats";
            case 2 :
                return "friends";
            default: return null ;
        }
    }
}
