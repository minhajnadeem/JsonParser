package com.example.minhaj.jsonparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "tag";
    private final String API = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1";
    private final String JSON_STR = "[\n" +
            "   {\n" +
            "     \"id\": 912345678901,\n" +
            "     \"text\": \"How do I read JSON on Android?\",\n" +
            "     \"geo\": [59.454722, -110.606667],\n" +
            "     \"user\": {\n" +
            "       \"name\": \"android_newb\",\n" +
            "       \"followers_count\": 41\n" +
            "      }\n" +
            "   },\n" +
            "   {\n" +
            "     \"id\": 912345678902,\n" +
            "     \"text\": \"@android_newb just use android.util.JsonReader!\",\n" +
            "     \"geo\": [50.454722, -104.606667],\n" +
            "     \"user\": {\n" +
            "       \"name\": \"jesse\",\n" +
            "       \"followers_count\": 2\n" +
            "     }\n" +
            "   }\n" +
            " ]";

    private List<Data> myData = new ArrayList<>();
    private StringBuilder stringBuilder = new StringBuilder();
    private JsonReader reader;

    private TextView tvDesc,tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvMain = (TextView) findViewById(R.id.tv_main);
        try {
            JSONArray jsonArray = new JSONArray(JSON_STR);
            Log.d(TAG,jsonArray.toString());
            myData = parseJson(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (Data data : myData){
            stringBuilder.append(data.getId())
                    .append("\n")
                    .append(data.getText())
                    .append("\n")
                    .append(data.getGeo()[0])
                    .append(data.getGeo()[1])
                    .append("\n")
                    .append(data.getUser().getName())
                    .append("\n")
                    .append(data.getUser().getFollowersCount());
        }
        String d = stringBuilder.toString();
        Log.d(TAG,"---------------------parsing using json object----------------------------");
        Log.d(TAG,d);
        Log.d(TAG,"---------------------parsing using json reader----------------------------");
        new Async(this).execute(API);
    }

    public void getJsonReader(InputStream inputStream) {
        reader = new JsonReader(new InputStreamReader(inputStream));
        try {
            reader.beginObject();
            while (reader.hasNext()){
                String name = reader.nextName();
                //Log.d(TAG,"name :"+name);
                if (name.equals("weather")){
                    //handleArray(reader);
                    getDescription(reader);
                }

                else if (name.equals("coord"))
                    handleCoord(reader);
                else if (name.equals("main")) {
                    getTemp(reader);
                    Log.d(TAG,"mainnnnnnn");
                }
                else
                    reader.skipValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getTemp(JsonReader reader) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                Log.d(TAG,reader.nextName());
                String name = reader.nextName();
                if (name.equals("temp")) {
                    tvMain.setText(reader.nextString());
                }else
                    reader.skipValue();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDescription(JsonReader reader) {
        try {
            reader.beginArray();
            while (reader.hasNext()){
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    Log.d(TAG,name);
                    if (name.equals("description")){
                        tvDesc.setText(reader.nextString());
                    }else
                        reader.skipValue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCoord(JsonReader reader) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("lon")) {
                    Log.d(TAG, reader.nextDouble() + "");
                } else if (name.equals("lat")) {
                    Log.d(TAG, reader.nextDouble() + "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleArray(JsonReader reader) {
        try {
            reader.beginArray();
            while (reader.hasNext()){
                handleObject(reader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.endArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleObject(JsonReader reader) {
        try {
            reader.beginObject();
            while (reader.hasNext()){
                String name = reader.nextName();
                //Log.d(TAG,name);
                //Log.d(TAG,reader.nextString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Data> parseJson(JSONArray jsonArray) {
        List<Data> list = new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String text = jsonObject.getString("text");
                double[] geo = getGeo(jsonObject);
                User user = getUser(jsonObject);
                list.add(new Data(id,text,geo,user));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private User getUser(JSONObject jsonObject) {
        String name = null;
        int count = -1;
        try {
            JSONObject object = jsonObject.getJSONObject("user");
            name = object.getString("name");
            count = object.getInt("followers_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new User(name,count);
    }

    private double[] getGeo(JSONObject jsonObject) {
        double[] geo = new double[2] ;
        try {
            JSONArray array = jsonObject.getJSONArray("geo");
            geo[0] = array.getDouble(0);
            geo[1] = array.getDouble(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return geo;
    }

}
