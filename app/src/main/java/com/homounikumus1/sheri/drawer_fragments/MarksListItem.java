package com.homounikumus1.sheri.drawer_fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homounikumus1.sheri.R;
import com.homounikumus1.sheri.StackAdapter;
import com.homounikumus1.sheri.StackItems;

import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

import static com.homounikumus1.sheri.MainActivity.item;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarksListItem extends Fragment {


    public MarksListItem() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_marks_list_item, container, false);

        FeatureCoverFlow mCoverFlow;
        TextView myBurger;
        TextView myPlace;
        TextView generalDishMarkView;
        TextView generalPlaceMarkView;
        TextView generalMark;
        try {

            final LocalMark mark = getArguments().getParcelable("item");

            ImageButton sherBtn = getActivity().findViewById(R.id.drawer_button);
            sherBtn.setImageDrawable(getResources().getDrawable(R.drawable.share_button));

            sherBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    assert mark != null;
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.dish_message, mark.getName(), mark.getAddress(), String.format("%.1f", mark.getGeneralDishMark())));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_body));
                    startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.sher_mark)));
                }
            });


            assert mark != null;
            final String address = mark.getAddress();

            ArrayList<StackItems> list = new ArrayList<>();


            String[] images = mark.getPaths().split("&");

            for (String image : images) {
                StackItems stackItems = new StackItems("i", image);
                list.add(stackItems);
            }
            myBurger = rootView.findViewById(R.id.my_burger);
            myBurger.setText(mark.getName());

            generalDishMarkView = rootView.findViewById(R.id.general_dish_mark);
            generalPlaceMarkView = rootView.findViewById(R.id.general_place_mark);
            generalMark = rootView.findViewById(R.id.general_mark);

            @SuppressLint("DefaultLocale") String placeMarkString = String.format("%.1f", mark.getPlaceMark());
            @SuppressLint("DefaultLocale") String result = String.format("%.1f", (mark.getPlaceMark() / 10) * mark.getGeneralDishMark());
            @SuppressLint("DefaultLocale") String dishMarkString = String.format("%.1f", mark.getGeneralDishMark());

            if (result.endsWith("0")) {
                generalMark.setText(result.substring(0, result.indexOf(",")));

            } else {
                generalMark.setText(result);
            }

            if (placeMarkString.endsWith("0")) {
                generalPlaceMarkView.setText(placeMarkString.substring(0, placeMarkString.indexOf(",")));
            } else {
                generalPlaceMarkView.setText(placeMarkString);
            }

            if (dishMarkString.endsWith("0")) {
                generalDishMarkView.setText(dishMarkString.substring(0, dishMarkString.indexOf(",")));
            } else {
                generalDishMarkView.setText(dishMarkString);
            }

            myPlace = rootView.findViewById(R.id.my_place);
            myPlace.setText(address);

            StackAdapter mAdapter = new StackAdapter(rootView.getContext(), list);

            mCoverFlow = rootView.findViewById(R.id.coverflow);
            mCoverFlow.setAdapter(mAdapter);

            mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

            mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
                @Override
                public void onScrolledToPosition(int position) {

                }

                @Override
                public void onScrolling() {

                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        item = 5;
        TextView title = getActivity().findViewById(R.id.title);
        title.setText(R.string.dish_mark);
        ImageButton backBtn = getActivity().findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);

    }


    public static MarksListItem newInstance(LocalMark mark) {
        MarksListItem fragment = new MarksListItem();
        Bundle args = new Bundle();
        args.putParcelable("item", mark);
        fragment.setArguments(args);
        return fragment;
    }
}
