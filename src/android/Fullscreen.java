//  cordova-plugin-fullscreen
//  Copyright © 2016 filfat Studios AB
//  Repo: https://github.com/filfat-Studios-AB/cordova-plugin-fullscreen
//
//  The following code is
//  based on https://github.com/toluhta/Immersify/blob/master/src/android/ImmersiveMode.java
package com.filfatstudios.fullscreen;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class Fullscreen extends CordovaPlugin {
    Window window;
    View decorView;
    private String TAG = Fullscreen.class.getSimpleName();

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        this.window = cordova.getActivity().getWindow();
        this.decorView = window.getDecorView();
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("on")) {
            return fullscreen();
        } else if (action.equals("off")) {
            return fullScreenOff();
        }
        callbackContext.error("Error: Unknown action!");
        return false;
    }

    //Workaround for BT300 - decorView.setOnSystemUiVisibilityChangeListener doesn't trigger...
    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

        int uiOptions = decorView.getSystemUiVisibility();
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i(TAG, "immersive mode mode on, forcing fullscreen");
            fullscreen();
        } else {
            Log.i(TAG, "immersive mode off, leaving decor as is");
        }
    }

    private boolean fullscreen() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                resetWindow();

                final int uiOptions =
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                decorView.setSystemUiVisibility(uiOptions);

                decorView.setOnFocusChangeListener(new View.OnFocusChangeListener()

                {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            decorView.setSystemUiVisibility(uiOptions);
                        }
                    }
                });

                decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()

                {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                });
            }
        });
        return true;
    }

    private boolean fullScreenOff() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resetWindow();

                // Remove translucent theme from bars

                window.clearFlags
                        (
                                WindowManager.LayoutParams.FLAG_FULLSCREEN
                                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        );

                // Update system UI

                decorView.setOnSystemUiVisibilityChangeListener(null);
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
        return true;
    }

    private void resetWindow() {
        decorView.setOnFocusChangeListener(null);
        decorView.setOnSystemUiVisibilityChangeListener(null);

        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

}