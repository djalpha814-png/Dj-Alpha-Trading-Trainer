package com.djalphatradingtrainer;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {

    private static final String CHANNEL_ID = "trading_signals";

    private TextView signalText;
    private TextView priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    100);
        }

        buildInterface();
    }

    private void buildInterface() {

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(30, 30, 30, 30);
        main.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("DJ ALPHA TRADING TRAINER");
        title.setTextSize(25);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 10, 0, 25);

        main.addView(title);

        TextView pairLabel = new TextView(this);
        pairLabel.setText("Trading pair");
        pairLabel.setTextSize(16);

        main.addView(pairLabel);

        Spinner pairSpinner = new Spinner(this);

        String[] pairs = {
                "XAUUSD",
                "BTCUSD"
        };

        ArrayAdapter<String> pairAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        pairs);

        pairSpinner.setAdapter(pairAdapter);

        main.addView(pairSpinner);

        TextView timeframe = new TextView(this);
        timeframe.setText("Timeframe: M5");
        timeframe.setTextSize(18);
        timeframe.setPadding(0, 25, 0, 20);

        main.addView(timeframe);

        priceText = new TextView(this);
        priceText.setText("Entry: ---");
        priceText.setTextSize(18);

        main.addView(priceText);

        TextView slText = new TextView(this);
        slText.setText("Stop Loss: ---");
        slText.setTextSize(18);
        slText.setPadding(0, 10, 0, 10);

        main.addView(slText);

        TextView tpText = new TextView(this);
        tpText.setText("Take Profit: ---");
        tpText.setTextSize(18);

        main.addView(tpText);

        signalText = new TextView(this);
        signalText.setText("WAITING FOR SIGNAL");
        signalText.setTextSize(23);
        signalText.setGravity(Gravity.CENTER);
        signalText.setPadding(0, 35, 0, 35);

        main.addView(signalText);

        Button buyButton = new Button(this);
        buyButton.setText("🟢 BUY SIGNAL");

        buyButton.setOnClickListener(v -> {

            signalText.setText("🟢 BUY");
            signalText.setTextColor(Color.rgb(0, 130, 0));

            priceText.setText("Entry: MARKET");
            slText.setText("Stop Loss: PRACTICE");
            tpText.setText("Take Profit: PRACTICE");

            sendNotification(
                    "BUY SIGNAL",
                    pairSpinner.getSelectedItem().toString()
                            + " M5 BUY signal");
        });

        main.addView(buyButton);

        Button sellButton = new Button(this);
        sellButton.setText("🔴 SELL SIGNAL");

        sellButton.setOnClickListener(v -> {

            signalText.setText("🔴 SELL");
            signalText.setTextColor(Color.RED);

            priceText.setText("Entry: MARKET");
            slText.setText("Stop Loss: PRACTICE");
            tpText.setText("Take Profit: PRACTICE");

            sendNotification(
                    "SELL SIGNAL",
                    pairSpinner.getSelectedItem().toString()
                            + " M5 SELL signal");
        });

        main.addView(sellButton);

        Button resetButton = new Button(this);
        resetButton.setText("RESET");

        resetButton.setOnClickListener(v -> {

            signalText.setText("WAITING FOR SIGNAL");
            signalText.setTextColor(Color.BLACK);

            priceText.setText("Entry: ---");
            slText.setText("Stop Loss: ---");
            tpText.setText("Take Profit: ---");
        });

        main.addView(resetButton);

        TextView warning = new TextView(this);
        warning.setText(
                "\nPractice mode only.\n" +
                "Signals are educational and are not financial advice."
        );

        warning.setTextSize(13);
        warning.setGravity(Gravity.CENTER);
        warning.setPadding(0, 25, 0, 0);

        main.addView(warning);

        setContentView(main);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "Trading Signals",
                            NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription(
                    "DJ Alpha BUY and SELL notifications");

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String title, String message) {

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE |
                                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager manager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(
                (int) System.currentTimeMillis(),
                builder.build());
    }
          }
