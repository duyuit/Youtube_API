package com.example.billy.youtube_api;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import Adapter.MyDatabaseAdapter;
import Adapter.adapter_popular;
import Json_Support.ReadInternet;
import model.MyVideo1;


public class Main2Activity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayerView   youTubePlayerView;
    String API_Key="AIzaSyBWzQpjkJ1ZiG-Laj8TILCzzBDVuVEE4hQ";
    String List ="PLcPIagLJ1NL7tewId_Dk8qqlS8-spcELQ";

    YouTubePlayer youTubePlayer_main;

    ListView listView;
    public  static  adapter_popular adapterVideo;
    public  static  ArrayList<MyVideo1> arrayList;


    String cur_vid;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;

    String user_id;
    MyDatabaseAdapter myDatabase;
    SQLiteDatabase database;
    ArrayList<String> arrayListtemp;
    MyVideo1 Cac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        AddControl();

        Intent intent=getIntent();
        if(intent!=null)
        {
            user_id=intent.getStringExtra("user_id");
            cur_vid=intent.getStringExtra("vid_id");
        }
        final Button button=findViewById(R.id.button);
       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                youTubePlayerView.animate().alpha(0f).setDuration(2000).start();
//                youTubePlayerView.animate().scaleX(0.5f).scaleY(0.5f).setDuration(1000).start();

            }
        });
        new ReadInternet().execute("https://www.googleapis.com/youtube/v3/search?part=snippet&relatedToVideoId="+cur_vid+"&type=video&key=AIzaSyBWzQpjkJ1ZiG-Laj8TILCzzBDVuVEE4hQ&maxResults=50");


//        for(MyVideo myVideo:arrayList)
//        {
//            arrayListtemp.add(myVideo.getId());
//        }
//        Toast.makeText(this,arrayList.size()+"",Toast.LENGTH_LONG).show();
//        //youTubePlayer_main.cueVideos(arrayListtemp);
    }

    private void AddControl() {
        myDatabase= new MyDatabaseAdapter(Main2Activity.this);
        myDatabase.Khoitai();
        database=myDatabase.getMyDatabase();

        Cac= (MyVideo1) getIntent().getSerializableExtra("myVideo");
        listView=findViewById(R.id.lvVideo);
        arrayList=new ArrayList<>();
        adapterVideo=new adapter_popular(this,R.layout.video_item_popular,arrayList);
        listView.setAdapter(adapterVideo);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cur_vid=arrayList.get(i).getId();
                youTubePlayer_main.cueVideo(cur_vid);
                Cac=arrayList.get(i);
                arrayList.clear();
                new ReadInternet().execute("https://www.googleapis.com/youtube/v3/search?part=snippet&relatedToVideoId="+cur_vid+"&type=video&key=AIzaSyBWzQpjkJ1ZiG-Laj8TILCzzBDVuVEE4hQ&maxResults=20");

            }
        });

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<String> id=new ArrayList<>();
                try {
                    Cursor cursor = database.rawQuery("select * from Video where user_id='"+user_id+"'", null);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {
                        String vid_id=cursor.getString(2);
                        id.add(vid_id);
                        cursor.moveToNext();
                    }
                    cursor.close();
                    for(String id_vid:id)
                    {
                        if(id_vid.equals(cur_vid))
                        {
                            Toast.makeText(Main2Activity.this,"Bạn đã lưu video này rồi",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                } catch (Exception e){}
                ContentValues contentValues=new ContentValues();
                contentValues.put("user_id",user_id);
                contentValues.put("video_id",cur_vid);


                contentValues.put("channel_title",Cac.getChannelTitle());
                contentValues.put("video_title",Cac.getTitle());
                contentValues.put("avatar_url",Cac.getThumnail());

                database.insert("video",null,contentValues);
                Toast.makeText(Main2Activity.this,"Lưu thành công",Toast.LENGTH_LONG).show();
            }
        });
        ImageView imageView=findViewById(R.id.imgNext);
        ImageView imageView1=findViewById(R.id.imgPre);

        youTubePlayerView=findViewById(R.id.youtube);
        youTubePlayerView.initialize(API_Key,this);

        youTubePlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Main2Activity.this,"hehee",Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.cueVideo(cur_vid);
        youTubePlayer_main=youTubePlayer;

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


}

