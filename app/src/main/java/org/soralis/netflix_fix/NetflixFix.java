package org.soralis.netflix_fix;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class NetflixFix implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    private static final String TAG = "NetflixFix";
    private static final String PREFS_NAME = "NetflixFixPrefs";
    private static final String PREF_MODEL = "device_model";
    private static final String PREF_PRODUCT = "device_product";
    private static final String PREF_BRAND = "device_brand";
    private static final String PREF_MANUFACTURER = "device_manufacturer";
    private static final String PREF_DEVICE = "device_device";
    private static final String PREF_HARDWARE = "device_hardware";
    private static final String PREF_FINGERPRINT = "device_fingerprint";
    private static final String PREF_BUILD_ID = "device_build_id";
    private static final String PREF_TYPE = "device_type";
    private static final String PREF_TAGS = "device_tags";

    public static final String DEFAULT_MODEL = "Pixel 10 Pro XL";
    public static final String DEFAULT_PRODUCT = "mustang";
    public static final String DEFAULT_BRAND = "google";
    public static final String DEFAULT_MANUFACTURER = "Google";
    public static final String DEFAULT_DEVICE = "mustang";
    public static final String DEFAULT_HARDWARE = "mustang";
    public static final String DEFAULT_FINGERPRINT = "google/mustang/mustang:16/BD3A.251105.010.E1/14337626:user/release-keys";
    public static final String DEFAULT_BUILD_ID = "BD3A.251105.010.E1";
    public static final String DEFAULT_TYPE = "user";
    public static final String DEFAULT_TAGS = "release-keys";

    private static final Set<String> TARGET_PACKAGES = new HashSet<>(Arrays.asList(
            "com.netflix.mediaclient"
    ));

    private static final Map<String, Object> GENERIC_PROPS = new HashMap<>();
    private static final Map<String, Object> PIXEL_PROPS = new HashMap<>();

    static {
        GENERIC_PROPS.put("TYPE", DEFAULT_TYPE);
        GENERIC_PROPS.put("TAGS", DEFAULT_TAGS);

        PIXEL_PROPS.put("BRAND", DEFAULT_BRAND);
        PIXEL_PROPS.put("MANUFACTURER", DEFAULT_MANUFACTURER);
        PIXEL_PROPS.put("DEVICE", DEFAULT_DEVICE);
        PIXEL_PROPS.put("PRODUCT", DEFAULT_PRODUCT);
        PIXEL_PROPS.put("HARDWARE", DEFAULT_HARDWARE);
        PIXEL_PROPS.put("MODEL", DEFAULT_MODEL);
        PIXEL_PROPS.put("ID", DEFAULT_BUILD_ID);
        PIXEL_PROPS.put("FINGERPRINT", DEFAULT_FINGERPRINT);
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        // no-op
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        final String packageName = lpparam.packageName;

        XposedBridge.log("[" + TAG + "] handleLoadPackage: " + packageName);

        if (packageName == null || !TARGET_PACKAGES.contains(packageName)) {
            return;
        }

        XposedBridge.log("[" + TAG + "] Applying Pixel props to " + packageName);

        Context context = getApplicationContext(lpparam.classLoader);
        Map<String, Object> propsToApply = new HashMap<>();
        propsToApply.putAll(GENERIC_PROPS);
        propsToApply.putAll(PIXEL_PROPS);

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String model = prefs.getString(PREF_MODEL, DEFAULT_MODEL);
            String product = prefs.getString(PREF_PRODUCT, DEFAULT_PRODUCT);
            String brand = prefs.getString(PREF_BRAND, DEFAULT_BRAND);
            String manufacturer = prefs.getString(PREF_MANUFACTURER, DEFAULT_MANUFACTURER);
            String device = prefs.getString(PREF_DEVICE, DEFAULT_DEVICE);
            String hardware = prefs.getString(PREF_HARDWARE, DEFAULT_HARDWARE);
            String fingerprint = prefs.getString(PREF_FINGERPRINT, DEFAULT_FINGERPRINT);
            String buildId = prefs.getString(PREF_BUILD_ID, DEFAULT_BUILD_ID);
            String type = prefs.getString(PREF_TYPE, DEFAULT_TYPE);
            String tags = prefs.getString(PREF_TAGS, DEFAULT_TAGS);

            propsToApply.put("MODEL", model);
            propsToApply.put("PRODUCT", product);
            propsToApply.put("BRAND", brand);
            propsToApply.put("MANUFACTURER", manufacturer);
            propsToApply.put("DEVICE", device);
            propsToApply.put("HARDWARE", hardware);
            propsToApply.put("FINGERPRINT", fingerprint);
            propsToApply.put("ID", buildId);
            propsToApply.put("TYPE", type);
            propsToApply.put("TAGS", tags);

            XposedBridge.log("[" + TAG + "] Using settings - Model: " + model + ", Product: " + product);
        } else {
            XposedBridge.log("[" + TAG + "] Context not available, using defaults");
        }

        applyProps(propsToApply);
    }

    private Context getApplicationContext(ClassLoader classLoader) {
        try {
            Object application = XposedHelpers.callStaticMethod(
                    XposedHelpers.findClass("android.app.ActivityThread", classLoader),
                    "currentApplication"
            );
            return (Context) XposedHelpers.callMethod(application, "getApplicationContext");
        } catch (Throwable t) {
            XposedBridge.log("[" + TAG + "] Unable to get application context: " + t.getMessage());
            return null;
        }
    }

    private void applyProps(Map<String, Object> props) {
        for (Map.Entry<String, Object> entry : props.entrySet()) {
            setPropValue(entry.getKey(), entry.getValue());
        }
    }

    private void setPropValue(String key, Object value) {
        try {
            Class<?> clazz = Build.class;
            if (key.startsWith("VERSION.")) {
                clazz = Build.VERSION.class;
                key = key.substring(8);
            }
            Field field = clazz.getDeclaredField(key);
            field.setAccessible(true);
            if (field.getType().equals(Integer.TYPE)) {
                field.set(null, Integer.parseInt(String.valueOf(value)));
            } else if (field.getType().equals(Long.TYPE)) {
                field.set(null, Long.parseLong(String.valueOf(value)));
            } else {
                field.set(null, value);
            }
            field.setAccessible(false);
        } catch (Exception e) {
            Log.e(TAG, "Failed to set prop " + key, e);
        }
    }
}
