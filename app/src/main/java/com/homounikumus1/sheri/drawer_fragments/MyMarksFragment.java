package com.homounikumus1.sheri.drawer_fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.homounikumus1.sheri.MyDataBase;
import com.homounikumus1.sheri.R;
import com.homounikumus1.sheri.add_package.AddActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static com.homounikumus1.sheri.MainActivity.item;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMarksFragment extends Fragment {
    private Spinner sort;
    private View rootView;
    private ArrayList<Integer> selectedItem = new ArrayList<>();
    public static int height = 0;

    public MyMarksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        if (height == 0) {

            RelativeLayout fr = (RelativeLayout) rootView;

            int topYcoord = fr.getTop();
            int bottomYcoord = fr.getBottom();
            height = ((bottomYcoord - topYcoord) / 100) * 75;

        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(sort);

            popupWindow.setHeight(height);


        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ListView drawerList = getActivity().findViewById(R.id.drawer_list);
        item = 1;
        drawerList.setItemChecked(item, true);
        TextView title = getActivity().findViewById(R.id.title);
        title.setText(R.string.my_marks);

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
        backBtn.setVisibility(View.INVISIBLE);


    }

    Button addBtn;
    Button firstButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_my_marks, container, false);

        firstButton = rootView.findViewById(R.id.first_btn);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(inflater.getContext(), AddActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        addBtn = rootView.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inflater.getContext(), AddActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        sort = rootView.findViewById(R.id.sort_list);

        start();

        return rootView;
    }

    private void start() {
        if (update().size() > 0) {
            firstButton.setVisibility(View.INVISIBLE);
        } else {
            sort.setVisibility(View.INVISIBLE);
            addBtn.setVisibility(View.INVISIBLE);
            ImageView line = rootView.findViewById(R.id.line);
            line.setVisibility(View.INVISIBLE);
            TextView place = rootView.findViewById(R.id.place);
            place.setVisibility(View.INVISIBLE);
        }

    }


    private ArrayList<LocalMark> addInMarksList(ArrayList<LocalMark> marks) {

        MyDataBase myDataBase = new MyDataBase(rootView.getContext());
        SQLiteDatabase db = myDataBase.getWritableDatabase();
        Cursor cursor = db.query("DATA", new String[]{"_id", "NAME", "ADDRESS", "COST", "DISH_MARK", "GENERAL_MARK", "LATLNG", "IMAGE"},
                null, null, null, null, null);

        ArrayList<String> localSort = new ArrayList<>();

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

                marks.add(new LocalMark(localPos, name, address, latLng, cost, dishMark, placeMark, paths) {
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

                if (!localSort.contains(address)) {
                    localSort.add(address);
                }
            } while (cursor.moveToNext());


            Collections.sort(localSort);

            ArrayList<String> sortList = new ArrayList<>();

            sortList.add(getResources().getStringArray(R.array.sort_list)[0]);
            sortList.addAll(localSort);
            marks.get(0).setList(sortList);

            MyCustomAdapter adapter = new MyCustomAdapter(rootView.getContext(),
                    R.layout.sort_item, sortList);

            sort.setAdapter(adapter);


            db.close();
            myDataBase.close();
            cursor.close();

        }


        return marks;
    }

    private class MyCustomAdapter extends ArrayAdapter<String> {
        List<String> sortList;

        MyCustomAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            this.sortList = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    @NonNull ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View row = inflater.inflate(R.layout.drop_down_spinner, parent, false);

            String address = sortList.get(position);
            if (address.length() > 15) {
                address = address.substring(0, 14) + "...";
            }

            TextView label = row.findViewById(R.id.text1);
            label.setText(address);

            return row;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.sort_item, parent, false);

            String address = sortList.get(position);
            if (address.length() > 15) {
                address = address.substring(0, 14) + "...";
            }


            TextView label = row.findViewById(R.id.my_sort_list_item);
            label.setText(address);


            return row;
        }

    }

    private ArrayList<LocalMark> update() {

        final ArrayList<LocalMark> marks = new ArrayList<>();
        final ArrayList<LocalMark> localMarks = new ArrayList<>();
        addInMarksList(marks);
        if (marks.size() > 0) {
            final List<String> sortList = marks.get(0).getList();
            final ListView items = rootView.findViewById(R.id.items);

            sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0) {
                        Collections.sort(marks);
                        final AdapterForLocalMarks adapterForLocalMarks = new AdapterForLocalMarks(rootView.getContext(), marks) {


                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                View row = inflater.inflate(R.layout.item, parent, false);

                                TextView dish = row.findViewById(R.id.name);
                                TextView cost = row.findViewById(R.id.cost);
                                TextView dishMark = row.findViewById(R.id.dih_mark);
                                TextView score = row.findViewById(R.id.score_item);

                                final SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());

                                if (!pm.getBoolean("didShowItemPrompt", false)) {
                                    new MaterialTapTargetPrompt.Builder(getActivity())
                                            .setTarget(row.findViewById(R.id.square))
                                            .setPrimaryText(getString(R.string.did_show_item))
                                            .setBackgroundColour(Color.parseColor("#F2ed7c02"))
                                            .setFocalRadius(350f)
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
                                                        editor.putBoolean("didShowItemPrompt", true);
                                                        editor.apply();
                                                    }
                                                }
                                            })
                                            .show();
                                }

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

                                row.setBackgroundColor(mSelectedItemsIds.get(position) ? getResources().getColor(R.color.cream)
                                        : Color.TRANSPARENT);

                                return row;
                            }


                        };

                        items.setAdapter(adapterForLocalMarks);
                        items.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                        items.setMultiChoiceModeListener(new CustomMultiChoiceModeListener(adapterForLocalMarks, marks));

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

                    } else {
                        String placeName = sortList.get(position);

                        localMarks.clear();
                        for (int i = 0; i < marks.size(); i++) {
                            if (placeName.equals(marks.get(i).getAddress())) {
                                localMarks.add(marks.get(i));
                            }
                        }

                        Collections.sort(localMarks);
                        AdapterForLocalMarks adapterForLocalMarks = new AdapterForLocalMarks(rootView.getContext(), localMarks) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                View row = inflater.inflate(R.layout.item, parent, false);
                                TextView dish = row.findViewById(R.id.name);
                                TextView cost =  row.findViewById(R.id.cost);
                                TextView dishMark =  row.findViewById(R.id.dih_mark);
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

                                row.setBackgroundColor(mSelectedItemsIds.get(position) ? getResources().getColor(R.color.cream)
                                        : Color.TRANSPARENT);

                                return row;
                            }
                        };
                        items.setAdapter(adapterForLocalMarks);
                        items.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                        items.setMultiChoiceModeListener(new CustomMultiChoiceModeListener(adapterForLocalMarks, localMarks));
                        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                int pos = localMarks.get(position).getPosition();

                                MarksListItem fragment = MarksListItem.newInstance(localMarks.get(position));
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.container, fragment, "visible_fragment");
                                ft.addToBackStack(null);
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.commit();
                            }
                        });

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if (localMarks.size() > 0) {
            return localMarks;
        }

        return marks;
    }


    class CustomMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {
        ArrayList<LocalMark> marks;
        AdapterForLocalMarks adapterForLocalMarks;

        CustomMultiChoiceModeListener(AdapterForLocalMarks adapterForLocalMarks, ArrayList<LocalMark> marks) {
            this.marks = marks;
            this.adapterForLocalMarks = adapterForLocalMarks;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position,
                                              long id, boolean checked) {
            // Here you can do something when items are selected/de-selected,
            // such as update the title in the CAB

            Integer item = marks.get(position).getPosition();

            adapterForLocalMarks.toggleSelection(position);//Toggle the selection
            boolean hasCheckedItems = adapterForLocalMarks.getSelectedCount() > 0;//Check if any items are already selected or not

            if (!hasCheckedItems && mode != null)
                // there no selected items, finish the actionMode
                mode.finish();
            if (mode != null)
                //set action mode title on item selection
                mode.setTitle(getString(R.string.del));
            assert mode != null;

            String subTitleSize = String.valueOf(adapterForLocalMarks
                    .getSelectedCount());
            String subTitle = getString(R.string.chose1);

            if (check(subTitleSize)) {
                subTitle = getString(R.string.chose2);
            } else {
                if (subTitleSize.endsWith("11"))
                    subTitle = getString(R.string.chose);
                if (!subTitleSize.endsWith("1"))
                    subTitle = getString(R.string.chose);
            }
            mode.setSubtitle(subTitleSize + " " + subTitle);

            if (!selectedItem.contains(item)) {
                selectedItem.add(item);
            } else {
                selectedItem.remove(item);
            }


        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // Respond to clicks on the actions in the CAB
            alert(mode);

            return true;

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate the menu for the CAB
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_contect, menu);

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            MyMarksFragment fragment = new MyMarksFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

            // Here you can make any necessary updates to the activity when
            // the CAB is removed. By default, selected items are deselected/unchecked.
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // Here you can perform updates to the CAB due to
            // an invalidate() request
            return false;
        }


        private void alert(final ActionMode mode) {
            @SuppressLint("InflateParams") final View alertDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_delite, null);//inflater.inflate(R.layout.dialog_info, null);
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
            mDialogBuilder.setView(alertDialog);
            TextView delete = alertDialog.findViewById(R.id.delete);
            String markString = getString(R.string.mark_string);
            String size = String.valueOf(selectedItem.size());

            if (check(size)) {
                markString = getString(R.string.mark_string_second);
            } else {
                if (size.endsWith("11"))
                    markString = getString(R.string.mark_string_third);
                if (!size.endsWith("1"))
                    markString = getString(R.string.mark_string_third);
            }

            String deleteString = getString(R.string.delete, selectedItem.size(), markString);
            delete.setText(deleteString);

            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            delete();
                            mode.finish();
                        }

                    });
            mDialogBuilder
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            mode.finish();
                        }

                    });
            mDialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK &&
                            event.getAction() == KeyEvent.ACTION_UP &&
                            !event.isCanceled()) {
                        dialog.cancel();
                        return true;
                    }
                    return false;
                }
            });


            final AlertDialog alert = mDialogBuilder.create();

            alert.show();

            alert.onBackPressed();


        }

        private boolean check (String s) {
            String[] mass = {"12", "13", "14"};
            if (s.endsWith("2") || s.endsWith("3") || s.endsWith("4")) {
                for (String mas : mass) {
                    if (s.endsWith(mas)) {
                        return false;
                    }
                }
                return true;
            }

            return false;
        }

        private void delete() {
            MyDataBase myDataBase = new MyDataBase(rootView.getContext());
            SQLiteDatabase db = myDataBase.getWritableDatabase();

            for (int i = 0; i < selectedItem.size(); i++) {
                db.delete("DATA", "_id = " + selectedItem.get(i), null);
            }

            db.close();
            myDataBase.close();
            selectedItem.clear();

           // mode.finish();
            MyMarksFragment fragment = new MyMarksFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        }

    }

}

