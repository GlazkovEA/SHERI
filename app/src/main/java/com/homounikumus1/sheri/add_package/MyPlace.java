package com.homounikumus1.sheri.add_package;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.MapsInitializer;
import com.homounikumus1.sheri.MyDataBase;
import com.homounikumus1.sheri.R;
import com.rd.PageIndicatorView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static android.app.Activity.RESULT_OK;
import static com.homounikumus1.sheri.add_package.AddActivity.position;

/**
 * A simple {@link Fragment} subclass.
 */

public class MyPlace extends Fragment {
    private View layout;
    private EditText dish;
    private ImageButton btn;
    private TextView choosePlace;
    private Button btnNext;
    private boolean color;
    private boolean checkPlace;
    static String address = "";
    static String placeName = "";
    static String latLng = "";
    static String dishString = "";
    private ImageView firstLine;
    private TextView place;
    private ImageView secondLine;
    private TextView placeNme;
    private View choosePlaceLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_my_place, container, false);

        choosePlace = layout.findViewById(R.id.choose_place);
        choosePlaceLayout = layout.findViewById(R.id.choose_place_layout);


        final SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(layout.getContext());

        if (!pm.getBoolean("didShowPlacePrompt", false)) {
            new MaterialTapTargetPrompt.Builder(getActivity())
                    .setTarget(layout.findViewById(R.id.tap))
                    .setPrimaryText(getString(R.string.did_show_place))
                    .setTextGravity(Gravity.CENTER)
                    .setFocalRadius(500f)
                    .setBackgroundColour(Color.parseColor("#F2ed7c02"))
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
                                editor.putBoolean("didShowPlacePrompt", true);
                                editor.apply();
                            }
                        }
                    })
                    .show();
        }



        firstLine = layout.findViewById(R.id.firs_line);
        place = layout.findViewById(R.id.place_chooser);
        dish = layout.findViewById(R.id.edit_txt);
        secondLine = layout.findViewById(R.id.second_line);
        placeNme = layout.findViewById(R.id.place_name);


        final ImageView secondLine = layout.findViewById(R.id.second_line);
        final Button btnNext = getActivity().findViewById(R.id.btnNext);

        dish.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    secondLine.setBackgroundColor(getResources().getColor(R.color.bright_orange));

                    if (!address.equals("")) {
                        btnNext.setBackgroundColor(getResources().getColor(R.color.bright_orange));
                    } else {
                        btnNext.setBackgroundColor(getResources().getColor(R.color.empty_btn));
                    }

                } else {
                    secondLine.setBackgroundColor(getResources().getColor(R.color.color_light_grey));
                    btnNext.setBackgroundColor(getResources().getColor(R.color.empty_btn));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }




        choosePlaceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    choosePlaceLayout.setClickable(false);
                    btn.setClickable(false);
                    Intent intent1 = new PlacePicker.IntentBuilder()
                            .build((Activity) layout.getContext());
                    startActivityForResult(intent1, Place.TYPE_FOOD);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        btn = layout.findViewById(R.id.tap);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    choosePlaceLayout.setClickable(false);
                    btn.setClickable(false);
                    Intent intent1 = new PlacePicker.IntentBuilder()
                            .build((Activity) layout.getContext());
                    startActivityForResult(intent1, Place.TYPE_FOOD);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!address.equals("") && !dish.getText().toString().equals("")) {
                    checkPlace = checkPlace();
                    if (!checkPlace) {
                        position++;
                        dishString = dish.getText().toString();
                        Fragment fragment = new PhotoFragment();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.place_container, fragment, "visible_fragment");
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                    } else {
                        Toast.makeText(layout.getContext(), getResources().getString(R.string.has_already), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(layout.getContext(), getResources().getString(R.string.not_all_filds), Toast.LENGTH_SHORT).show();

                    final Handler handler = new Handler();

                    color = false;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!color) {
                                if (address.equals("")) {
                                    choosePlace.setHintTextColor(Color.RED);
                                    firstLine.setBackgroundColor(Color.RED);
                                    place.setTextColor(Color.RED);
                                }

                                if (dish.getText().toString().equals("")) {
                                    dish.setHintTextColor(Color.RED);
                                    secondLine.setBackgroundColor(Color.RED);
                                    placeNme.setTextColor(Color.RED);
                                }
                                color = true;
                                handler.postDelayed(this, 1000);

                            } else {
                                if (choosePlace.getText().toString().equals("")) {
                                    choosePlace.setHintTextColor(getResources().getColor(R.color.color_ultra_light_grey));
                                    firstLine.setBackgroundColor(getResources().getColor(R.color.color_light_grey));
                                    place.setTextColor(getResources().getColor(R.color.color_light_grey));
                                }

                                if (dish.getText().toString().equals("")) {
                                    dish.setHintTextColor(getResources().getColor(R.color.color_ultra_light_grey));
                                    secondLine.setBackgroundColor(getResources().getColor(R.color.color_light_grey));
                                    placeNme.setTextColor(getResources().getColor(R.color.color_light_grey));
                                }
                            }

                        }
                    };
                    handler.post(runnable);
                }
            }
        });
        return layout;
    }

    @Override
    public void onResume() {
        choosePlaceLayout.setClickable(true);
        btn.setClickable(true);
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView title = getActivity().findViewById(R.id.title);
        title.setText(R.string.get_place);

        PageIndicatorView pageIndicatorView = getActivity().findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setSelection(position);
        Button btnNext = getActivity().findViewById(R.id.btnNext);
        btnNext.setText(R.string.next);

        if (!dishString.equals("")) {
            dish.setText(dishString);
            secondLine.setBackgroundColor(getResources().getColor(R.color.bright_orange));
        }

        if (!placeName.equals("")) {
            choosePlace.setText(placeName);
            choosePlace.setTextColor(getResources().getColor(R.color.black));
            firstLine.setBackgroundColor(getResources().getColor(R.color.bright_orange));
            btn.setImageDrawable(layout.getResources().getDrawable(R.mipmap.ic_launcher_orange));
        }

        if (!placeName.equals("") && !dishString.equals("")) {
            btnNext.setBackgroundColor(getResources().getColor(R.color.bright_orange));
        }

    }

    public MyPlace() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList list = new ArrayList();
            list.add(9);
            list.add(15);
            list.add(79);
            if (list.contains(PlacePicker.getPlace(data, layout.getContext()).getPlaceTypes().get(0))) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, layout.getContext());

                    choosePlace.setText(place.getName());
                    choosePlace.setTextColor(Color.BLACK);
                    btn.setImageDrawable(layout.getResources().getDrawable(R.mipmap.ic_launcher_orange));
                    firstLine = layout.findViewById(R.id.firs_line);
                    firstLine.setBackgroundColor(getResources().getColor(R.color.bright_orange));

                    address = Objects.requireNonNull(place.getAddress()).toString();
                    placeName = place.getName().toString();
                    latLng = place.getLatLng().toString();

                    if (!dish.getText().toString().equals("")) {
                        Button btnNext = getActivity().findViewById(R.id.btnNext);
                        btnNext.setBackgroundColor(getResources().getColor(R.color.bright_orange));
                    }
                }
            } else {
                Toast.makeText(layout.getContext(), getString(R.string.wrong_place), Toast.LENGTH_LONG).show();

                try {
                    Intent intent1 = new PlacePicker.IntentBuilder().build((Activity) layout.getContext());
                    startActivityForResult(intent1, Place.TYPE_FOOD);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkPlace() {

        MyDataBase myDataBase = new MyDataBase(layout.getContext());
        SQLiteDatabase db = myDataBase.getWritableDatabase();
        Cursor cursor = db.query("DATA", new String[]{"_id", "NAME", "ADDRESS"}, null, null, null, null, null);
        String nameInBase;
        String addressInBase;
        if (cursor.moveToFirst()) {
            int nameColumn = cursor.getColumnIndex("NAME");
            int addressColumn = cursor.getColumnIndex("ADDRESS");
            do {
                nameInBase = cursor.getString(nameColumn);
                addressInBase = cursor.getString(addressColumn);
                String placeInBase = addressInBase + nameInBase;
                String thisPlace = address + "&&&" + placeName + dish.getText().toString();

                if (placeInBase.toLowerCase().replace(" ", "").equals(thisPlace.toLowerCase().replace(" ", ""))) {
                    return true;
                }
            } while (cursor.moveToNext());
        }

        myDataBase.close();
        db.close();
        cursor.close();

        return false;
    }


}
