package com.edward.wallpaperengine;

import android.content.Context;
import android.hardware.Camera;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.content.SharedPreferences;

import java.io.IOException;

public class CameraWallpaper extends WallpaperService {

    SharedPreferences sharedPref;
    int currCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
    boolean switchInProgress = false;

    public Engine onCreateEngine() {
        return new CameraEngine();
    }

    class CameraEngine extends Engine implements Camera.PreviewCallback {
        private Camera camera;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            sharedPref = getApplicationContext().getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);

            startPreview();
            setTouchEventsEnabled(true);

            switchPreview();

        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopPreview();
            stopSelf();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                startPreview();
            } else {
                stopPreview();
            }
        }

        public void startPreview() {
            if (!switchInProgress) {
                stopPreview();
                camera = Camera.open(currCamera);
                camera.setDisplayOrientation(90);

                try {
                    camera.setPreviewDisplay(getSurfaceHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            }

        }

        public void stopPreview() {
            if (camera != null) {
                try {
                    camera.stopPreview();
                    camera.setPreviewCallback(null);
                    camera.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                camera = null;
            }
        }

        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            camera.addCallbackBuffer(bytes);
        }

        public void switchPreview() {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (!switchInProgress) {
                            if ((sharedPref.getBoolean("isBackCamera", true)) && (currCamera == Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                                switchInProgress = true;
                                stopPreview();
                                currCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
                                switchInProgress = false;
                                startPreview();

                            } else if ((!sharedPref.getBoolean("isBackCamera", true)) && (currCamera == Camera.CameraInfo.CAMERA_FACING_BACK)) {
                                switchInProgress = true;
                                stopPreview();
                                currCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
                                switchInProgress = false;
                                startPreview();
                            }
                        }
                    }
                }
            }).start();
        }

    }

}
