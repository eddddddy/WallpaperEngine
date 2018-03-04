package com.edward.wallpaperengine;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.content.SharedPreferences;

public class ShakeDetectService extends IntentService implements SensorEventListener {

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7f;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public ShakeDetectService() {
        super("ShakeDetectService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("i started");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST/*SensorManager.SENSOR_DELAY_UI*/);

        sharedPref = getApplicationContext().getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putBoolean("isBackCamera", true);
        editor.commit();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {

        System.out.println("hi");

        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        double gX = x / SensorManager.GRAVITY_EARTH;
        double gY = y / SensorManager.GRAVITY_EARTH;
        double gZ = z / SensorManager.GRAVITY_EARTH;

        double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            if (sharedPref.getBoolean("isBackCamera", true)) {
                editor.putBoolean("isBackCamera", false);
                editor.apply();
            } else {
                editor.putBoolean("isBackCamera", true);
                editor.apply();
            }
        }

    }

    @Override
    public void onDestroy() {

    }

}
