package com.navasgroup.annoteweb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

public class AnnoteWebPage extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String tag = getString(R.string.app_name);                            // for logging
        String pre = getString(R.string.prepend);                            // prepend the URL
        String sharedText = getIntent().getStringExtra(Intent.EXTRA_TEXT);   // get URL
        Log.i(tag, "EXTRA_TEXT: \"" + sharedText + "\"");
        String urlText = getURL(sharedText);                     // handle bad browsers
        if (urlText != null && URLUtil.isNetworkUrl(urlText)){
            String enc = pre + Uri.encode(urlText, ":/?=&");    // allow needed
            Log.i(tag, "enc: \"" + enc + "\"");
            // Show a Toast when opening the page
            Toast.makeText(this, "Opening in Hypothes.is: " + urlText, Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse(enc);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Log.w(tag, "Failed to start web browser activity!");
                Toast.makeText(this, "Please install a web browser",  Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Not a valid URL: " + sharedText,  Toast.LENGTH_LONG).show();
        }
        finish();   // i'm done
    }

    // extract URL to handle bad browsers that pad URL before and/or after; e.g.,
    // "IBM - United States http://www.ibm.com/us-en/    (Share from CM Browser)"
    String getURL(String source) {
        String tag = getString(R.string.app_name);                // for logging
        String url = source.toLowerCase();                        // lower case for scan
        int r = url.indexOf("http://");                           // find HTTP
        int s = url.indexOf("https://");                          // find HTTPS
        int n = Math.min((r < 0 ? s : r), (s < 0 ? r : s));       // take first http or https
        url = (n > 0) ? source.substring(n) : source ;            // strip anything before URL
        url = url.split("\\s",2)[0];   // strip any trailing material starting with whitespace
        Log.d(tag, "getURL: \"" + url + "\"");
        return url;
    }
}
