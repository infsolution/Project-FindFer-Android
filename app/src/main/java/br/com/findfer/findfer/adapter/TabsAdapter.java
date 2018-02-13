package br.com.findfer.findfer.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.findfer.findfer.MainActivity;
import br.com.findfer.findfer.fragments.PosterFragment;
import br.com.findfer.findfer.fragments.UserFragment;

/**
 * Created by infsolution on 26/09/17.
 */

public class TabsAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;
    private Context tContext;
    private String [] titles;// = {"ANUNCIOS", "CLIENTES"};
    public TabsAdapter(FragmentManager fm, Context context, String [] titles) {
        super(fm);
        tContext = context;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        if(position == 0){
            frag = new PosterFragment();
        }
        if(position == 1){
            frag = new UserFragment();
        }
        Bundle bd = new Bundle();
        bd.putInt("position",position);
        frag.setArguments(bd);
        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
