package com.example.mrmohammad.protalk;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;


class TabsPagerAdapter extends FragmentPagerAdapter{


    public static final String TAG = TabsPagerAdapter.class.getSimpleName();

    Context ctx;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
            RequestsFragment requestsFragment = new RequestsFragment();
            return requestsFragment;

            case 1:
            ChatsFragment chatsFragment = new ChatsFragment();
            return chatsFragment;

            case 2:
            FriendsFragment friendsFragment = new FriendsFragment();
            return friendsFragment;

            default:
                Log.e(TAG, "Error");
                Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Requests";


            case 1:
                return "Chats";

            case 2:
                return "Friends";

                default:
                    Log.e(TAG, "Error");
                    Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                    return null;
        }
    }
}
