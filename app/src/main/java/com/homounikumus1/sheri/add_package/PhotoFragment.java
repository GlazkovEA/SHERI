package com.homounikumus1.sheri.add_package;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.homounikumus1.sheri.R;
import com.rd.PageIndicatorView;

import java.io.File;
import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static com.homounikumus1.sheri.add_package.AddActivity.position;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment implements SurfaceHolder.Callback, android.hardware.Camera.PictureCallback, android.hardware.Camera.PreviewCallback, android.hardware.Camera.AutoFocusCallback {
    private android.hardware.Camera camera;
    private File directory;
    private boolean color;
    private File file;
    public String lastFile = "";
    private int numPages = 1;
    private FlippableStackView mPager;
    private String[] pathList;
    private int rotation;
    private SensorManager sensorManager;
    private Sensor sensorAccel;
    private Sensor sensorMagnet;
    private Timer timer;
    private ImageView someImage;
    private ImageView line;
    private Button appearanceScore;
    static int appearance = 0;
    static String paths = "";


    @Override
    public void onStart() {
        super.onStart();

        TextView title = getActivity().findViewById(R.id.title);
        title.setText(R.string.score_of_appearance);
        PageIndicatorView pageIndicatorView = getActivity().findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setSelection(position);
        Button btnNext = getActivity().findViewById(R.id.btnNext);
        btnNext.setText(R.string.next);

        paint();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rowView = inflater.inflate(R.layout.fragment_photo, container, false);
        someImage = rowView.findViewById(R.id.someImage);

        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE

        }, 1);

        final SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(rowView.getContext());

        if (!pm.getBoolean("didShowBunPrompt", false)) {
            new MaterialTapTargetPrompt.Builder(getActivity())
                    .setTarget(rowView.findViewById(R.id.appearance))
                    .setPrimaryText(R.string.did_show_bun)
                    //  .setPromptBackground(new RectanglePromptBackground())
                    // .setPromptFocal(new RectanglePromptFocal())
                    .setFocalRadius(500f)
                    .setBackgroundColour(Color.parseColor("#F2ed7c02"))
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
                                editor.putBoolean("didShowBunPrompt", true);
                                editor.apply();
                            }
                        }
                    })
                    .show();
        }

        final Button btnNext = getActivity().findViewById(R.id.btnNext);

        final TextView appearanceTextView = rowView.findViewById(R.id.appearance);
        appearanceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(getString(R.string.appearance), getString(R.string.appearance_info));
            }
        });

        line = rowView.findViewById(R.id.img_appearance);

        if (appearance > 0) {
            btnNext.setBackgroundColor(getResources().getColor(R.color.bright_orange));
        } else {
            btnNext.setBackgroundColor(getResources().getColor(R.color.empty_btn));
        }

        final Animation animButton = AnimationUtils.loadAnimation(rowView.getContext(), R.anim.animation_button);
        createDirectory();

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        final ImageButton shotBtn = rowView.findViewById(R.id.photo);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (shotBtn.isPressed()) {
                    shotBtn.startAnimation(animButton);
                }
                handler.postDelayed(this, 1);
            }
        });

        appearanceScore = rowView.findViewById(R.id.appearance_score);
        Button appearanceMinus = rowView.findViewById(R.id.appearance_minus);
        Button appearancePlus = rowView.findViewById(R.id.appearance_plus);

        shotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotBtn.startAnimation(animButton);
                //if (ContextCompat.checkSelfPermission(rowView.getContext(), Manifest.permission.CAMERA)
                //      == PackageManager.PERMISSION_GRANTED) {
                doPhoto();
                // Permission is not granted
                // }


            }
        });


        View takePhoto = rowView.findViewById(R.id.take_photo);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhoto();
            }
        });

        appearanceMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appearance > 0) {
                    appearance--;
                    paint();
                    appearanceScore.setText(String.valueOf(appearance));
                    if (appearance == 0) {
                        btnNext.setBackgroundColor(getResources().getColor(R.color.light_orange));
                        appearanceScore.setTextColor(getResources().getColor(R.color.grey));
                    }
                }
            }
        });
        appearancePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appearance < 10) {
                    appearance++;
                    paint();
                    btnNext.setBackgroundColor(getResources().getColor(R.color.bright_orange));
                    appearanceScore.setText(String.valueOf(appearance));
                    appearanceScore.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        someImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhoto();
            }
        });


        mPager = rowView.findViewById(R.id.need_stack_view);
        mPager.initStack(1);


        appearanceScore.setText(String.valueOf(appearance));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appearance != 0) {
                    position++;
                    Fragment fragment = new ScrollViewFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.place_container, fragment, "visible_fragment");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.not_right_marks), Toast.LENGTH_SHORT).show();

                    final Handler handler = new Handler();

                    color = false;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!color) {
                                appearanceScore.setTextColor(Color.RED);
                                appearanceTextView.setTextColor(Color.RED);
                                line.setBackgroundColor(Color.RED);
                                color = true;
                                handler.postDelayed(this, 1000);
                            } else {
                                if (appearance == 0) {
                                    appearanceScore.setTextColor(getResources().getColor(R.color.grey));
                                    line.setBackgroundColor(getResources().getColor(R.color.grey));
                                }
                                appearanceTextView.setTextColor(getResources().getColor(R.color.grey));
                            }
                        }

                    };
                    handler.post(runnable);

                }
            }
        });

        return rowView;
    }

    private void photo() {

        if (camera==null) {
            camera = android.hardware.Camera.open(0);
        }

        LayoutInflater li = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View  promptsView = li.inflate(R.layout.preview, null);

        SurfaceView preview;
        preview = promptsView.findViewById(R.id.SurfaceView02);
        SurfaceHolder surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

        mDialogBuilder.setView(promptsView);

        mDialogBuilder
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {

                            camera.setPreviewCallback(null);
                            camera.stopPreview();
                            camera.release();
                            camera = null;

                            dialog.cancel();

                            return true;
                        }
                        return false;
                    }
                });

        final AlertDialog alertDialog = mDialogBuilder.create();

        Log.d("Need_This", "6");

        ImageButton imageButton = promptsView.findViewById(R.id.do_photo);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera) {
                        try {
                            Bitmap bitmap;
                            generateFileUri();
                            pathList = paths.split("&");
                            if (pathList.length == 1) {
                                FileOutputStream os = new FileOutputStream(paths);
                                os.write(paramArrayOfByte);
                                os.close();
                                bitmap = decodeURI(paths);
                                change(bitmap, paths);
                                someImage.setVisibility(View.INVISIBLE);
                            } else if (pathList.length == 2) {
                                FileOutputStream os = new FileOutputStream(pathList[1]);
                                os.write(paramArrayOfByte);
                                os.close();

                                bitmap = decodeURI(pathList[1]);
                                change(bitmap, pathList[1]);
                            } else {
                                FileOutputStream os = new FileOutputStream(pathList[2]);
                                os.write(paramArrayOfByte);
                                os.close();
                                bitmap = decodeURI(pathList[2]);
                                change(bitmap, pathList[2]);
                            }
                            setImage();
                            alertDialog.cancel();


                            camera.setPreviewCallback(null);
                            camera.stopPreview();
                            camera.release();
                            camera = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        alertDialog.show();

        alertDialog.onBackPressed();


    }

    private void doPhoto() {
       // photo();
       boolean isAccess = false;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            createDirectory();
            photo();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE

            }, 1);

        }
    }


    public Bitmap decodeURI(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight) >= Math.abs(options.outWidth);
        if (options.outHeight * options.outWidth * 2 >= 16384) {
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight / 500
                    : options.outWidth / 500;
            options.inSampleSize =
                    (int) Math.pow(2d, Math.floor(
                            Math.log(sampleSize) / Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];

        return BitmapFactory.decodeFile(filePath, options);
    }


    public void up() {

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), createViewPagerFragments());
        mPager.setAdapter(mPagerAdapter);
    }

    public void change(Bitmap bitmap, String myPath) {

        bitmap = getCorrectlyOrientedImage(bitmap);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] byteArray = os.toByteArray();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(myPath));
            fileOutputStream.write(byteArray);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(listener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, sensorMagnet, SensorManager.SENSOR_DELAY_NORMAL);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDeviceOrientation();
                        getActualDeviceOrientation();
                    }
                });
            }
        };
        timer.schedule(task, 0, 400);

        WindowManager windowManager = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE));
        assert windowManager != null;
        Display display = windowManager.getDefaultDisplay();
        rotation = display.getRotation();

        if (!paths.equals("")) {
            someImage.setVisibility(View.INVISIBLE);
            parsingOfString();
            numPages = pathList.length;
            up();

        } else {
            someImage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        sensorManager.unregisterListener(listener);
        timer.cancel();

    }

    private void setImage() {
        if (pathList.length == 1) {
            if (directory == null) {
                directory = new File(pathList[0]);
            }
            numPages = 1;
            up();
        } else if (pathList.length == 2) {
            numPages = 2;
            up();
        } else {
            numPages = 3;
            up();
        }

        if (pathList.length == 4) {
            paths = paths.substring(0, paths.lastIndexOf("&"));
        }
    }

    private void parsingOfString() {
        if (paths.contains("&")) {
            pathList = paths.split("&");
        } else {
            pathList = new String[]{paths};
        }
    }

    private Uri generateFileUri() {
        long a = System.currentTimeMillis();
        if (paths.equals("")) {
            lastFile = directory.getPath() + "/" + "photo_"
                    + a + ".jpg";
            paths = lastFile;
            file = new File(paths);
            parsingOfString();
        } else if (pathList.length == 1) {
            lastFile = directory.getPath() + "/" + "photo_"
                    + a + ".jpg";
            paths += "&";
            paths += lastFile;
            parsingOfString();
            file = new File(pathList[1]);
        } else {
            lastFile = directory.getPath() + "/" + "photo_"
                    + a + ".jpg";
            paths += "&";
            paths += lastFile;
            parsingOfString();
            file = new File(pathList[2]);
        }

        return Uri.fromFile(file);
    }

    private void createDirectory() {
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "SHERI");
        if (!directory.exists())
            directory.mkdirs();
    }

    private ArrayList<Fragment> createViewPagerFragments() {

        ArrayList<Fragment> pagerFragments = new ArrayList<>();

        for (int i = 0; i < numPages; i++) {
            pagerFragments.add(ImageFragment.getInstance(i));
        }

        return pagerFragments;
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> pagerFragments;

        ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<Fragment> pagerFragments) {
            super(fm);
            this.pagerFragments = pagerFragments;
        }

        @Override
        public Fragment getItem(int position) {

            return pagerFragments.get(position);
        }

        @Override
        public int getCount() {
            return numPages;
            // return numPages;
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(0);
        } else {
            camera.setDisplayOrientation(90);
        }
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void onPictureTaken(byte[] paramArrayOfByte, android.hardware.Camera paramCamera) {

        try {
            FileOutputStream os = new FileOutputStream(lastFile);
            os.write(paramArrayOfByte);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        paramCamera.startPreview();
    }

    @Override
    public void onAutoFocus(boolean paramBoolean, android.hardware.Camera paramCamera) {
        if (paramBoolean) {
            paramCamera.takePicture(null, null, null, this);
        }
    }

    @Override
    public void onPreviewFrame(byte[] paramArrayOfByte, android.hardware.Camera paramCamera) {
        // здесь можно обрабатывать изображение, показываемое в preview
    }


    public Bitmap getCorrectlyOrientedImage(Bitmap bitmap) {

        Orient orient = format();
        int orientation = 90;


        if (orient.getB() > -50 && orient.getB() < 50 && orient.getC() > 0) {
            orientation = 180;
        }

        if (orient.getB() > -50 && orient.getB() < 50 && orient.getC() < 0) {
            orientation = 0;
        }


        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    Orient getDeviceOrientation() {
        SensorManager.getRotationMatrix(r, null, valuesAccel, valuesMagnet);
        SensorManager.getOrientation(r, valuesResult);

        return new Orient((float) Math.toDegrees(valuesResult[0]), (float) Math.toDegrees(valuesResult[1]), (float) Math.toDegrees(valuesResult[2]));
    }

    private class Orient {
        float a;
        float b;
        float c;

        Orient(float a, float b, float c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        private float getA() {
            return a;
        }

        private float getB() {
            return b;
        }

        private float getC() {
            return c;
        }

    }

    float[] valuesAccel = new float[3];
    float[] valuesMagnet = new float[3];
    float[] valuesResult = new float[3];
    float[] valuesResult2 = new float[3];
    float[] inR = new float[9];
    float[] outR = new float[9];
    float[] r = new float[9];

    void getActualDeviceOrientation() {
        SensorManager.getRotationMatrix(inR, null, valuesAccel, valuesMagnet);
        int xAxis = SensorManager.AXIS_X;
        int yAxis = SensorManager.AXIS_Y;
        switch (rotation) {
            case (Surface.ROTATION_0):
                break;
            case (Surface.ROTATION_90):
                xAxis = SensorManager.AXIS_Y;
                yAxis = SensorManager.AXIS_MINUS_X;
                break;
            case (Surface.ROTATION_180):
                yAxis = SensorManager.AXIS_MINUS_Y;
                break;
            case (Surface.ROTATION_270):
                xAxis = SensorManager.AXIS_MINUS_Y;
                yAxis = SensorManager.AXIS_X;
                break;
            default:
                break;
        }
        SensorManager.remapCoordinateSystem(inR, xAxis, yAxis, outR);
        SensorManager.getOrientation(outR, valuesResult2);
        valuesResult2[0] = (float) Math.toDegrees(valuesResult2[0]);
        valuesResult2[1] = (float) Math.toDegrees(valuesResult2[1]);
        valuesResult2[2] = (float) Math.toDegrees(valuesResult2[2]);
    }

    Orient format() {
        return new Orient(valuesResult2[0], valuesResult2[1], valuesResult2[2]);
    }

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(event.values, 0, valuesAccel, 0, 3);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    System.arraycopy(event.values, 0, valuesMagnet, 0, 3);
                    break;
            }
        }
    };

    private void dialog(String title, String info) {
        @SuppressLint("InflateParams") View alertDialog = getLayoutInflater().inflate(R.layout.dialog_info_mark, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

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

    private void paint() {
        if (appearance > 0) {
            line.setBackgroundColor(getResources().getColor(R.color.light_orange));
            appearanceScore.setTextColor(getResources().getColor(R.color.black));
        } else {
            line.setBackgroundColor(getResources().getColor(R.color.grey));
            appearanceScore.setTextColor(getResources().getColor(R.color.grey));
        }
    }

}
