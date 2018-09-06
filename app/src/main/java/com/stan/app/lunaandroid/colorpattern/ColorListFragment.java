package com.stan.app.lunaandroid.colorpattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.colorpattern.helper.OnStateChangeListener;
import com.stan.app.lunaandroid.colorpattern.helper.SimpleItemTouchHelperCallback;
import com.stan.app.lunaandroid.colorpicker.ColorPickerActivity;
import com.stan.app.lunaandroid.util.PersistantData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ColorListFragment extends Fragment implements OnStateChangeListener {

    private final int COLOR_PICKER_REQUEST_CODE = 1;
    private OnColorChangeListener onColorChangeListener;

    private ItemTouchHelper itemTouchHelper;
    private RecyclerListAdapter adapter;

    public ColorListFragment() {
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
        return inflater.inflate(R.layout.fragment_color_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Integer> colors = PersistantData.getPickedColorList(getContext());
        if(colors.isEmpty()) colors.add(PersistantData.getLastPickedColor(getContext()));
        adapter = new RecyclerListAdapter(this, colors);

        RecyclerView recyclerView = view.findViewById(R.id.colors_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        final Intent intent = new Intent(getActivity(), ColorPickerActivity.class);
        view.findViewById(R.id.add_color_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getItemCount()>= 10) {
                    Toast.makeText(getContext(),R.string.max_colors_warnings, Toast.LENGTH_SHORT).show();
                }else{
                    startActivityForResult(intent, COLOR_PICKER_REQUEST_CODE);
                }
            }
        });
        view.findViewById(R.id.clearall_color_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.dismissAll();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COLOR_PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int r = data.getIntExtra("COLOR_R", 0);
                int g = data.getIntExtra("COLOR_G", 0);
                int b = data.getIntExtra("COLOR_B", 0);
                adapter.add(Color.rgb(r, g, b));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnColorChangeListener || getActivity() instanceof OnColorChangeListener) {
            if (getParentFragment() != null) {
                onColorChangeListener = (OnColorChangeListener) getParentFragment();
            } else {
                onColorChangeListener = (OnColorChangeListener) getActivity();
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ColorChangeListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onColorChangeListener = null;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onListUpdated(ArrayList<Integer> color) {
        onColorChangeListener.onColorChange(color);
    }

    @Override
    public void onPause() {
        super.onPause();
        PersistantData.setPickedColorList(adapter.getColorList());
        PersistantData.savePersistantValues(getContext());
    }

    public interface OnColorChangeListener {
        void onColorChange(ArrayList<Integer> colors);
    }
}
