package com.pegalite.fivegonly;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pegalite.fivegonly.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* For Window Color Adjustments */
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        new AlertDialog.Builder(this).setTitle("5G Only!").setMessage("Make sure your device supports 5G networks for 5G Only to work properly.").setPositiveButton("Confirm", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            binding.set5GOnly.setOnClickListener(view -> openPhoneInfo());
        }).setNegativeButton("Exit", (dialogInterface, i) -> finish()).setCancelable(false).show();

        binding.knowMore.setOnClickListener(view -> new AlertDialog.Builder(this).setTitle("5G Only!").setMessage("Note for Airtel Users: If you're using NR only (5G-only) mode, please be aware that Airtel's 5G network operates on a Non-Standalone (NSA) architecture, which requires a 4G connection for signaling. Forcing 5G-only mode may cause connectivity issues, such as an inability to make calls or use data. We recommend using 5G Auto mode to ensure smooth network performance with Airtel.").setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss()).show());
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