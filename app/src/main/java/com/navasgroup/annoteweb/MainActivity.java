package com.navasgroup.annoteweb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Close button: closes the activity
        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> finish());

        // Help button: opens the help URL in browser
        Button helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(v -> {
            String helpUrl = getString(R.string.help_url);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(helpUrl));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "No browser found to open Help.", Toast.LENGTH_SHORT).show();
            }
        });

        // What's New: load and display content from whats-new.json
        TextView whatsNewContent = findViewById(R.id.whatsNewContent);
        String whatsNewText = loadWhatsNewFromJson();
        whatsNewContent.setText(whatsNewText);
    }

    // Helper method to read and parse whats-new.json
    private String loadWhatsNewFromJson() {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = getAssets().open("whats-new.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                sb.append(obj.getString("text")).append("\n");
            }
        } catch (Exception e) {
            sb.append("Unable to load What's New.");
        }
        return sb.toString().trim();
    }
}
