package com.example.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String result = null;
                try {
                    // Open the connection
                    URL url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?y=37.514322572335935&x=127.06283102249932&radius=1000&query=abc");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    conn.setRequestProperty("Authorization", "KakaoAK restapi키");
                    InputStream is = conn.getInputStream();

                    // Get the stream
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    // Set the result
                    result = builder.toString();
                }
                catch (Exception e) {
                    // Error calling the rest api
                    Log.e("REST_API", "GET method failed: " + e.getMessage());
                    e.printStackTrace();
                }

                Log.d("rest 결과", result);

            }
        });

//        String key = getKeyHash(this);
//        Log.d("키해시 좀 얻자 제발", key);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    //    public static String getKeyHash(final Context context) {
//        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
//        if (packageInfo == null)
//            return null;
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
//            } catch (NoSuchAlgorithmException e) {
//                Log.d("트라이 캐치 에러", "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//        return null;
//    }

}
