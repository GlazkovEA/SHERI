package com.homounikumus1.sheri.drawer_fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.homounikumus1.sheri.R;

import static com.homounikumus1.sheri.MainActivity.item;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {


    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        ListView drawerList = getActivity().findViewById(R.id.drawer_list);
        item = 3;
        drawerList.setItemChecked(item, true);
        TextView title = getActivity().findViewById(R.id.title);
        title.setText(R.string.about_sheri);

        ImageButton sherBtn = getActivity().findViewById(R.id.drawer_button);
        sherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
            }
        });
        sherBtn.setImageDrawable(getResources().getDrawable(R.drawable.burger_passive));

        ImageButton backBtn = getActivity().findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

}
