package com.aguspuryanto.laguazmp3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //JSON URL
    String DATA_URL = "http://yukpigi.com/laguaz/";
    //Tags used in the JSON String
    String TAG_TITLE = "title";
    String TAG_LINK = "link";
    String TAG_IMG = "img";

    // Each row in the list stores country name, currency and flag
    //ArrayList<HashMap<String, Object>> MyArrList = new ArrayList<HashMap<String, Object>>();
    private List<HashMap<String, String>> MyArrList = new ArrayList<>();
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray result = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            result = new JSONArray(response);
                            //Calling method getStudents to get the students from the JSON Array
                            laguHot(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //System.out.println(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

        //Initializing the ArrayList
        listview = (ListView)findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(MainActivity.this, MyArrList.get(position).get(TAG_TITLE),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("title", MyArrList.get(position).get(TAG_TITLE));
                intent.putExtra("desc", MyArrList.get(position).get(TAG_LINK));
                startActivity(intent);
            }

        });
    }

    private void laguHot(JSONArray j){
        //Traversing through all the items in the json array
        //MyArrList = new ArrayList<HashMap<String, Object>>();
        //HashMap<String, Object> map;

        for(int i=0;i<j.length();i++){
            try {
                HashMap<String, String> map = new HashMap<>();
                //Getting json object
                JSONObject obj = j.getJSONObject(i);

                //Adding the name of the student to array list
                map.put(TAG_TITLE, obj.getString(TAG_TITLE));
                map.put(TAG_LINK, obj.getString(TAG_LINK));
                map.put(TAG_IMG, obj.getString(TAG_IMG));

                MyArrList.add(map);
                //System.out.println(obj.getString(TAG_IMG));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Keys used in Hashmap
        String[] from = { TAG_IMG,TAG_TITLE,TAG_LINK };
        // Ids of views in listview_layout
        int[] to = { R.id.imageView,R.id.textViewName,R.id.textViewLink};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        CustomList adapter = new CustomList(getBaseContext(), MyArrList, R.layout.listview_layout, from, to);
        // Setting the adapter to the listView
        listview.setAdapter(adapter);
    }
}
