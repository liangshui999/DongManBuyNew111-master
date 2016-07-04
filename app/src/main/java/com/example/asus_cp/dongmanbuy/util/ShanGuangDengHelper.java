package com.example.asus_cp.dongmanbuy.util;


import android.hardware.Camera;

import java.util.List;

/**
 * 闪光灯的帮助类
 * Created by asus-cp on 2016-07-04.
 */
public class ShanGuangDengHelper {

    /**
     * 开启闪光灯
     * @param mCamera
     */
    public static void turnLightOn(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
            }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }


    /**
     * 关闭闪光灯
     * @param mCamera
     */
    public static void turnLightOff(Camera mCamera) {
        if (mCamera == null) {
            return;
            }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
         List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
         if (flashModes == null) {
            return;
             }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
             // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
                } else {
            }
        }
    }
}
