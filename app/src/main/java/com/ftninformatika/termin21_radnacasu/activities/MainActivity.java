package com.ftninformatika.termin21_radnacasu.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ftninformatika.termin21_radnacasu.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
   // logt loge psfs psfi

    TextView tvText;
    Button buttonStart;
    Button buttonCancel;
    Button button_ON;
    Button button_OFF;
    ProgressBar pBar;
    boolean pokrenuto = false;
    Handler handler = new Handler();
    ProgressDialog progressDialog;
    boolean direction = true;
    boolean started = false;



    private MyAsyncTask myTask = new MyAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Log.e(TAG, "onCreate: " + "Usli Smo");

        tvText = findViewById(R.id.text_View);
        buttonStart = findViewById(R.id.button_start);
        buttonCancel = findViewById(R.id.button_cancel);
        pBar = findViewById(R.id.pBar);
        button_ON = findViewById(R.id.button_ON);
        button_OFF = findViewById(R.id.button_OFF);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showProgressDialog();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               myTask.cancel(true);
            }
        });

        button_ON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direction = true;
                if (!started) {
                    started = true;
                    showProgressBar();
                }
            }
        });


        button_OFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showProgressBackwards();
                direction = false;
            }
        });

    }

    private void showProgressBar() {


       new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(100);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (direction) {
                                    pBar.incrementProgressBy(1);
                                    if (pBar.getProgress() >= pBar.getMax()){
                                        direction = false;
                                    }
                                } else {
                                    if (pBar.getProgress() <= 0){
                                        direction = true;
                                    } else {
                                        pBar.incrementProgressBy(-1);
                                    }
                                }
                                Log.e(TAG, "run: " + "Progress set to: " + pBar.getProgress());
                                tvText.setText("Progress: " + pBar.getProgress());
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }




    private void showProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Ucitavanje ...");
        progressDialog.setTitle("Primer Progres Dialoga");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progressDialog.getProgress() <= progressDialog.getMax()) {
                        Thread.sleep(200);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.incrementProgressBy(1);
                            }
                        });
                        if (progressDialog.getProgress() == progressDialog.getMax()) {
                            progressDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }





    private void startBackgroundTask() {

        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <  5;i++) {
                    try {
                        Thread.sleep(1000);
                        final String str = i + " s";
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvText.setText(str);
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        });
        myThread.start();
    }

    private class MyAsyncTask extends AsyncTask<Integer,Integer,Void> {



        @Override
        protected void onPreExecute() {

           pokrenuto = true;
           tvText.setText("Pocetak");

        }

        @Override
        protected Void doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0];i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i+1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            pokrenuto = false;
            tvText.setText("Uspesno");


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            tvText.setText("Proslo " + values[0] + " sekundi");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            tvText.setText("Zaustavljeno");
            pokrenuto = false;


        }
    }




}
