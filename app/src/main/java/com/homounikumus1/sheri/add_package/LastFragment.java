package com.homounikumus1.sheri.add_package;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.homounikumus1.sheri.MainActivity;
import com.homounikumus1.sheri.MyDataBase;
import com.homounikumus1.sheri.R;
import com.homounikumus1.sheri.StackAdapter;
import com.homounikumus1.sheri.StackItems;
import com.rd.PageIndicatorView;
import java.util.ArrayList;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static com.homounikumus1.sheri.add_package.AddActivity.position;
import static com.homounikumus1.sheri.add_package.MyPlace.address;
import static com.homounikumus1.sheri.add_package.MyPlace.dishString;
import static com.homounikumus1.sheri.add_package.MyPlace.latLng;
import static com.homounikumus1.sheri.add_package.MyPlace.placeName;
import static com.homounikumus1.sheri.add_package.PhotoFragment.paths;
import static com.homounikumus1.sheri.add_package.ScrollViewFragment.cost;


/**
 * A simple {@link Fragment} subclass.
 */
public class LastFragment extends Fragment {
    FeatureCoverFlow mCoverFlow;
    TextView myBurger;
    TextView myPlace;
    TextView generalDishMarkView;
    TextView generalPlaceMarkView;
    TextView generalMark;
    Button btnNext;

    public LastFragment() {
        // Required empty public constructor
    }


    public static LastFragment getInstance(double generalDishMark, double generalPlaceMark, String resultGeneral) {
        LastFragment lastFragment = new LastFragment();
        Bundle args = new Bundle();
        args.putDouble("generalDishMark", generalDishMark);
        args.putDouble("generalPlaceMark", generalPlaceMark);
        args.putString("resultGeneral", resultGeneral);

        lastFragment.setArguments(args);
        return lastFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_last, container, false);


        final double generalDishMark = getArguments().getDouble("generalDishMark");
        final double generalPlaceMark = getArguments().getDouble("generalPlaceMark");
        @SuppressLint("DefaultLocale") String resultDish = String.format("%.1f", (generalDishMark));
        @SuppressLint("DefaultLocale") String resultPlace = String.format("%.1f", (generalPlaceMark));
        String resultGeneral = getArguments().getString("resultGeneral");

        ArrayList<StackItems> list = new ArrayList<>();

        String[] images = paths.split("&");

        for (String image : images) {
            StackItems stackItems = new StackItems("i", image);
            list.add(stackItems);
        }

        myBurger = layout.findViewById(R.id.my_burger);
        myBurger.setText(dishString);

        generalDishMarkView = layout.findViewById(R.id.general_dish_mark);
        generalPlaceMarkView = layout.findViewById(R.id.general_place_mark);
        generalMark = layout.findViewById(R.id.general_mark);

        final SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(layout.getContext());

        if (!pm.getBoolean("didShowDishPrompt", false)) {
            new MaterialTapTargetPrompt.Builder(getActivity())
                    .setTarget(layout.findViewById(R.id.my_burger))
                    .setPrimaryText(getString(R.string.did_show_dish))
                    .setBackgroundColour(Color.parseColor("#F2ed7c02"))
                   // .setPromptBackground(new RectanglePromptBackground())
                   // .setPromptFocal(new RectanglePromptFocal())
                    .setFocalRadius(500f)
                    .setAnimationInterpolator(new FastOutSlowInInterpolator())
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
                                    || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED)
                            {
                                SharedPreferences.Editor editor = pm.edit();
                                editor.putBoolean("didShowDishPrompt", true);
                                editor.apply();
                            }
                        }
                    })
                    .show();
        }


        if (resultDish.endsWith("0")) {
            generalDishMarkView.setText(resultDish.substring(0, resultDish.indexOf(",")));
        } else {
            generalDishMarkView.setText(resultDish);
        }

        if (resultPlace.endsWith("0")) {
            generalPlaceMarkView.setText(resultPlace.substring(0, resultPlace.indexOf(",")));
        } else {
            generalPlaceMarkView.setText(resultPlace);
        }

        assert resultGeneral != null;
        if (resultGeneral.endsWith("0")) {
            generalMark.setText(resultGeneral.substring(0, resultGeneral.indexOf(",")));
        } else {
            generalMark.setText(resultGeneral);

        }

        myPlace = layout.findViewById(R.id.my_place);
        myPlace.setText(placeName);

        StackAdapter mAdapter = new StackAdapter(layout.getContext(), list);

        mCoverFlow = layout.findViewById(R.id.coverflow);
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
        Button btnNext = getActivity().findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDataBase myDataBase = new MyDataBase(layout.getContext());
                SQLiteDatabase db = myDataBase.getWritableDatabase();

                putInDb(db, dishString, address + "&&&" + placeName, latLng, String.valueOf(cost) + getResources().getString(R.string.r), generalDishMark, generalPlaceMark, paths);

                db.close();
                myDataBase.close();

                AddActivity.update();

                Intent intent = new Intent(layout.getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });


        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView title = getActivity().findViewById(R.id.title);
        title.setText(R.string.general);
        PageIndicatorView pageIndicatorView = getActivity().findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setSelection(position);
        Button btnNext = getActivity().findViewById(R.id.btnNext);
        btnNext.setText(R.string.end);
    }


    private void putInDb(SQLiteDatabase database, String name, String address, String latLng, String cost, double dish_mark, double general_mark, String image) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("ADDRESS", address);
        contentValues.put("LATLNG", latLng);
        contentValues.put("COST", cost);
        contentValues.put("DISH_MARK", dish_mark);
        contentValues.put("GENERAL_MARK", general_mark);
        contentValues.put("IMAGE", image);
        database.insert("DATA", null, contentValues);
    }
}
