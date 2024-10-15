package com.pegalite.fivegonly;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.pegalite.fivegonly.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, initializationStatus -> {
        });

        /* For Window Color Adjustments */
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        binding.knowMore.setOnClickListener(view -> new AlertDialog.Builder(this).setTitle("5G Only!").setMessage("Note for Airtel Users: If you're using NR only (5G-only) mode, please be aware that Airtel's 5G network operates on a Non-Standalone (NSA) architecture, which requires a 4G connection for signaling. Forcing 5G-only mode may cause connectivity issues, such as an inability to make calls or use data. We recommend using 5G Auto mode to ensure smooth network performance with Airtel.").setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss()).show());

        loadAd();

        new Handler().postDelayed(() -> {
            initMain();
            if (mInterstitialAd == null) {
                binding.mainContainer.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

            }
        }, 5000);

    }

    boolean isLoaded;

    private void initMain() {
        if (isLoaded) {
            return;
        }
        isLoaded = true;
        new AlertDialog.Builder(this).setTitle("5G Only!").setMessage("Make sure your device supports 5G networks for 5G Only to work properly.").setPositiveButton("Confirm", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            binding.set5GOnly.setOnClickListener(view -> {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(this);
                } else {
                    openPhoneInfo();
                }
            });
        }).setNegativeButton("Exit", (dialogInterface, i) -> finish()).setCancelable(false).show();

    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.ad_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                initMain();
                binding.mainContainer.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                mInterstitialAd = interstitialAd;
                setupAdCallbacks();
                Log.i(TAG, "Ad loaded successfully");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, "Ad failed to load: " + loadAdError);
                mInterstitialAd = null;
            }
        });
    }

    private void setupAdCallbacks() {
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "Ad clicked");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                openPhoneInfo();
                Log.d(TAG, "Ad dismissed");
                mInterstitialAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.e(TAG, "Ad failed to show: " + adError);
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                Log.d(TAG, "Ad impression recorded");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad shown");
            }
        });
    }

    private void openPhoneInfo() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            if (Build.VERSION.SDK_INT >= 30) {
                intent.setClassName("com.android.phone", "com.android.phone.settings.RadioInfo");
            } else {
                intent.setClassName("com.android.settings", "com.android.settings.RadioInfo");
            }
            startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(android.net.Uri.parse("tel:*#*#4636#*#*"));
                startActivity(intent);
            } catch (Exception e2) {
                try {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivity(intent);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }
}