package com.homounikumus1.sheri.add_package;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.homounikumus1.sheri.R;
import com.homounikumus1.sheri.add_package.AddActivity;
import com.rd.PageIndicatorView;

import static com.homounikumus1.sheri.add_package.AddActivity.position;
import static com.homounikumus1.sheri.add_package.PhotoFragment.appearance;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceMarkFragment extends Fragment implements View.OnClickListener {
    private View layout;
    private Button interiorPlus;
    private Button musicPlus;
    private Button staffPlus;
    private Button purityPlus;
    private Button interiorMinus;
    private Button musicMinus;
    private Button staffMinus;
    private Button purityMinus;
    private Button interiorScore;
    private Button musicScore;
    private Button staffScore;
    private Button purityScore;
    private Button chkButton;
    private ImageView imgInterior;
    private ImageView imgMusic;
    private ImageView imgStaff;
    private ImageView imgPurity;
    private TextView interiorTextView;
    private TextView musicTextView;
    private TextView staffTextView;
    private TextView purityTextView;
    private Button btnNext;
    private boolean color;

    static int interior;
    static int music;
    static int staff;
    static int purity;


    public PlaceMarkFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_place_mark, container, false);

        chkButton = container.getRootView().findViewById(R.id.btnNext);
        if (check()) {
            chkButton.setBackgroundColor(getResources().getColor(R.color.bright_orange));
        } else {
            chkButton.setBackgroundColor(getResources().getColor(R.color.light_orange));
        }

        interiorTextView = layout.findViewById(R.id.interior);
        musicTextView = layout.findViewById(R.id.music);
        staffTextView = layout.findViewById(R.id.staff);
        purityTextView = layout.findViewById(R.id.purity);

        imgInterior = layout.findViewById(R.id.img_interior);
        imgMusic = layout.findViewById(R.id.img_music);
        imgStaff = layout.findViewById(R.id.img_staff);
        imgPurity = layout.findViewById(R.id.img_purity);

        interiorPlus = layout.findViewById(R.id.interior_plus);
        musicPlus = layout.findViewById(R.id.music_plus);
        staffPlus = layout.findViewById(R.id.staff_plus);
        purityPlus = layout.findViewById(R.id.purity_plus);

        interiorMinus = layout.findViewById(R.id.interior_minus);
        musicMinus = layout.findViewById(R.id.music_minus);
        staffMinus = layout.findViewById(R.id.staff_minus);
        purityMinus = layout.findViewById(R.id.purity_minus);

        interiorScore = layout.findViewById(R.id.interior_score);
        musicScore = layout.findViewById(R.id.music_score);
        staffScore = layout.findViewById(R.id.staff_score);
        purityScore = layout.findViewById(R.id.purity_score);

        interiorTextView.setOnClickListener(this);
        musicTextView.setOnClickListener(this);
        staffTextView.setOnClickListener(this);
        purityTextView.setOnClickListener(this);

        interiorPlus.setOnClickListener(this);
        musicPlus.setOnClickListener(this);
        staffPlus.setOnClickListener(this);
        purityPlus.setOnClickListener(this);
        interiorMinus.setOnClickListener(this);
        musicMinus.setOnClickListener(this);
        staffMinus.setOnClickListener(this);
        purityMinus.setOnClickListener(this);

        interiorScore.setText(String.valueOf(interior));
        musicScore.setText(String.valueOf(music));
        staffScore.setText(String.valueOf(staff));
        purityScore.setText(String.valueOf(purity));

        // Inflate the layout for this fragment




        btnNext = getActivity().findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interior != 0 && music != 0 && staff != 0 && purity != 0) {
                    Bundle bdl = getArguments();
                    double generalDishMark = bdl.getDouble("generalDishMark");
                    double  generalPlaceMark = (interior + music + staff + purity)/4.0;
                    @SuppressLint("DefaultLocale") String resultGeneral = String.format("%.1f", ((generalPlaceMark)/10.0)*(generalDishMark));

                    position++;

                    Fragment fragment = LastFragment.getInstance(generalDishMark, generalPlaceMark, resultGeneral);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.place_container, fragment, "visible_fragment");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                    btnNext.setText(R.string.end);
                } else {
                    Toast.makeText(layout.getContext(), getResources().getString(R.string.not_right_marks), Toast.LENGTH_SHORT).show();
                    invalidInput();
                }

            }
        });
        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.interior:
                dialog(getString(R.string.interior), getString(R.string.interior_info));
                break;
            case R.id.music:
                dialog(getString(R.string.music), getString(R.string.music_info));
                break;
            case R.id.staff:
                dialog(getString(R.string.staff), getString(R.string.staff_info));
                break;
            case R.id.purity:
                dialog(getString(R.string.purity), getString(R.string.purity_info));
                break;

            case R.id.interior_plus:
                if (interior < 10) {
                    interior++;
                    paint(0);
                    interiorScore.setText(String.valueOf(interior));

                }
                break;
            case R.id.music_plus:
                if (music < 10) {
                    music++;
                    paint(1);
                    musicScore.setText(String.valueOf(music));

                }
                break;
            case R.id.staff_plus:
                if (staff < 10) {
                    staff++;
                    paint(2);
                    staffScore.setText(String.valueOf(staff));

                }
                break;
            case R.id.purity_plus:
                if (purity < 10) {
                    purity++;
                    paint(3);
                    purityScore.setText(String.valueOf(purity));

                }
                break;
            case R.id.interior_minus:
                if (interior > 0) {
                    interior--;
                    paint(0);
                    interiorScore.setText(String.valueOf(interior));
                    if (interior == 0) {
                        paint(0);
                    }

                }
                break;
            case R.id.music_minus:
                if (music > 0) {
                    music--;
                    paint(1);
                    musicScore.setText(String.valueOf(music));
                    if (music== 0) {
                        paint(1);
                    }
                }
                break;
            case R.id.staff_minus:
                if (staff > 0) {
                    staff--;
                    paint(2);
                    staffScore.setText(String.valueOf(staff));
                    if (staff == 0) {
                        paint(2);
                    }
                }
                break;
            case R.id.purity_minus:
                if (purity > 0) {
                    purity--;
                    paint(3);
                    purityScore.setText(String.valueOf(purity));
                    if (purity == 0) {
                        paint(3);
                    }

                }
                break;
        }

        if (check()) {
            chkButton.setBackgroundColor(getResources().getColor(R.color.bright_orange));
        } else {
            chkButton.setBackgroundColor(getResources().getColor(R.color.light_orange));
        }
    }

    private boolean check() {
        return interior > 0 && music > 0 && staff > 0 && purity > 0;
    }

    private void paint(int pos) {
        switch (pos) {
            case 0:
                if (interior > 0) {
                    imgInterior.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    interiorScore.setTextColor(getResources().getColor(R.color.black));
                } else {
                    imgInterior.setBackgroundColor(getResources().getColor(R.color.hint_grey));
                }
                break;
            case 1:
                if (music > 0) {
                    imgMusic.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    musicScore.setTextColor(getResources().getColor(R.color.black));
                } else {
                    imgMusic.setBackgroundColor(getResources().getColor(R.color.hint_grey));
                }
                break;
            case 2:
                if (staff > 0) {
                    imgStaff.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    staffScore.setTextColor(getResources().getColor(R.color.black));
                } else {
                    imgStaff.setBackgroundColor(getResources().getColor(R.color.hint_grey));
                }
                break;
            case 3:
                if (purity > 0) {
                    imgPurity.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    purityScore.setTextColor(getResources().getColor(R.color.black));
                } else {
                    imgPurity.setBackgroundColor(getResources().getColor(R.color.hint_grey));
                }
                break;
        }
    }

    private void dialog(String title, String info) {
        @SuppressLint("InflateParams") View alertDialog = getLayoutInflater().inflate(R.layout.dialog_info_mark, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(layout.getContext());
        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(alertDialog);

        mDialogBuilder
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            dialog.cancel();
                            // showDialog(DIALOG_MENU);
                            return true;
                        }
                        return false;
                    }
                });

        //Создаем AlertDialog:
        final AlertDialog dialog = mDialogBuilder.create();

        TextView dialogText = alertDialog.findViewById(R.id.dish);
        TextView dishInfo = alertDialog.findViewById(R.id.dish_info);
        dialogText.setText(title);

        ImageView line = alertDialog.findViewById(R.id.line);
        line.setMinimumWidth(title.length() * 50);
        line.setMaxWidth(title.length() * 50 + 8);
        dishInfo.setText(info);

        ImageButton closeBtn = alertDialog.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

        dialog.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.title);
        title.setText(R.string.mark_place);
        PageIndicatorView pageIndicatorView = getActivity().findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setSelection(position);
        Button btnNext = getActivity().findViewById(R.id.btnNext);
        btnNext.setText(R.string.next);

        paint(0);
        paint(1);
        paint(2);
        paint(3);
    }

    public static Fragment newInstance(double generalDishMark) {
        PlaceMarkFragment fragment = new PlaceMarkFragment();
        Bundle args = new Bundle();
        args.putDouble("generalDishMark", generalDishMark);
        fragment.setArguments(args);
        return fragment;
    }

    private void invalidInput () {
        final Handler handler = new Handler();

        color = false;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!color) {
                    if (interior==0) {
                        imgInterior.setBackgroundColor(Color.RED);
                        interiorScore.setTextColor(Color.RED);
                        interiorTextView.setTextColor(Color.RED);
                    }

                    if (music==0) {
                        imgMusic.setBackgroundColor(Color.RED);
                        musicScore.setTextColor(Color.RED);
                        musicTextView.setTextColor(Color.RED);
                    }

                    if (staff==0) {
                        imgStaff.setBackgroundColor(Color.RED);
                        staffScore.setTextColor(Color.RED);
                        staffTextView.setTextColor(Color.RED);
                    }

                    if (purity==0) {
                        imgPurity.setBackgroundColor(Color.RED);
                        purityScore.setTextColor(Color.RED);
                        purityTextView.setTextColor(Color.RED);
                    }

                    //  line.setBackgroundColor(Color.RED);
                    color = true;
                    handler.postDelayed(this, 1000);
                } else {
                    if (interior==0) {
                        imgInterior.setBackgroundColor(getResources().getColor(R.color.grey));
                        interiorScore.setTextColor(getResources().getColor(R.color.grey));
                    }

                    if (music==0) {
                        imgMusic.setBackgroundColor(getResources().getColor(R.color.grey));
                        musicScore.setTextColor(getResources().getColor(R.color.grey));
                    }

                    if (staff==0) {
                        imgStaff.setBackgroundColor(getResources().getColor(R.color.grey));
                        staffScore.setTextColor(getResources().getColor(R.color.grey));
                    }

                    if (purity==0) {
                        imgPurity.setBackgroundColor(getResources().getColor(R.color.grey));
                        purityScore.setTextColor(getResources().getColor(R.color.grey));
                    }


                    interiorTextView.setTextColor(getResources().getColor(R.color.black));
                    musicTextView.setTextColor(getResources().getColor(R.color.black));
                    staffTextView.setTextColor(getResources().getColor(R.color.black));
                    purityTextView.setTextColor(getResources().getColor(R.color.black));


                }
            }

        };
        handler.post(runnable);
    }
}
