package com.homounikumus1.sheri.drawer_fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.homounikumus1.sheri.R;
import java.util.ArrayList;
import static com.homounikumus1.sheri.MainActivity.item;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralMarks extends Fragment {
    private MapView mMapView;
    private GoogleMap googleMap;
    private View rootView;
    private static Bundle savedInstanceStateStatic;

    public GeneralMarks() {

        // Required empty public constructor
    }


    public static GeneralMarks newInstance (ArrayList<LocalMark> marks) {
        GeneralMarks generalMarks = new GeneralMarks();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("marks", marks);

        generalMarks.setArguments(bundle);
        return generalMarks;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_general_marks, container, false);

        savedInstanceStateStatic = savedInstanceState;


        return rootView;
    }

    private void addMarker(String s, String title) {
        try {
            float lat = Float.parseFloat(s.substring(s.indexOf('(') + 1, s.indexOf(',')));
            float lng = Float.parseFloat(s.substring(s.indexOf(',') + 1, s.indexOf(')')));

            LatLng latLng = new LatLng(lat, lng);

            googleMap.addMarker(new MarkerOptions().position(latLng).title(title));

            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(11).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        ListView drawerList = getActivity().findViewById(R.id.drawer_list);
        item = 2;
        drawerList.setItemChecked(item, true);

        ImageButton sherBtn = getActivity().findViewById(R.id.drawer_button);
        sherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
            }
        });
        sherBtn.setImageDrawable(getResources().getDrawable(R.drawable.burger_passive));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceStateStatic);
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.setBuildingsEnabled(true);
                googleMap.setIndoorEnabled(true);

                Bundle extras = getArguments();
                ArrayList<LocalMark> marks = extras.getParcelableArrayList("marks");

                assert marks != null;
                for (LocalMark m :
                        marks) {
                    addMarker(m.getLatLng(), m.getAddress());
                }
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        mMapView.onDestroy();
        onDestroy();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
