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
    
    private EditText modelEditText;
    private EditText productEditText;
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
        saveButton = findViewById(R.id.buttonSave);
        resetButton = findViewById(R.id.buttonReset);
    }
    
    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String model = prefs.getString(PREF_MODEL, "Mi 10");
        String product = prefs.getString(PREF_PRODUCT, "umi");
        
        modelEditText.setText(model);
        productEditText.setText(product);
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
        
        if (model.isEmpty() || product.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_MODEL, model);
        editor.putString(PREF_PRODUCT, product);
        editor.apply();
        
        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
    }
    
    private void resetSettings() {
        modelEditText.setText("Mi 10");
        productEditText.setText("umi");
        Toast.makeText(this, R.string.settings_reset, Toast.LENGTH_SHORT).show();
    }
    
    public static String getDeviceModel(android.content.Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(PREF_MODEL, "Mi 10");
    }
    
    public static String getDeviceProduct(android.content.Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(PREF_PRODUCT, "umi");
    }
}
