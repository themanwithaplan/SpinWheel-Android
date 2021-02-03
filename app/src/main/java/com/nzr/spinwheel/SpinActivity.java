package com.nzr.spinwheel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.OkHttpClient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adefruandta.spinningwheel.SpinningWheelView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class SpinActivity extends AppCompatActivity implements SpinningWheelView.OnRotationListener<String> {

    private SpinningWheelView wheelView;
    private Button spinBtn;
    RewardModel[] rewards;
    ArrayList<String> prizes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getData();

    }

    public void updateWheel(){
        wheelView = (SpinningWheelView) findViewById(R.id.wheel);

        spinBtn = (Button) findViewById(R.id.spinBtn);

        wheelView.setItems(prizes);
        wheelView.setEnabled(false);
        wheelView.setOnRotationListener(this);

        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheelView.rotate(50, 3000, 50);
            }
        });
    }

    @Override
    public void onRotation() {

    }

    @Override
    public void onStopRotation(String item) {
        Toast toast= Toast.makeText(getApplicationContext(),
                item, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }

    public void getData() {
        AndroidNetworking.get("http://mockbin.org/bin/84a8a8ed-b3d9-43e0-a6d8-d04ddc311efd")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        rewards = gson.fromJson(String.valueOf(response), RewardModel[].class);
                        for (RewardModel reward : rewards) {
                            prizes.add(reward.getDisplayText());
                        }
                        updateWheel();
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("error", "Error while getting Data: "+error);
                    }
                });
    }
}
