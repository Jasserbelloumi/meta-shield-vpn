package com.jasser.vpn;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.*;
import android.view.*;
import android.graphics.Color;

public class MainActivity extends Activity {
    WebView ghostBrowser;
    String currentProxy = "104.248.48.190";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGhostUI();
    }

    private void setupGhostUI() {
        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.parseColor("#050505"));

        // شريط التحكم العلوي
        LinearLayout topBar = new LinearLayout(this);
        topBar.setPadding(20, 30, 20, 30);
        topBar.setBackgroundColor(Color.parseColor("#111111"));
        topBar.setGravity(Gravity.CENTER_VERTICAL);

        Spinner countrySpinner = new Spinner(this);
        String[] countries = {"🇺🇸 USA", "🇩🇪 Germany", "🇯🇵 Japan", "🇬🇧 UK", "🇫🇷 France"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinner.setAdapter(adapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateServer(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        // زر مسح البيانات (Clear Trace)
        Button clearBtn = new Button(this);
        clearBtn.setText("💣 مسح الأثر");
        clearBtn.setBackgroundColor(Color.parseColor("#8B0000"));
        clearBtn.setTextColor(Color.WHITE);
        clearBtn.setOnClickListener(v -> clearAllData());

        topBar.addView(countrySpinner, new LinearLayout.LayoutParams(0, -2, 1));
        topBar.addView(clearBtn);
        main.addView(topBar);

        ghostBrowser = new WebView(this);
        configureGhostEngine();
        main.addView(ghostBrowser, new LinearLayout.LayoutParams(-1, -1));

        setContentView(main);
        ghostBrowser.loadUrl("https://whoer.net");
    }

    private void clearAllData() {
        // 1. مسح الكوكيز
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
        
        // 2. مسح الكاش والتاريخ
        ghostBrowser.clearCache(true);
        ghostBrowser.clearHistory();
        ghostBrowser.clearFormData();
        
        // 3. إعادة تحميل الصفحة لضمان نظافة الجلسة
        ghostBrowser.reload();
        
        Toast.makeText(this, "⚠️ تم تدمير جميع ملفات التعريف وبيانات التتبع بنجاح!", Toast.LENGTH_LONG).show();
    }

    private void updateServer(int pos) {
        String[] proxies = {"104.248.48.190", "159.65.105.210", "139.162.115.118", "178.62.18.251", "51.15.242.202"};
        currentProxy = proxies[pos];
        System.setProperty("http.proxyHost", currentProxy);
        System.setProperty("https.proxyHost", currentProxy);
        if(ghostBrowser != null) ghostBrowser.reload();
    }

    private void configureGhostEngine() {
        WebSettings s = ghostBrowser.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        ghostBrowser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                injectAntiFingerprint();
            }
        });
    }

    private void injectAntiFingerprint() {
        String js = "javascript:(function() {" +
            "Object.defineProperty(navigator, 'hardwareConcurrency', {get: () => 16});" +
            "Object.defineProperty(navigator, 'deviceMemory', {get: () => 64});" +
            "Object.defineProperty(screen, 'width', {get: () => 3840});" +
            "Object.defineProperty(screen, 'height', {get: () => 2160});" +
            "Object.defineProperty(navigator, 'languages', {get: () => ['en-US', 'en']});" +
            "})()";
        ghostBrowser.loadUrl(js);
    }
}
