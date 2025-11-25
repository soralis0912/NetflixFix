package org.soralis.netflix_fix;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    
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
    
    private EditText modelEditText;
    private EditText productEditText;
    private EditText brandEditText;
    private EditText manufacturerEditText;
    private EditText deviceEditText;
    private EditText hardwareEditText;
    private EditText fingerprintEditText;
    private EditText buildIdEditText;
    private EditText typeEditText;
    private EditText tagsEditText;
    private Button saveButton;
    private Button resetButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initViews();
        loadSettings();
        setupListeners();
    }
    
    private void initViews() {
        modelEditText = findViewById(R.id.editTextModel);
        productEditText = findViewById(R.id.editTextProduct);
        brandEditText = findViewById(R.id.editTextBrand);
        manufacturerEditText = findViewById(R.id.editTextManufacturer);
        deviceEditText = findViewById(R.id.editTextDevice);
        hardwareEditText = findViewById(R.id.editTextHardware);
        fingerprintEditText = findViewById(R.id.editTextFingerprint);
        buildIdEditText = findViewById(R.id.editTextBuildId);
        typeEditText = findViewById(R.id.editTextType);
        tagsEditText = findViewById(R.id.editTextTags);
        saveButton = findViewById(R.id.buttonSave);
        resetButton = findViewById(R.id.buttonReset);
    }
    
    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String model = prefs.getString(PREF_MODEL, NetflixFix.DEFAULT_MODEL);
        String product = prefs.getString(PREF_PRODUCT, NetflixFix.DEFAULT_PRODUCT);
        String brand = prefs.getString(PREF_BRAND, NetflixFix.DEFAULT_BRAND);
        String manufacturer = prefs.getString(PREF_MANUFACTURER, NetflixFix.DEFAULT_MANUFACTURER);
        String device = prefs.getString(PREF_DEVICE, NetflixFix.DEFAULT_DEVICE);
        String hardware = prefs.getString(PREF_HARDWARE, NetflixFix.DEFAULT_HARDWARE);
        String fingerprint = prefs.getString(PREF_FINGERPRINT, NetflixFix.DEFAULT_FINGERPRINT);
        String buildId = prefs.getString(PREF_BUILD_ID, NetflixFix.DEFAULT_BUILD_ID);
        String type = prefs.getString(PREF_TYPE, NetflixFix.DEFAULT_TYPE);
        String tags = prefs.getString(PREF_TAGS, NetflixFix.DEFAULT_TAGS);
        
        modelEditText.setText(model);
        productEditText.setText(product);
        brandEditText.setText(brand);
        manufacturerEditText.setText(manufacturer);
        deviceEditText.setText(device);
        hardwareEditText.setText(hardware);
        fingerprintEditText.setText(fingerprint);
        buildIdEditText.setText(buildId);
        typeEditText.setText(type);
        tagsEditText.setText(tags);
    }
    
    private void setupListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
        
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSettings();
            }
        });
    }
    
    private void saveSettings() {
        String model = modelEditText.getText().toString().trim();
        String product = productEditText.getText().toString().trim();
        String brand = brandEditText.getText().toString().trim();
        String manufacturer = manufacturerEditText.getText().toString().trim();
        String device = deviceEditText.getText().toString().trim();
        String hardware = hardwareEditText.getText().toString().trim();
        String fingerprint = fingerprintEditText.getText().toString().trim();
        String buildId = buildIdEditText.getText().toString().trim();
        String type = typeEditText.getText().toString().trim();
        String tags = tagsEditText.getText().toString().trim();
        
        if (model.isEmpty() || product.isEmpty() || brand.isEmpty() || manufacturer.isEmpty()
                || device.isEmpty() || hardware.isEmpty() || fingerprint.isEmpty()
                || buildId.isEmpty() || type.isEmpty() || tags.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_MODEL, model);
        editor.putString(PREF_PRODUCT, product);
        editor.putString(PREF_BRAND, brand);
        editor.putString(PREF_MANUFACTURER, manufacturer);
        editor.putString(PREF_DEVICE, device);
        editor.putString(PREF_HARDWARE, hardware);
        editor.putString(PREF_FINGERPRINT, fingerprint);
        editor.putString(PREF_BUILD_ID, buildId);
        editor.putString(PREF_TYPE, type);
        editor.putString(PREF_TAGS, tags);
        editor.apply();
        
        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
    }
    
    private void resetSettings() {
        modelEditText.setText(NetflixFix.DEFAULT_MODEL);
        productEditText.setText(NetflixFix.DEFAULT_PRODUCT);
        brandEditText.setText(NetflixFix.DEFAULT_BRAND);
        manufacturerEditText.setText(NetflixFix.DEFAULT_MANUFACTURER);
        deviceEditText.setText(NetflixFix.DEFAULT_DEVICE);
        hardwareEditText.setText(NetflixFix.DEFAULT_HARDWARE);
        fingerprintEditText.setText(NetflixFix.DEFAULT_FINGERPRINT);
        buildIdEditText.setText(NetflixFix.DEFAULT_BUILD_ID);
        typeEditText.setText(NetflixFix.DEFAULT_TYPE);
        tagsEditText.setText(NetflixFix.DEFAULT_TAGS);
        Toast.makeText(this, R.string.settings_reset, Toast.LENGTH_SHORT).show();
    }
    
    public static String getDeviceModel(android.content.Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(PREF_MODEL, NetflixFix.DEFAULT_MODEL);
    }
    
    public static String getDeviceProduct(android.content.Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(PREF_PRODUCT, NetflixFix.DEFAULT_PRODUCT);
    }
}
