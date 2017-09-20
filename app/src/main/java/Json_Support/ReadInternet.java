package Json_Support;

import android.os.AsyncTask;
import android.util.Log;

import com.example.billy.youtube_api.Main2Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import model.MyVideo1;

/**
 * Created by Billy on 9/17/2017.
 */

    public class ReadInternet extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String result=docNoiDung_Tu_URL(strings[0]);
            return result;
        }

        private String docNoiDung_Tu_URL(String theUrl){
            StringBuilder content = new StringBuilder();
            try    {
                // create a url object
                URL url = new URL(theUrl);

                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();

                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line + "\n");
                }
                bufferedReader.close();
            }
            catch(Exception e)    {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
//                JSONObject boss=new JSONObject(s);
//                JSONArray jsonArray= boss.getJSONArray("items");
//                String title="";
//                String url="";
//                String idVid="";
//                for(int i=0;i<jsonArray.length();i++)
//                {
//                    JSONObject jsonObject=jsonArray.getJSONObject(i);
//                    JSONObject jsonObject1=jsonObject.getJSONObject("snippet");
//                    JSONObject jsonObject2=jsonObject1.getJSONObject("thumbnails");
//                    JSONObject jsonObject3=jsonObject2.getJSONObject("medium");
//                    JSONObject jsonObject4=jsonObject1.getJSONObject("resourceId");
//                    idVid=jsonObject4.getString("videoId");
//                    url=jsonObject3.getString("url");
//                    title=jsonObject1.getString("title");
//                    arrayList.add(new MyVideo(title,url,idVid));
//                                adapterVideo.notifyDataSetChanged();
                JSONObject boss = new JSONObject(s);
                JSONArray jsonArray = boss.getJSONArray("items");

                String title = "";
                String url = "";
                String idVid = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject JSid=jsonObject.getJSONObject("id");
                    idVid=JSid.getString("videoId");


                    JSONObject JSsnip=jsonObject.getJSONObject("snippet");
                    title=JSsnip.getString("title");



                    JSONObject JSthumnail=JSsnip.getJSONObject("thumbnails");
                    JSONObject JSmedium=JSthumnail.getJSONObject("medium");

                    url=JSmedium.getString("url");

                    String channelTitle = JSsnip.getString("channelTitle");

                    //Log.d("langthang",url.toString());
                    Main2Activity.arrayList.add(new MyVideo1(title,url,idVid,channelTitle));
                    Main2Activity.adapterVideo.notifyDataSetChanged();

                }
            }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            Log.d("test",s);
        }
    }
