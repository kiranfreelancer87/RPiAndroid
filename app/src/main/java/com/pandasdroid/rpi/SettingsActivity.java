package com.pandasdroid.rpi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class SettingsActivity extends Activity {

    TextView booth_list;
    AppCompatSpinner spinner;
    Button btn;
    String booth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        booth = getSharedPreferences("Spref", Context.MODE_PRIVATE).getString("booth", "");

        booth_list = findViewById(R.id.booth_list);
        spinner = findViewById(R.id.spinner);
        btn = findViewById(R.id.btn);

        ArrayList<String> list = new ArrayList<String>();
        list.add("Select Booth");
        for (int i = 1; i <= 18; i++) {
            list.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spinner.setAdapter(adapter);

        try {
            int selected_booth = Integer.parseInt(booth);
            spinner.setSelection(selected_booth, true);
            booth_list.setText("Selected Booth: " + spinner.getSelectedItem().toString());
        } catch (Exception e){
            e.printStackTrace();
        }

        btn.setOnClickListener((view) -> {
            btn.setText("Processing...");
            btn.setEnabled(false);

            if (!booth.equals("")) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(booth).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseMessaging.getInstance().subscribeToTopic(spinner.getSelectedItem().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getSharedPreferences("Spref", Context.MODE_PRIVATE).edit().putString("booth", spinner.getSelectedItem().toString()).apply();
                                    booth = spinner.getSelectedItem().toString();
                                    btn.setText("Success");
                                    booth_list.setText("Selected Booth: " + spinner.getSelectedItem().toString());
                                    btn.setEnabled(true);
                                }
                            });
                        } else {
                            btn.setText("Submit");
                            btn.setEnabled(true);
                            Toast.makeText(SettingsActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                FirebaseMessaging.getInstance().subscribeToTopic(spinner.getSelectedItem().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getSharedPreferences("Spref", Context.MODE_PRIVATE).edit().putString("booth", spinner.getSelectedItem().toString()).apply();
                        booth = spinner.getSelectedItem().toString();
                        btn.setText("Success");
                        booth_list.setText("Selected Booth: " + spinner.getSelectedItem().toString());
                        btn.setEnabled(true);
                    }
                });
            }
        });
    }
}