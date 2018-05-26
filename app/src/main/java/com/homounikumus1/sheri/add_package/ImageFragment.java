package com.homounikumus1.sheri.add_package;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.homounikumus1.sheri.R;
import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */

public class ImageFragment extends Fragment {

    public static ImageFragment getInstance(int a) {
        ImageFragment fragment = new ImageFragment();
        Bundle bdl = new Bundle();
        bdl.putInt("extra", a);

        fragment.setArguments(bdl);

        return fragment;
    }


    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_image, container, false);

        Bundle bdl = getArguments();

        ArrayList<String> pathes = new ArrayList<>();


        String[] arr = PhotoFragment.paths.split("&");
        Collections.addAll(pathes, arr);

        ImageView imageView = rootView.findViewById(R.id.imageF);
        imageView.setImageURI(Uri.parse(pathes.get(bdl.getInt("extra"))));

        return rootView;
    }



}
