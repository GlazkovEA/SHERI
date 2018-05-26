package com.homounikumus1.sheri;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.support.v7.view.ActionMode;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.homounikumus1.sheri.drawer_fragments.AboutUsFragment;
import com.homounikumus1.sheri.drawer_fragments.MyMarksFragment;
import com.homounikumus1.sheri.drawer_fragments.MyPlaceMarksFragment;

import java.lang.reflect.Field;

import static com.homounikumus1.sheri.drawer_fragments.MyMarksFragment.height;

public class MainActivity extends AppCompatActivity {
    public static int item = 1;
    public DrawerLayout drawer;
    private String[] titles;
    private Fragment fragment;
    private ImageView backBtn;
    private String[] list;
    private ListView drawerList;
    private DrawerLayout drawerLayout;



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", item);
        outState.putInt("height", height);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState!=null) {
            item = savedInstanceState.getInt("position");
            height = savedInstanceState.getInt("height");
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,


        }, 1);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = 1;
                item(item);
                drawerList.setItemChecked(item, true);
            }
        });

     //   title = findViewById(R.id.title);
        titles = getResources().getStringArray(R.array.items);

        drawer = findViewById(R.id.drawer_layout);
        list = getResources().getStringArray(R.array.items);
        drawerList = findViewById(R.id.drawer_list);
        drawerLayout =  findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, list) {
                                  @SuppressLint("InflateParams")
                                  @NonNull
                                  @Override
                                  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                      View view;
                                      if (position == 0) {
                                          view = getLayoutInflater().inflate(R.layout.nav_header_main, null);
                                          view.setEnabled(false);
                                      } else {
                                          view = getLayoutInflater().inflate(R.layout.drawer_item, null);
                                          TextView textView = view.findViewById(R.id.text_item);
                                          textView.setText(list[position]);
                                          if (position == item) {
                                              view.findViewById(R.id.image_item).setBackgroundColor(getResources().getColor(R.color.bright_orange));
                                          }
                                      }

                                      return view;
                                  }
                              }
        );

        drawerList.setItemChecked(0, false);
        drawerList.setItemsCanFocus(false);
        drawerList = findViewById(R.id.drawer_list);
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    for (int i = 1; i < titles.length; i++) {
                        if (position != 4) {
                            if (position == i) {
                                drawerList.getChildAt(i).findViewById(R.id.image_item)
                                        .setBackgroundColor(getResources().getColor(R.color.bright_orange));
                            } else {
                                drawerList.getChildAt(i).findViewById(R.id.image_item)
                                        .setBackgroundColor(getResources().getColor(R.color.grey));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                item(position);

                drawerLayout.closeDrawer(GravityCompat.END);
            }
        });

        findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });
        backBtn = findViewById(R.id.back_btn);

        item(item);
       // drawerList.setItemChecked(item, true);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void item(int id) {
        if (id == 1) {
            fragment = new MyMarksFragment();
        } else if (id == 2) {
            fragment = new MyPlaceMarksFragment();
        } else if (id == 3) {
            fragment = new AboutUsFragment();
        } else if (id == 4) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.invitation_message));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.invitation_deep_link));
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.invite_fiend)));
        }
        if (fragment != null) {
            transaction(fragment);
        }
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (item==1) {
                finish();
            } else {
                super.onBackPressed();
                drawerList.setItemChecked(item, true);
            }
        }




    }


    public void drawerClose(View view) {
        drawer.closeDrawer(GravityCompat.END);
    }

    private void transaction(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }




}

