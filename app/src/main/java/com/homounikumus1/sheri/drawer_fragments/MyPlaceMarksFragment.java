package com.homounikumus1.sheri.drawer_fragments;


import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import android.widget.TextView;
import com.homounikumus1.sheri.MyDataBase;
import com.homounikumus1.sheri.R;
import com.homounikumus1.sheri.add_package.AddActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.homounikumus1.sheri.MainActivity.item;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlaceMarksFragment extends Fragment {
    //private List<LocalMark> marks = new ArrayList<>();
    private View rootView;
    private Button addBtn;


    @Override
    public void onStop() {
        super.onStop();
       // marks.clear();
    }

    public MyPlaceMarksFragment() {
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

        ImageButton backBtn = getActivity().findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_place_marks, container, false);

        Button firstButton = rootView.findViewById(R.id.first_btn);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), AddActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        addBtn = rootView.findViewById(R.id.map_btn);

        final ArrayList<LocalMark> marks = addInMarksList();

        /*addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Need_This", marks.size() + " размер");
                Fragment fragment = GeneralMarks.newInstance(marks);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment, "visible_fragment");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });*/

        if (marks.size() > 0) {
            firstButton.setVisibility(View.INVISIBLE);
        } else {
            addBtn.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }


    private ArrayList<LocalMark> addInMarksList() {
        final ArrayList<LocalMark> localMarks = new ArrayList<>();
        ArrayList<LocalMark> marks = new ArrayList<>();
        MyDataBase myDataBase = new MyDataBase(rootView.getContext());
        SQLiteDatabase db = myDataBase.getWritableDatabase();
        Cursor cursor = db.query("DATA", new String[]{"_id", "NAME", "ADDRESS", "COST", "DISH_MARK","GENERAL_MARK", "LATLNG", "IMAGE"},
                null, null, null, null, null);

        int localPos;
        String name;
        String address;
        String latLng;
        String cost;
        double dishMark;
        double placeMark;
        String paths;

        if (cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex("_id");
            int nameColumn = cursor.getColumnIndex("NAME");
            int addressColumn = cursor.getColumnIndex("ADDRESS");
            int latLngColumn = cursor.getColumnIndex("LATLNG");
            int costColumn = cursor.getColumnIndex("COST");
            int dishMarkColumn = cursor.getColumnIndex("DISH_MARK");
            int placeMarkColumn = cursor.getColumnIndex("GENERAL_MARK");
            int pathsMarkColumn = cursor.getColumnIndex("IMAGE");
            do {
                localPos = cursor.getInt(idColumn);
                name = cursor.getString(nameColumn);
                address = cursor.getString(addressColumn);
                address = address.substring(address.indexOf("&&&") + 3);
                latLng = cursor.getString(latLngColumn);
                cost = cursor.getString(costColumn);
                dishMark = cursor.getDouble(dishMarkColumn);
                placeMark = cursor.getDouble(placeMarkColumn);
                paths = cursor.getString(pathsMarkColumn);

                localMarks.add(new LocalMark(localPos, name, address,latLng, cost, dishMark, placeMark, paths) {
                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public int compareTo(@NonNull LocalMark o) {
                        int last = Double.compare(o.getPlaceMark(), this.getPlaceMark());

                        if (last != 0) {
                            return last;
                        }

                        last = this.getName().compareTo(o.getName());

                        return last;
                    }
                });
            } while (cursor.moveToNext());

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment fragment = GeneralMarks.newInstance(localMarks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment, "visible_fragment");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            });

            marks  = averagePlaceMark(localMarks);
            Collections.sort(marks);

            ListView items = rootView.findViewById(R.id.items);
            BaseAdapter adapterForLocalMarks = new AdapterForLocalMarks(rootView.getContext(), marks) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View row = inflater.inflate(R.layout.item, parent, false);
                    TextView dish =  row.findViewById(R.id.name);
                    TextView cost =  row.findViewById(R.id.cost);
                    TextView dishMark = row.findViewById(R.id.dih_mark);
                    TextView score = row.findViewById(R.id.score_item);


                    @SuppressLint("DefaultLocale") String placeMarkString = String.format("%.1f", products.get(position).getPlaceMark());
                    String stringCost;

                    if (placeMarkString.endsWith("0")) {
                        String integerDishMark = placeMarkString.substring(0, placeMarkString.indexOf(","));
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
                        dish.setText(products.get(position).getAddress());
                        stringCost = getResources().getString(R.string.appraised_dishes) + " " + products.get(position).getQuantity();
                        cost.setText(stringCost);
                    } else {
                        dish.setText(products.get(position).getAddress());
                        stringCost = getResources().getString(R.string.appraised_dishes) + " " + products.get(position).getQuantity();
                        cost.setText(stringCost);
                        dishMark.setText(placeMarkString);
                    }
                    return row;
                }
            };
            items.setAdapter(adapterForLocalMarks);
            final ArrayList<LocalMark> finalMarks = marks;
            items.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Fragment fragment = DishesInPlaceFragment.newInstance(finalMarks.get(position).getAddress(), localMarks);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment, "visible_fragment");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                }
            });

            db.close();
            myDataBase.close();
            cursor.close();
        }

        return marks;
    }

    private ArrayList<LocalMark> averagePlaceMark(List<LocalMark> list) {
        ArrayList<LocalMark> localMarks = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int count = 0;
            double sum = 0;
            for (LocalMark a :
                    list) {
                if (a.getAddress().equals(list.get(i).getAddress())) {
                    count++;
                    sum += a.getPlaceMark();
                }
            }
            if (!isContains(localMarks, list.get(i).getAddress())) {
                LocalMark localMark = list.get(i);
                localMark.setPlaceMark(sum / count);
                localMark.setQuantity(count);
                localMarks.add(localMark);
            }
        }

        return localMarks;
    }


    private boolean isContains(List<LocalMark> localMarks, String address) {
        for (LocalMark mark :
                localMarks) {
            if (mark.getAddress().equals(address)) {
                return true;
            }
        }
        return false;
    }

}
