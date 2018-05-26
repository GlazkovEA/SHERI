package com.homounikumus1.sheri.add_package;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.homounikumus1.sheri.MainActivity;
import com.homounikumus1.sheri.R;
import com.homounikumus1.sheri.add_package.AddActivity;
import com.rd.PageIndicatorView;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static com.homounikumus1.sheri.add_package.AddActivity.position;
import static com.homounikumus1.sheri.add_package.PhotoFragment.appearance;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScrollViewFragment extends Fragment implements View.OnClickListener {
    private Button saturationPlus;
    private Button btnNext;
    private Button ingredientsPlus;
    private Button bunPlus;
    private Button cutletPlus;
    private Button succulencePlus;
    private Button coastPlus;
    private Button saturationMinus;
    private Button ingredientsMinus;
    private Button bunMinus;
    private Button cutletMinus;
    private Button succulenceMinus;
    private Button coastMinus;
    private Button saturationScore;
    private Button ingredientsScore;
    private Button bunScore;
    private Button cutletScore;
    private Button succulenceScore;
    private Button coastScore;
    private ImageView imgSaturation;
    private ImageView imgIngredients;
    private ImageView imgBun;
    private ImageView imgCutlet;
    private ImageView imgSucculence;
    private View layout;
    private Handler handler;
    private Button chkButton;
    private TextView saturationTextView;
    private TextView ingredientsTextView;
    private TextView bunTextView;
    private TextView cutletTextView;
    private TextView succulenceTextView;
    private TextView text;
    private EditText editPrice;
    private boolean color;
    static int saturation;
    static int ingredients;
    static int bun;
    static int cutlet;
    static int succulence;
    static int cost;


    public ScrollViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_scroll_view, container, false);
        chkButton = container.getRootView().findViewById(R.id.btnNext);
        if (check()) {
            chkButton.setBackgroundColor(getResources().getColor(R.color.bright_orange));
        } else {
            chkButton.setBackgroundColor(getResources().getColor(R.color.empty_btn));
        }
        imgSaturation = layout.findViewById(R.id.img_saturation);
        imgIngredients = layout.findViewById(R.id.img_ingredients);
        imgBun = layout.findViewById(R.id.img_bun);
        imgCutlet = layout.findViewById(R.id.img_cutlet);
        imgSucculence = layout.findViewById(R.id.img_succulence);
        saturationPlus = layout.findViewById(R.id.saturation_plus);
        ingredientsPlus = layout.findViewById(R.id.ingredients_plus);
        bunPlus = layout.findViewById(R.id.bun_plus);
        cutletPlus = layout.findViewById(R.id.cutlet_plus);
        succulencePlus = layout.findViewById(R.id.succulence_plus);
        coastPlus = layout.findViewById(R.id.coastPlus);
        coastPlus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (coastPlus.isPressed()) {
                            cost++;
                            String costString = String.valueOf(cost);
                            editPrice.setText(costString);
                            coastScore.setText(costString);
                            handler.postDelayed(this, 1);
                        }
                    }
                });
                cost++;
                coastScore.setText(String.valueOf(cost));
                return false;
            }
        });

        saturationMinus = layout.findViewById(R.id.saturation_minus);
        ingredientsMinus = layout.findViewById(R.id.ingredients_minus);
        bunMinus = layout.findViewById(R.id.bun_minus);
        cutletMinus = layout.findViewById(R.id.cutlet_minus);
        succulenceMinus = layout.findViewById(R.id.succulence_minus);
        coastMinus = layout.findViewById(R.id.coastMinus);
        coastMinus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (coastMinus.isPressed()) {
                            if (cost > 0) {
                                cost--;
                                String costString = String.valueOf(cost);
                                editPrice.setText(costString);
                                coastScore.setText(costString);
                                handler.postDelayed(this, 1);
                            }
                        }
                    }
                });
                cost--;
                coastScore.setText(String.valueOf(cost));
                return false;
            }
        });

        saturationScore = layout.findViewById(R.id.saturation_score);
        ingredientsScore = layout.findViewById(R.id.ingredients_score);
        bunScore = layout.findViewById(R.id.bun_score);
        cutletScore = layout.findViewById(R.id.cutlet_score);
        succulenceScore = layout.findViewById(R.id.succulence_score);
        coastScore = layout.findViewById(R.id.coast_score);

        saturationTextView = layout.findViewById(R.id.saturation);
        ingredientsTextView = layout.findViewById(R.id.ingredients);
        bunTextView = layout.findViewById(R.id.bun);
        cutletTextView = layout.findViewById(R.id.cutlet);
        succulenceTextView = layout.findViewById(R.id.succulence);

        editPrice = layout.findViewById(R.id.edit_price);

        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String stringCost;
                if (!s.toString().equals("")) {
                    cost = Integer.parseInt(s.toString());
                    stringCost = s.toString() + " " + getResources().getString(R.string.currency);
                    coastScore.setText(stringCost);
                } else {
                    cost = 0;
                    stringCost = "0 " + getResources().getString(R.string.currency);
                    coastScore.setText(stringCost);
                }

                if (check()) {
                    chkButton.setBackgroundColor(getResources().getColor(R.color.bright_orange));
                } else {
                    chkButton.setBackgroundColor(getResources().getColor(R.color.empty_btn));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        saturationPlus.setOnClickListener(this);
        ingredientsPlus.setOnClickListener(this);
        bunPlus.setOnClickListener(this);
        cutletPlus.setOnClickListener(this);
        succulencePlus.setOnClickListener(this);
        coastPlus.setOnClickListener(this);
        saturationMinus.setOnClickListener(this);
        ingredientsMinus.setOnClickListener(this);
        bunMinus.setOnClickListener(this);
        cutletMinus.setOnClickListener(this);
        succulenceMinus.setOnClickListener(this);
        coastMinus.setOnClickListener(this);
        saturationTextView.setOnClickListener(this);
        ingredientsTextView.setOnClickListener(this);
        bunTextView.setOnClickListener(this);
        cutletTextView.setOnClickListener(this);
        succulenceTextView.setOnClickListener(this);

        saturationScore.setText(String.valueOf(saturation));
        ingredientsScore.setText(String.valueOf(ingredients));
        bunScore.setText(String.valueOf(bun));
        cutletScore.setText(String.valueOf(cutlet));
        succulenceScore.setText(String.valueOf(succulence));
        coastScore.setText(String.valueOf(cost));

        btnNext = getActivity().findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saturation != 0 && ingredients != 0 && bun != 0 && cutlet != 0 && succulence != 0) {
                    if (cost != 0) {
                        position++;
                        double generalDishMark = (saturation + ingredients + bun + cutlet + succulence + appearance) / 6.0;

                        Fragment fragment = PlaceMarkFragment.newInstance(generalDishMark);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.place_container, fragment, "visible_fragment");
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                        // setFragmentTitle(position);
                        // pageIndicatorView.setSelection(position);
                    } else {
                        Toast.makeText(layout.getContext(), getResources().getString(R.string.null_coast), Toast.LENGTH_SHORT).show();
                        invalidInput();
                    }
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
            case R.id.saturation:
                dialog(getString(R.string.saturation), getString(R.string.saturation_info));
                break;
            case R.id.ingredients:
                dialog(getString(R.string.ingredients), getString(R.string.ingredients_info));
                break;
            case R.id.bun:
                dialog(getString(R.string.bun), getString(R.string.bun_info));
                break;
            case R.id.cutlet:
                dialog(getString(R.string.cutlet), getString(R.string.cutlet_info));
                break;
            case R.id.succulence:
                dialog(getString(R.string.succulence), getString(R.string.succulence_info));
                break;
            case R.id.saturation_plus:
                if (saturation < 10) {
                    saturation++;
                    paint(0);
                    saturationScore.setText(String.valueOf(saturation));

                }
                break;
            case R.id.ingredients_plus:
                if (ingredients < 10) {
                    ingredients++;
                    paint(1);
                    ingredientsScore.setText(String.valueOf(ingredients));
                }
                break;
            case R.id.bun_plus:
                if (bun < 10) {
                    bun++;
                    paint(2);
                    bunScore.setText(String.valueOf(bun));
                }
                break;
            case R.id.cutlet_plus:
                if (cutlet < 10) {
                    cutlet++;
                    paint(3);
                    cutletScore.setText(String.valueOf(cutlet));
                }
                break;
            case R.id.succulence_plus:
                if (succulence < 10) {
                    succulence++;
                    paint(4);
                    succulenceScore.setText(String.valueOf(succulence));
                }
                break;
            case R.id.coastPlus:
                cost++;
                String stringCost = String.valueOf(cost) + " " + getResources().getString(R.string.currency);
                coastScore.setText(stringCost);

                break;
            case R.id.saturation_minus:
                if (saturation > 0) {
                    saturation--;
                    paint(0);
                    saturationScore.setText(String.valueOf(saturation));
                    if (saturation == 0) {
                        paint(0);
                    }
                }
                break;
            case R.id.ingredients_minus:
                if (ingredients > 0) {
                    ingredients--;
                    paint(1);
                    ingredientsScore.setText(String.valueOf(ingredients));
                    if (ingredients == 0) {
                        paint(1);
                    }
                }
                break;
            case R.id.bun_minus:
                if (bun > 0) {
                    bun--;
                    paint(2);
                    bunScore.setText(String.valueOf(bun));
                    if (bun == 0) {
                        paint(2);
                    }
                }
                break;
            case R.id.cutlet_minus:
                if (cutlet > 0) {
                    cutlet--;
                    paint(3);
                    cutletScore.setText(String.valueOf(cutlet));
                    if (cutlet == 0) {
                        paint(3);
                    }
                }
                break;
            case R.id.succulence_minus:
                if (succulence > 0) {
                    succulence--;
                    paint(4);
                    succulenceScore.setText(String.valueOf(succulence));
                    if (succulence == 0) {
                        paint(4);
                    }
                }
                break;
            case R.id.coastMinus:
                if (cost > 0) {
                    cost--;
                    String stringCost2 = String.valueOf(cost) + " " + getResources().getString(R.string.currency);
                    coastScore.setText(stringCost2);

                }
                break;
        }

        if (check()) {
            chkButton.setBackgroundColor(getResources().getColor(R.color.bright_orange));
        } else {
            chkButton.setBackgroundColor(getResources().getColor(R.color.empty_btn));
        }
    }

    private boolean check() {
        return cost > 0 && succulence > 0 && cutlet > 0 && bun > 0 && saturation > 0 && ingredients > 0;

    }

    private void dialog(String title, String info) {
        @SuppressLint("InflateParams") View alertDialog = getLayoutInflater().inflate(R.layout.dialog_info_mark, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(layout.getContext());
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
                            return true;
                        }
                        return false;
                    }
                });

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
        title.setText(R.string.other_marks);
        PageIndicatorView pageIndicatorView = getActivity().findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setSelection(position);
        Button btnNext = getActivity().findViewById(R.id.btnNext);
        btnNext.setText(R.string.next);

        if (cost != 0) {
            editPrice.setText(String.valueOf(cost));
        }

        paint(0);
        paint(1);
        paint(2);
        paint(3);
        paint(4);
    }

    private void paint(int pos) {
        switch (pos) {
            case 0:
                if (saturation > 0) {
                    imgSaturation.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    saturationScore.setTextColor(getResources().getColor(R.color.black));
                } else {
                    imgSaturation.setBackgroundColor(getResources().getColor(R.color.hint_grey));
                }
                break;
            case 1:
                if (ingredients > 0) {
                    imgIngredients.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    ingredientsScore.setTextColor(getResources().getColor(R.color.black));
                } else {
                    imgIngredients.setBackgroundColor(getResources().getColor(R.color.hint_grey));
                }
                break;
            case 2:
                if (bun > 0) {
                    imgBun.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    bunScore.setTextColor(getResources().getColor(R.color.black));
                } else {
                    imgBun.setBackgroundColor(getResources().getColor(R.color.hint_grey));
                }
                break;
            case 3:
                if (cutlet > 0) {
                    imgCutlet.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    cutletScore.setTextColor(getResources().getColor(R.color.black));
                } else {
                    imgCutlet.setBackgroundColor(getResources().getColor(R.color.hint_grey));
                }
                break;
            case 4:
                if (succulence > 0) {
                    imgSucculence.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    succulenceScore.setTextColor(getResources().getColor(R.color.black));
                } else {
                    imgSucculence.setBackgroundColor(getResources().getColor(R.color.hint_grey));
                }
                break;
        }
    }


    private void invalidInput() {
        final Handler handler = new Handler();
        text = layout.findViewById(R.id.text);
        color = false;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!color) {
                    ScrollView scrollView = layout.findViewById(R.id.scroll);


                    if (saturation == 0) {
                        imgSaturation.setBackgroundColor(Color.RED);
                        saturationScore.setTextColor(Color.RED);
                        saturationTextView.setTextColor(Color.RED);
                    }

                    if (ingredients == 0) {
                        imgIngredients.setBackgroundColor(Color.RED);
                        ingredientsScore.setTextColor(Color.RED);
                        ingredientsTextView.setTextColor(Color.RED);
                    }

                    if (bun == 0) {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                        imgBun.setBackgroundColor(Color.RED);
                        bunScore.setTextColor(Color.RED);
                        bunTextView.setTextColor(Color.RED);
                    }

                    if (cutlet == 0) {
                        imgCutlet.setBackgroundColor(Color.RED);
                        cutletScore.setTextColor(Color.RED);
                        cutletTextView.setTextColor(Color.RED);
                    }

                    if (succulence == 0) {
                        imgSucculence.setBackgroundColor(Color.RED);
                        succulenceScore.setTextColor(Color.RED);
                        succulenceTextView.setTextColor(Color.RED);
                    }

                    if (cost == 0) {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        coastScore.setTextColor(Color.RED);
                        text.setTextColor(Color.RED);
                    }

                    //  line.setBackgroundColor(Color.RED);
                    color = true;
                    handler.postDelayed(this, 1000);
                } else {
                    coastScore.setBackgroundColor(getResources().getColor(R.color.cream));
                    if (saturation == 0) {
                        imgSaturation.setBackgroundColor(getResources().getColor(R.color.grey));
                        saturationScore.setTextColor(getResources().getColor(R.color.grey));

                    }

                    if (ingredients == 0) {
                        imgIngredients.setBackgroundColor(getResources().getColor(R.color.grey));
                        ingredientsScore.setTextColor(getResources().getColor(R.color.grey));
                    }

                    if (bun == 0) {
                        imgBun.setBackgroundColor(getResources().getColor(R.color.grey));
                        bunScore.setTextColor(getResources().getColor(R.color.grey));
                    }

                    if (cutlet == 0) {
                        imgCutlet.setBackgroundColor(getResources().getColor(R.color.grey));
                        cutletScore.setTextColor(getResources().getColor(R.color.grey));
                    }

                    if (succulence == 0) {
                        imgSucculence.setBackgroundColor(getResources().getColor(R.color.grey));
                        succulenceScore.setTextColor(getResources().getColor(R.color.grey));
                    }

                    saturationTextView.setTextColor(getResources().getColor(R.color.black));
                    ingredientsTextView.setTextColor(getResources().getColor(R.color.black));
                    bunTextView.setTextColor(getResources().getColor(R.color.black));
                    cutletTextView.setTextColor(getResources().getColor(R.color.black));
                    succulenceTextView.setTextColor(getResources().getColor(R.color.black));
                    coastScore.setTextColor(getResources().getColor(R.color.black));
                    text.setTextColor(getResources().getColor(R.color.grey));
                }
            }
        };
        handler.post(runnable);
    }


}
