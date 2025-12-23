package com.jasser.vpn;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.*;
import android.view.*;
import android.graphics.Color;
import java.util.Properties;

public class MainActivity extends Activity {
    WebView ghostBrowser;
    // قائمة بسيرفرات بروكسي مجانية وسريعة
    String[] proxies = {"104.248.48.190", "159.65.105.210", "139.162.115.118", "178.62.18.251", "51.15.242.202"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGhostUI();
    }

    private void setupGhostUI() {
        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.BLACK);

        Spinner countrySpinner = new Spinner(this);
        String[] countries = {"🇺🇸 USA", "🇩🇪 Germany", "🇯🇵 Japan", "🇬🇧 UK", "🇫🇷 France"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinner.setAdapter(adapter);
        
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setProxy(proxies[position]);
                ghostBrowser.reload();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        main.addView(countrySpinner);
        ghostBrowser = new WebView(this);
        ghostBrowser.getSettings().setJavaScriptEnabled(true);
        main.addView(ghostBrowser);
        setContentView(main);
        ghostBrowser.loadUrl("https://whoer.net");
    }

    private void setProxy(String proxyHost) {
        // تفعيل البروكسي على مستوى النظام للتطبيق
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", "8080");
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", "8080");
    }
}
