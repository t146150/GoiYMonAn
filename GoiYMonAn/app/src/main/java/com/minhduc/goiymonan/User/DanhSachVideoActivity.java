package com.minhduc.goiymonan.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.minhduc.goiymonan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DanhSachVideoActivity extends AppCompatActivity {

    public static String API_KEY = "AIzaSyAfC6QZeKyhtarCD1u0lUGmpIG9u3-pWbk";
    String ID_PLAYLIST = "PLhvDq05xmwPiLYHm9WuSZVe-nDb3V2kve";

    String urlGetVideo ="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLhvDq05xmwPiLYHm9WuSZVe-nDb3V2kve&key=AIzaSyCPUys7we20UOayI3ulZEdwL6wPQRonmv4&maxResults=50";

    ListView lvVideo;
    ArrayList<VideoYoutube> arrayVideo;
    VideoYoutubeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_video); // ??????
        //à

        lvVideo = (ListView) findViewById(R.id.listviewVideo);
        arrayVideo = new ArrayList<>();
        adapter = new VideoYoutubeAdapter(this, R.layout.dong_video_youtube, arrayVideo);
        lvVideo.setAdapter(adapter);

        GetJsonYoutube(urlGetVideo);

        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DanhSachVideoActivity.this, PlayVideoActivity.class);
                intent.putExtra("idvideo", arrayVideo.get(i).getIdVideo());
                startActivity(intent);
            }
        });
    }

    private void GetJsonYoutube(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonItems = response.getJSONArray("items");
                            String title = ""; String url = ""; String idVideo = "";

                            for (int i = 0; i < jsonItems.length(); i++){
                                JSONObject jsonItem     = jsonItems.getJSONObject(i);
                                JSONObject jsonSnippet  = jsonItem.getJSONObject("snippet");
                                title = jsonSnippet.getString("title");
                                JSONObject jsonThumbnail    = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium       = jsonThumbnail.getJSONObject("medium");
                                url = jsonMedium.getString("url");
                                JSONObject jsonResourceId   = jsonSnippet.getJSONObject("resourceId");
                                idVideo = jsonResourceId.getString("videoId");
                                arrayVideo.add(new VideoYoutube(idVideo, title, url));
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DanhSachVideoActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "ERROR: " + error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}