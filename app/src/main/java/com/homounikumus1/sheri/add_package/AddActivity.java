package com.homounikumus1.sheri.add_package;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.homounikumus1.sheri.MainActivity;
import com.homounikumus1.sheri.R;
import com.rd.PageIndicatorView;
import static com.homounikumus1.sheri.add_package.MyPlace.address;
import static com.homounikumus1.sheri.add_package.MyPlace.dishString;
import static com.homounikumus1.sheri.add_package.MyPlace.latLng;
import static com.homounikumus1.sheri.add_package.MyPlace.placeName;
import static com.homounikumus1.sheri.add_package.PhotoFragment.appearance;
import static com.homounikumus1.sheri.add_package.PhotoFragment.paths;
import static com.homounikumus1.sheri.add_package.PlaceMarkFragment.interior;
import static com.homounikumus1.sheri.add_package.PlaceMarkFragment.music;
import static com.homounikumus1.sheri.add_package.PlaceMarkFragment.purity;
import static com.homounikumus1.sheri.add_package.PlaceMarkFragment.staff;
import static com.homounikumus1.sheri.add_package.ScrollViewFragment.bun;
import static com.homounikumus1.sheri.add_package.ScrollViewFragment.cost;
import static com.homounikumus1.sheri.add_package.ScrollViewFragment.cutlet;
import static com.homounikumus1.sheri.add_package.ScrollViewFragment.ingredients;
import static com.homounikumus1.sheri.add_package.ScrollViewFragment.saturation;
import static com.homounikumus1.sheri.add_package.ScrollViewFragment.succulence;


public class AddActivity extends AppCompatActivity {
    static int position = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("position", position);

        outState.putString("dishString", dishString);
        outState.putString("placeName", placeName);
        outState.putString("address", address);
        outState.putString("latLng", latLng);

        outState.putString("paths", paths);
        outState.putInt("appearance", appearance);

        outState.putInt("saturation", saturation);
        outState.putInt("ingredients", ingredients);
        outState.putInt("bun", bun);
        outState.putInt("cutlet", cutlet);
        outState.putInt("succulence", succulence);
        outState.putInt("cost", cost);

        outState.putInt("interior", interior);
        outState.putInt("music", music);
        outState.putInt("staff", staff);
        outState.putInt("purity", purity);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");

            address = savedInstanceState.getString("address");
            latLng = savedInstanceState.getString("latLng");
            placeName = savedInstanceState.getString("placeName");
            dishString = savedInstanceState.getString("dishString");

            appearance = savedInstanceState.getInt("appearance");
            paths = savedInstanceState.getString("paths");

            saturation = savedInstanceState.getInt("saturation");
            ingredients = savedInstanceState.getInt("ingredients");
            bun = savedInstanceState.getInt("bun");
            cutlet = savedInstanceState.getInt("cutlet");
            succulence = savedInstanceState.getInt("succulence");
            cost = savedInstanceState.getInt("cost");

            interior = savedInstanceState.getInt("interior");
            music = savedInstanceState.getInt("music");
            staff = savedInstanceState.getInt("staff");
            purity = savedInstanceState.getInt("purity");
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageView backBtn = findViewById(R.id.drawer_button);
        backBtn.setVisibility(View.INVISIBLE);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setBackgroundColor(getResources().getColor(R.color.empty_btn));

        PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        TextView title = findViewById(R.id.title);
        pageIndicatorView.setCount(5);
        pageIndicatorView.setSelection(position);

        if (position == 0) {
            Fragment fragment = new MyPlace();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.place_container, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }


    }

    static void update() {
        saturation = 0;
        ingredients = 0;
        bun = 0;
        cutlet = 0;
        succulence = 0;
        cost = 0;
        appearance = 0;

        interior = 0;
        music = 0;
        staff = 0;
        purity = 0;

        position = 0;
        latLng = "";
        paths = "";
        address = "";
        placeName = "";
        dishString = "";

    }

    @Override
    public void onBackPressed() {
        position--;
        if (position < 0) {
            update();
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

}
