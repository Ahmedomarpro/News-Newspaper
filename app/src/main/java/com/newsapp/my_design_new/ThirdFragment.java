package com.newsapp.my_design_new;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class ThirdFragment extends Fragment  {

    private InterstitialAd mInterstitialAd;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_third_fragment,container,false);



        prepareAd();


        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                Log.i("hello", "world");
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG"," Interstitial not loaded");
                            prepareAd();

                        }




                    }
                });

            }
        },2,3, TimeUnit.SECONDS);




        TextView textView = (TextView)view.findViewById(R.id.textView2);

        Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "SF-Khaled-Bold.otf");
        textView.setTypeface(typeface);


        return view;
    }

    public void  prepareAd(){


        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-6915160267704063/6527707808");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    };

}
