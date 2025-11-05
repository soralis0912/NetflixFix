package org.soralis.netflix_fix;



import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * @author Jecelyin
 */
public class NetflixFix implements IXposedHookZygoteInit,
        IXposedHookLoadPackage {

    private static final String PREFS_NAME = "NetflixFixPrefs";
    private static final String PREF_MODEL = "device_model";
    private static final String PREF_PRODUCT = "device_product";

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam)throws Throwable {

        XposedBridge.log("[NetflixFix]");
        
        // デフォルト値
        String defaultModel = "Mi 10";
        String defaultProduct = "umi";
        
        String model = defaultModel;
        String product = defaultProduct;
        
        // 設定値を読み込み（可能な場合）
        try {
            Context context = (Context) XposedHelpers.callMethod(
                XposedHelpers.callStaticMethod(
                    XposedHelpers.findClass("android.app.ActivityThread", lpparam.classLoader),
                    "currentApplication"
                ), 
                "getApplicationContext"
            );
            
            if (context != null) {
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                model = prefs.getString(PREF_MODEL, defaultModel);
                product = prefs.getString(PREF_PRODUCT, defaultProduct);
                XposedBridge.log("[NetflixFix] Using custom settings - Model: " + model + ", Product: " + product);
            } else {
                XposedBridge.log("[NetflixFix] Context not available, using default values");
            }
        } catch (Exception e) {
            XposedBridge.log("[NetflixFix] Error loading settings, using defaults: " + e.getMessage());
        }
        
        XposedHelpers.findField(Build.class, "MODEL").set(null, model);
        XposedHelpers.findField(Build.class, "PRODUCT").set(null, product);
  
    }



}
