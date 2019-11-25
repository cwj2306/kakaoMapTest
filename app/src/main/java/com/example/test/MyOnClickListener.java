package com.example.test;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.test.model.Result;
import com.google.gson.Gson;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyOnClickListener implements View.OnClickListener, Runnable {
    //검색 키워드
    private EditText editText;
    MapView mapView;
    Gson gson = new Gson();

    public MyOnClickListener(EditText editText, MapView mapView){
        this.editText = editText;
        this.mapView = mapView;
    }

    @Override
    public void onClick(View v) {
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.514322572335935, 127.06283102249932), true);

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        String resultJson = null;
        try {
            // Open the connection
            URL url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?y=37.514322572335935&x=127.06283102249932&radius=1000&query="+editText.getText().toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK 키");
            InputStream is = conn.getInputStream();

            // Get the stream
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            // Set the result
            resultJson = builder.toString();
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }

        Log.d("rest 결과", resultJson);

        Result result = gson.fromJson(resultJson, Result.class);
        double x = Double.parseDouble(result.getDocuments().get(0).getX());
        double y = Double.parseDouble(result.getDocuments().get(0).getY());

        Log.d("위치 = ", "X : " + x + " , Y : " + y );

        MapPOIItem poiItem = new MapPOIItem();
        poiItem.setMapPoint(MapPoint.mapPointWithGeoCoord(x, y));
        mapView.addPOIItem(poiItem);
    }
}
