package com.edward.wallpaperengine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;
import android.view.TextureView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;

import com.example.android.basicrenderscript.ScriptC_saturation;

import java.io.IOException;

import android.hardware.Camera;
import android.widget.FrameLayout;
import android.app.WallpaperManager;

public class MainActivity extends AppCompatActivity /*implements TextureView.SurfaceTextureListener */ {

    /**
     * Number of bitmaps that is used for RenderScript thread and UI thread synchronization.
     */

    private TextureView mTextureView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Camera mCamera;
    private CameraPreview mPreview;
    Bitmap b;
    //private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // Initialize UI
        /*
        mBitmapIn = loadBitmap(R.drawable.data);
        mBitmapsOut = new Bitmap[NUM_BITMAPS];
        for (int i = 0; i < NUM_BITMAPS; ++i) {
            mBitmapsOut[i] = Bitmap.createBitmap(mBitmapIn.getWidth(), mBitmapIn.getHeight(), mBitmapIn.getConfig());
        }
*/
        //mImageView = findViewById(R.id.imageView);
        //mImageView.setImageBitmap(mBitmapsOut[mCurrentBitmap]);
        //mCurrentBitmap += (mCurrentBitmap + 1) % NUM_BITMAPS;

        // Create renderScript
        //createScript();

        // Invoke renderScript kernel and update imageView
        //updateImage(1.0f);
        //startEngine();

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }

        mCamera = getCameraInstance();

        //mTextureView = new TextureView(this);
        //mTextureView.setSurfaceTextureListener(this);

        //setContentView(mTextureView);

        //startEngine();

        mPreview = new CameraPreview(getApplicationContext(), mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        //startEngine();

    }


    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mCamera = Camera.open();

                try {
                    mCamera.setPreviewTexture(surface);
                    mCamera.startPreview();
                } catch (IOException ioe) {

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    private void startEngine() {
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                while (true) {
                    System.out.println("hi");
                    b = mTextureView.getBitmap();
                    try {
                        myWallpaperManager.setBitmap(b);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                */
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        b = mTextureView.getBitmap();
        try {
            myWallpaperManager.setBitmap(b);
        } catch (IOException e) {
            e.printStackTrace();
        }/*
            }
        }).start();*/
    }


}
