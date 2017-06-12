package com.aguspuryanto.laguazmp3;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by AGUS on 10/06/2017.
 */

public class DetailActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handler;
    //private Runnable moveSeekBarThread;
    private Button bPlay, bStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView txtTitle = (TextView) findViewById(R.id.title);
        final TextView txtDesc = (TextView) findViewById(R.id.desc);
        txtDesc.setVisibility(View.GONE);

        Intent i = getIntent();
        txtTitle.setText(i.getStringExtra("title"));
        String[] paths = i.getStringExtra("desc").split("/");

        // DEMO SERVER
        String DATA_URL = "http://yukpigi.com/laguaz/" + "?" +
                URLEncoder.encode(paths[1]) + "=" +
                URLEncoder.encode(paths[2]);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);

        bPlay = (Button)findViewById(R.id.btnPlay);
        bStop = (Button)findViewById(R.id.btnStop);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(0);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, DATA_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    txtDesc.setText(response.getString("source_list").toString());
                    //Toast.makeText(getApplicationContext(), response.getString("source_list").toString(), Toast.LENGTH_LONG).show();
                    mediaPlayer.setDataSource(txtDesc.getText().toString());
                    mediaPlayer.prepareAsync();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //System.out.println(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d("Error : ", error.toString());
            }
        });
        queue.add(jsObjRequest);

        // You can show progress dialog here untill it prepared to play
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Now dismis progress dialog, Media palyer will start playing
                mp.start();
                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // dissmiss progress bar here. It will come here when
                // MediaPlayer
                // is not able to play file. You can show error message to user
                return false;
            }
        });

        //seekBar.setOnTouchListener(DetailActivity.this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });

        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    bPlay.setText("Play");
                } else {
                    mediaPlayer.start();
                    bPlay.setText("Pause");
                }
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    finish();
                }
            }
        });

        handler = new Handler();
        handler.removeCallbacks(moveSeekBarThread);
        handler.postDelayed(moveSeekBarThread, 100); //cal the thread after 100 milliseconds
    }

    private Runnable moveSeekBarThread = new Runnable() {
        public void run() {
            if (mediaPlayer.isPlaying()) {

                int mediaPos_new = mediaPlayer.getCurrentPosition();
                int mediaMax_new = mediaPlayer.getDuration();

                seekBar.setMax(mediaMax_new);
                seekBar.setProgress(mediaPos_new);

                handler.postDelayed(this, 100); // Looping the thread after 0.1
                // seconds
            } else {
                int mediaPos_new = mediaPlayer.getCurrentPosition();
                int mediaMax_new = mediaPlayer.getDuration();

                seekBar.setMax(mediaMax_new);
                seekBar.setProgress(mediaPos_new);

                handler.postDelayed(this, 100); // Looping the thread after 0.1
                // seconds
            }
        }
    };

        public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }

        private static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
}
