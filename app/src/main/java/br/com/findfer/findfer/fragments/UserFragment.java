package br.com.findfer.findfer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.findfer.findfer.MainActivity;
import br.com.findfer.findfer.R;

/**
 * Created by infsolution on 03/09/17.
 */

public class UserFragment extends Fragment {
    private TextView lat;
    private TextView longi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_user_fragment, container, false);
        return rootView;
    }
}

