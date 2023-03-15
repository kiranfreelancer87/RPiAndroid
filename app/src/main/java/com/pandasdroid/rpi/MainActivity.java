package com.pandasdroid.rpi;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.pandasdroid.rpi.databinding.ActivityMainBinding;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    //d7TC87wfTnujvQHDj870Hb:APA91bFtx5VT1-OYS1w-O4cHuY-NjbNuwZb_fAXeX6L7dBnZZQgsEMTIMT6KtUDR3tJ7LE6AOTqlLaBzSVo_6HTvCpHcaf_dAqX1jmJaFcVGo2mPH6hFrQKtjol7j8wiRIC7fwFeJCqj

    private int count = 0;
    private String booth = "2";
    private String topic = "watch2";
    //726792324
    //L0l@2199o


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //eyOnApGhQeCLvAevS-OAKP:APA91bHczpT-3lBTMqza4U79kV6M2Hj6rEqQ86NPfBwC5U3osPhf-fB6wfv2j5AC0jsWX0cmow3f0Gd663RXghxH3ah232dkjqZacFemfeaMxWYN8eukCD3wqsTFHwJZG-jFG7st1cLZ

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn.setOnClickListener((view -> {
            if (count == 0) {
                count = 1;
            } else {
                count = 0;
            }
            binding.tv.setText(String.valueOf(count));
            Log.wtf("Count", "" + count);

        }));

        UpdateStatus("green");
        binding.tv.setText("Order Received");
        binding.btn.setText("Complete Order");
        binding.btn.setOnClickListener((view) -> {
            UpdateStatus("red");
        });


        findViewById(R.id.iv_setting).setOnClickListener((view) -> {
            Settings();
        });
    }

    public void UpdateStatus(String status) {
        booth = getSharedPreferences("Spref", Context.MODE_PRIVATE).getString("booth", "");
        if (booth.equals("")) {
            Toast.makeText(this, "Please select booth", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.wtf("Status", status);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = api.UpdateStatus(status, booth);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) {
                    toast("Something Went wrong!");
                } else {
                    try {
                        String str = response.body().string();
                        Log.wtf("Str", str);
                        toast(str);
                    } catch (Exception e) {
                        toast(e.getMessage());
                        Log.wtf("Str", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.wtf("Str", t.getMessage());
                toast(t.getMessage());
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    public void Settings() {
        Log.wtf("Ivsetting", "Activity");
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
