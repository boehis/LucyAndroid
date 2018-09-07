package com.stan.app.lunaandroid.colorpattern;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stan.app.lunaandroid.R;

import java.util.ArrayList;
import java.util.List;

public class ColorPatternFragment extends Fragment implements ColorListFragment.OnColorChangeListener {

    //Todo: add hex input for color upper left corner

    public ColorPatternFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color_pattern, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onColorChange(ArrayList<Integer> colors) {
        Log.i("", "onColorChange: " +colors);

    }
}
