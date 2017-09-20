package com.example.billy.youtube_api;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import Adapter.MyDatabaseAdapter;
import Adapter.adapter_popular;
import Json_Support.ReadInternet;
import model.MyVideo1;

public class ListPopular extends AppCompatActivity{
    ListView lsvpopular;

    adapter_popular adapterVideo;
    ArrayList<MyVideo1> arrayList;

    MyDatabaseAdapter myDatabase;
    SQLiteDatabase database;

    String user_id;
    SearchView searchView;
    String API_Key = "AIzaSyBWzQpjkJ1ZiG-Laj8TILCzzBDVuVEE4hQ";
    String uri = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_popular);
        lsvpopular = findViewById(R.id.lsvpopular);

        myDatabase = new MyDatabaseAdapter(ListPopular.this);

        myDatabase.Khoitai();
        database = myDatabase.getMyDatabase();
        user_id=getIntent().getStringExtra("user_id");
        new ReadInternet().execute("https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails&chart=mostPopular&regionCode=vn&key=AIzaSyBh6iutQqAhE4smQ19YLgJHR0HzcEZUGRs&maxResults=50");
        arrayList = new ArrayList<>();
        adapterVideo = new adapter_popular(this, R.layout.video_item_popular, arrayList);
        lsvpopular.setAdapter(adapterVideo);
        lsvpopular.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListPopular.this, Main2Activity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("vid_id", arrayList.get(i).getId());
                intent.putExtra("myVideo",arrayList.get(i));
                startActivityForResult(intent, 123);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        setTitle("Video");
    }
    Boolean flag=false;
    @Override
    public void onBackPressed() {

        if(flag)
        {
            new ReadInternet().execute("https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails&chart=mostPopular&regionCode=vn&key=AIzaSyBh6iutQqAhE4smQ19YLgJHR0HzcEZUGRs&maxResults=50");
            flag=!true;
        } else    super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.myVideo)
        {
            flag=true;
            try {
                Cursor cursor = database.rawQuery("select * from video where user_id='" + user_id + "'", null);
                cursor.moveToFirst();
                //Toast.makeText(this, cursor.getString(2), Toast.LENGTH_SHORT).show();
                arrayList.clear();
                adapterVideo.notifyDataSetChanged();
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(3);
                    String id_vid = cursor.getString(2);
                    String channel = cursor.getString(4);
                    String avatar = cursor.getString(5);
                    arrayList.add(new MyVideo1(title, avatar, id_vid, channel));
                    adapterVideo.notifyDataSetChanged();
                    cursor.moveToNext();
                }
                cursor.close();
            }catch (Exception e){
            };
            if(arrayList.size()==0)     Toast.makeText(this, "Bạn chưa thêm video nào", Toast.LENGTH_SHORT).show();

        }
        else if(item.getItemId()==R.id.pop)
        {
            flag=false;
            arrayList.clear();  new ReadInternet().execute("https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails&chart=mostPopular&regionCode=vn&key=AIzaSyBh6iutQqAhE4smQ19YLgJHR0HzcEZUGRs&maxResults=20");

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123)
        {
            searchView.onActionViewExpanded();

        }
    }

    public class ReadInternet extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = docNoiDung_Tu_URL(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject boss = new JSONObject(s);
                JSONArray jsonArray = boss.getJSONArray("items");
                String title = "";
                String url = "";
                String idVid = "";
                String channelTitle = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("snippet");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("thumbnails");
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("medium");

                    idVid = jsonObject.getString("id");
                    url = jsonObject3.getString("url");
                    title = jsonObject1.getString("title");
                    channelTitle = jsonObject1.getString("channelTitle");
                    arrayList.add(new MyVideo1(title, url, idVid, channelTitle));
                    adapterVideo.notifyDataSetChanged();


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("test", s);
        }
    }

    private String docNoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.itsearch);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                new ReadInternet1().execute(uri+searchView.getQuery()+"&key="+API_Key);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
//    }
    }


    public class ReadInternet1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = docNoiDung_Tu_URL(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject boss = new JSONObject(s);
                JSONArray jsonArray = boss.getJSONArray("items");
                String title = "";
                String url = "";
                String idVid = "";
                String channelTitle = "";
                arrayList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("id");
                    JSONObject jsonObject2 = jsonObject.getJSONObject("snippet");
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("thumbnails");
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("medium");
                    idVid = jsonObject1.getString("videoId");
                    url = jsonObject4.getString("url");
                    title = jsonObject2.getString("title");
                    channelTitle = jsonObject2.getString("channelTitle");
                    arrayList.add(new MyVideo1(title, url, idVid, channelTitle));
                    adapterVideo.notifyDataSetChanged();


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("test", s);
        }
    }

}

