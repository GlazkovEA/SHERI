package com.homounikumus1.sheri.drawer_fragments;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.homounikumus1.sheri.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.homounikumus1.sheri.MainActivity.item;

/**
 * A simple {@link Fragment} subclass.
 */
public class DishesInPlaceFragment extends Fragment {
    private View rootView;
    private String place;

    @Override
    public void onStop() {
        super.onStop();
    }

    public static DishesInPlaceFragment newInstance(String address, ArrayList<LocalMark> marks) {
        DishesInPlaceFragment fragment = new DishesInPlaceFragment();
        Bundle args = new Bundle();
        args.putString("address", address);
        args.putParcelableArrayList("marks", marks);
        fragment.setArguments(args);
        return fragment;
    }


    public DishesInPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        ListView drawerList = getActivity().findViewById(R.id.drawer_list);
        item = 2;
        drawerList.setItemChecked(item, true);

        TextView title = getActivity().findViewById(R.id.title);
        title.setText(R.string.general_marks);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dishes_in_place, container, false);


        place = getArguments().getString("address");

        TextView placeName = rootView.findViewById(R.id.place_name);
        placeName.setText(place);
        ImageView line = rootView.findViewById(R.id.line);
        line.setMinimumWidth(place.length() * 50);

        final ArrayList<LocalMark> marks = addInMarksList();



        Button addBtn = rootView.findViewById(R.id.map_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = GeneralMarks.newInstance(marks);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment, "visible_fragment");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });

        return rootView;
    }

    private ArrayList<LocalMark> addInMarksList() {
        ArrayList<LocalMark> argMarks = getArguments().getParcelableArrayList("marks");
        final ArrayList<LocalMark> marks = new ArrayList<>();
        // marks = getArguments().getParcelableArrayList("marks");

        assert argMarks != null;
        for (LocalMark m :
                argMarks) {
            if (place.equals(m.getAddress())) {
                marks.add(new LocalMark(m.getPosition(), m.getName(), m.getAddress(), m.getLatLng(), m.getCoast(), m.getGeneralDishMark(), m.getPlaceMark(),
                        m.getPaths()) {
                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public int compareTo(@NonNull LocalMark o) {
                        int last = Double.compare(o.getGeneralDishMark(), this.getGeneralDishMark());

                        if (last != 0) {
                            return last;
                        }

                        last = this.getName().compareTo(o.getName());

                        return last;
                    }
                });
            }
        }

        Collections.sort(marks);

        ListView items = rootView.findViewById(R.id.items);
        BaseAdapter adapterForLocalMarks = new AdapterForLocalMarks(rootView.getContext(), marks) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.item, parent, false);
                TextView dish =  row.findViewById(R.id.name);
                TextView cost =  row.findViewById(R.id.cost);
                TextView dishMark = row.findViewById(R.id.dih_mark);
                TextView score =  row.findViewById(R.id.score_item);

                @SuppressLint("DefaultLocale") String dishMarkString = String.format("%.1f", products.get(position).getGeneralDishMark());
                if (dishMarkString.endsWith("0")) {
                    String integerDishMark = dishMarkString.substring(0, dishMarkString.indexOf(","));
                    switch (integerDishMark) {
                        case "1":
                            score.setText(R.string.score_one);
                            break;
                        case "5":
                            score.setText(R.string.score_plus);
                            break;
                        case "6":
                            score.setText(R.string.score_plus);
                            break;
                        case "7":
                            score.setText(R.string.score_plus);
                            break;
                        case "8":
                            score.setText(R.string.score_plus);
                            break;
                        case "9":
                            score.setText(R.string.score_plus);
                            break;
                        case "10":
                            score.setText(R.string.score_plus);
                            break;
                        default:
                            score.setText(R.string.score);
                            break;
                    }
                    dishMark.setText(integerDishMark);
                    dish.setText(products.get(position).getName());
                    cost.setText(products.get(position).getCoast());
                } else {
                    dish.setText(products.get(position).getName());
                    cost.setText(products.get(position).getCoast());
                    dishMark.setText(dishMarkString);
                }
                return row;
            }
        };
        items.setAdapter(adapterForLocalMarks);
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                MarksListItem fragment = MarksListItem.newInstance(marks.get(position));
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment, "visible_fragment");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });

        return marks;
    }
}
