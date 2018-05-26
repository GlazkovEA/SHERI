package com.homounikumus1.sheri;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.util.ArrayList;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Created by Homo__000 on 02.04.2018.
 */

public class StackAdapter extends BaseAdapter {
    private ArrayList<StackItems> arrayList;
    private LayoutInflater inflater;
    private ViewHolder holder = null;

    public StackAdapter(Context context, ArrayList<StackItems> arrayList) {
        this.arrayList = arrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public StackItems getItem(int pos) {
        return arrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.photo_item, parent, false);

            view.setOnClickListener(onClickListener(pos));
            holder = new ViewHolder();
            holder.image = view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String item = arrayList.get(pos).getImage();

        if (item.equals("")) {
            ImageView finger = view.findViewById(R.id.finger);
            finger.setVisibility(View.INVISIBLE);

            holder.image.setImageDrawable(view.getResources().getDrawable(R.drawable.burger));

        } else {
            holder.image.setImageURI(Uri.parse(item));

            ImageButton btn = view.findViewById(R.id.dialog);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog(pos);
                }
            });
        }


        return view;
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog(position);
            }
        };
    }

    public class ViewHolder {
        ImageView image;
    }

    private void alertDialog (int position) {
        @SuppressLint("InflateParams") final View alertDialog = inflater.inflate(R.layout.dialog_info, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(inflater.getContext());
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

        final AlertDialog alert = mDialogBuilder.create();
        ImageView image = alertDialog.findViewById(R.id.image);
        image.setImageURI(Uri.parse(arrayList.get(position).getImage()));
        ImageButton close = alertDialog.findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        alert.show();

        alert.onBackPressed();

    }



}
