package com.minhduc.goiymonan.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.minhduc.goiymonan.R;

public class PlayVideoActivity extends YouTubeBaseActivity {

    String id = "";
    YouTubePlayerView youTubePlayerView;
    int REQUEST_CODE = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.myYoutube);

        Intent intent = getIntent();

        if(intent != null){
            id = intent.getStringExtra("idvideo");
        }

        youTubePlayerView.initialize(DanhSachVideoActivity.API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(id);
                youTubePlayer.setFullscreen(true);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if(youTubeInitializationResult.isUserRecoverableError()){
                    youTubeInitializationResult.getErrorDialog(PlayVideoActivity.this, REQUEST_CODE);
                }else {
                    Toast.makeText(PlayVideoActivity.this, "Error Video!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE){
            youTubePlayerView.initialize(DanhSachVideoActivity.API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.loadVideo(id);
                    youTubePlayer.setFullscreen(true);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    if(youTubeInitializationResult.isUserRecoverableError()){
                        youTubeInitializationResult.getErrorDialog(PlayVideoActivity.this, REQUEST_CODE);
                    }else {
                        Toast.makeText(PlayVideoActivity.this, "Error Video!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
