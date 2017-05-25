package com.example.user.ubike;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Ubike extends AppCompatActivity {
    class Data {
        Ubike.Data.Features[] features;

        class Features {
            Ubike.Data.Features.Properties properties;
            Ubike.Data.Features.Geometry geometry;
            String id;
            class Properties {
                String name;
                String available;
                String total;
            }

            class Geometry {
                String[] coordinates;
            }
        }
    }
    //    private Activity mActivity;
    private LocationManager mLocationManager;
    private ArrayList<Poi> Pois = new ArrayList<>();
    @SuppressWarnings("unused")
    Thread thread;
    private Handler handler =new Handler();
    private com.google.android.gms.maps.GoogleMap Map;
    private ListView listV;
    private List<UbikeList> ubike_list = new ArrayList<UbikeList>();
    private String[] ListStr = new String[2];
    private MyAdapter adapter;
    private AlertDialog askOpenLocationDialog;
    private Long startTime;
    private LatLng nowposition ,markerPosition,myPosition;
    SQLiteDatabase dbrw;
    int X=0;
    public String list_item_all[] ,Lat_all[] ,Lng_all[] ,title_name_all[] ,title_total_all[] ,title_available_all[] ,stationId_all[];
    private String on_click_marker_name;
    private int ID;            //站編號

    private Button button;
    private int[] drawable_bike_park={R.drawable.bike_park_1,R.drawable.bike_park_2,R.drawable.bike_park_3,R.drawable.bike_park_4,R.drawable.bike_park_5,
            R.drawable.bike_park_6,R.drawable.bike_park_7,R.drawable.bike_park_8,R.drawable.bike_park_9,R.drawable.bike_park_10,R.drawable.bike_park_11,
            R.drawable.bike_park_12,R.drawable.bike_park_13,R.drawable.bike_park_14,R.drawable.bike_park_15,R.drawable.bike_park_16,R.drawable.bike_park_17,
            R.drawable.bike_park_18,R.drawable.bike_park_19,R.drawable.bike_park_20,R.drawable.bike_park_21,R.drawable.bike_park_22,R.drawable.bike_park_23,
            R.drawable.bike_park_24,R.drawable.bike_park_25,R.drawable.bike_park_26,R.drawable.bike_park_27,R.drawable.bike_park_28,R.drawable.bike_park_29,
            R.drawable.bike_park_30,R.drawable.bike_park_31,R.drawable.bike_park_32,R.drawable.bike_park_33,R.drawable.bike_park_34,R.drawable.bike_park_35,
            R.drawable.bike_park_36,R.drawable.bike_park_37,R.drawable.bike_park_38,R.drawable.bike_park_39,R.drawable.bike_park_40,R.drawable.bike_park_41,
            R.drawable.bike_park_42,R.drawable.bike_park_43,R.drawable.bike_park_44,R.drawable.bike_park_45,R.drawable.bike_park_46,R.drawable.bike_park_47,
            R.drawable.bike_park_48,R.drawable.bike_park_49,R.drawable.bike_park_50,R.drawable.bike_park_51,R.drawable.bike_park_52,R.drawable.bike_park_53,
            R.drawable.bike_park_54,R.drawable.bike_park_55,R.drawable.bike_park_56,R.drawable.bike_park_57,R.drawable.bike_park_58,R.drawable.bike_park_59,
            R.drawable.bike_park_60,R.drawable.bike_park_61,R.drawable.bike_park_62,R.drawable.bike_park_63,R.drawable.bike_park_64,R.drawable.bike_park_65,
            R.drawable.bike_park_66,R.drawable.bike_park_67,R.drawable.bike_park_68,R.drawable.bike_park_69,R.drawable.bike_park_70,R.drawable.bike_park_71,
            R.drawable.bike_park_72,R.drawable.bike_park_73,R.drawable.bike_park_74,R.drawable.bike_park_75,R.drawable.bike_park_76,R.drawable.bike_park_77,
            R.drawable.bike_park_78,R.drawable.bike_park_79,R.drawable.bike_park_80,R.drawable.bike_park_81,R.drawable.bike_park_82,R.drawable.bike_park_83,
            R.drawable.bike_park_84,R.drawable.bike_park_85,R.drawable.bike_park_86,R.drawable.bike_park_87,R.drawable.bike_park_88,R.drawable.bike_park_89,
            R.drawable.bike_park_90,R.drawable.bike_park_91,R.drawable.bike_park_92,R.drawable.bike_park_93,R.drawable.bike_park_94,R.drawable.bike_park_95,
            R.drawable.bike_park_96,R.drawable.bike_park_97,R.drawable.bike_park_98,R.drawable.bike_park_99,R.drawable.bike_park_100,R.drawable.bike_park_101,
            R.drawable.bike_park_102,R.drawable.bike_park_103,R.drawable.bike_park_104,R.drawable.bike_park_105,R.drawable.bike_park_106,R.drawable.bike_park_107,
            R.drawable.bike_park_108,R.drawable.bike_park_109,R.drawable.bike_park_110,R.drawable.bike_park_111,R.drawable.bike_park_112,R.drawable.bike_park_113,
            R.drawable.bike_park_114,R.drawable.bike_park_115,R.drawable.bike_park_116,R.drawable.bike_park_117,R.drawable.bike_park_118,R.drawable.bike_park_119,
            R.drawable.bike_park_120,R.drawable.bike_park_121,R.drawable.bike_park_122,R.drawable.bike_park_123,R.drawable.bike_park_124,R.drawable.bike_park_125,
            R.drawable.bike_park_126,R.drawable.bike_park_127,R.drawable.bike_park_128,R.drawable.bike_park_129,R.drawable.bike_park_130,R.drawable.bike_park_131,
            R.drawable.bike_park_132,R.drawable.bike_park_133,R.drawable.bike_park_134,R.drawable.bike_park_135,R.drawable.bike_park_136,R.drawable.bike_park_137,
            R.drawable.bike_park_138,R.drawable.bike_park_139,R.drawable.bike_park_140,R.drawable.bike_park_141,R.drawable.bike_park_142,R.drawable.bike_park_143,
            R.drawable.bike_park_144,R.drawable.bike_park_145,R.drawable.bike_park_146,R.drawable.bike_park_147,R.drawable.bike_park_148,R.drawable.bike_park_149,
            R.drawable.bike_park_150,R.drawable.bike_park_151,R.drawable.bike_park_152,R.drawable.bike_park_153,R.drawable.bike_park_154,R.drawable.bike_park_155,
            R.drawable.bike_park_156,R.drawable.bike_park_157,R.drawable.bike_park_158,R.drawable.bike_park_159,R.drawable.bike_park_160,R.drawable.bike_park_161,
            R.drawable.bike_park_162,R.drawable.bike_park_163,R.drawable.bike_park_164,R.drawable.bike_park_165,R.drawable.bike_park_166,R.drawable.bike_park_167,
            R.drawable.bike_park_168,R.drawable.bike_park_169,R.drawable.bike_park_170,R.drawable.bike_park_171,R.drawable.bike_park_172,R.drawable.bike_park_173,
            R.drawable.bike_park_174,R.drawable.bike_park_175,R.drawable.bike_park_176,R.drawable.bike_park_177,R.drawable.bike_park_178,R.drawable.bike_park_179,
            R.drawable.bike_park_180,R.drawable.bike_park_181,R.drawable.bike_park_182,R.drawable.bike_park_183,R.drawable.bike_park_184,R.drawable.bike_park_185,
            R.drawable.bike_park_186,R.drawable.bike_park_187,R.drawable.bike_park_188,R.drawable.bike_park_189,R.drawable.bike_park_190,R.drawable.bike_park_191,
            R.drawable.bike_park_192,R.drawable.bike_park_193,R.drawable.bike_park_194,R.drawable.bike_park_195,R.drawable.bike_park_196,R.drawable.bike_park_197,
            R.drawable.bike_park_198,R.drawable.bike_park_199,R.drawable.bike_park_200};
    private int[] drawable_bike_rent={R.drawable.bike_rent_1,R.drawable.bike_rent_2,R.drawable.bike_rent_3,R.drawable.bike_rent_4,R.drawable.bike_rent_5,
            R.drawable.bike_rent_6,R.drawable.bike_rent_7,R.drawable.bike_rent_8,R.drawable.bike_rent_9,R.drawable.bike_rent_10,R.drawable.bike_rent_11,
            R.drawable.bike_rent_12,R.drawable.bike_rent_13,R.drawable.bike_rent_14,R.drawable.bike_rent_15,R.drawable.bike_rent_16,R.drawable.bike_rent_17,
            R.drawable.bike_rent_18,R.drawable.bike_rent_19,R.drawable.bike_rent_20,R.drawable.bike_rent_21,R.drawable.bike_rent_22,R.drawable.bike_rent_23,
            R.drawable.bike_rent_24,R.drawable.bike_rent_25,R.drawable.bike_rent_26,R.drawable.bike_rent_27,R.drawable.bike_rent_28,R.drawable.bike_rent_29,
            R.drawable.bike_rent_30,R.drawable.bike_rent_31,R.drawable.bike_rent_32,R.drawable.bike_rent_33,R.drawable.bike_rent_34,R.drawable.bike_rent_35,
            R.drawable.bike_rent_36,R.drawable.bike_rent_37,R.drawable.bike_rent_38,R.drawable.bike_rent_39,R.drawable.bike_rent_40,R.drawable.bike_rent_41,
            R.drawable.bike_rent_42,R.drawable.bike_rent_43,R.drawable.bike_rent_44,R.drawable.bike_rent_45,R.drawable.bike_rent_46,R.drawable.bike_rent_47,
            R.drawable.bike_rent_48,R.drawable.bike_rent_49,R.drawable.bike_rent_50,R.drawable.bike_rent_51,R.drawable.bike_rent_52,R.drawable.bike_rent_53,
            R.drawable.bike_rent_54,R.drawable.bike_rent_55,R.drawable.bike_rent_56,R.drawable.bike_rent_57,R.drawable.bike_rent_58,R.drawable.bike_rent_59,
            R.drawable.bike_rent_60,R.drawable.bike_rent_61,R.drawable.bike_rent_62,R.drawable.bike_rent_63,R.drawable.bike_rent_64,R.drawable.bike_rent_65,
            R.drawable.bike_rent_66,R.drawable.bike_rent_67,R.drawable.bike_rent_68,R.drawable.bike_rent_69,R.drawable.bike_rent_70,R.drawable.bike_rent_71,
            R.drawable.bike_rent_72,R.drawable.bike_rent_73,R.drawable.bike_rent_74,R.drawable.bike_rent_75,R.drawable.bike_rent_76,R.drawable.bike_rent_77,
            R.drawable.bike_rent_78,R.drawable.bike_rent_79,R.drawable.bike_rent_80,R.drawable.bike_rent_81,R.drawable.bike_rent_82,R.drawable.bike_rent_83,
            R.drawable.bike_rent_84,R.drawable.bike_rent_85,R.drawable.bike_rent_86,R.drawable.bike_rent_87,R.drawable.bike_rent_88,R.drawable.bike_rent_89,
            R.drawable.bike_rent_90,R.drawable.bike_rent_91,R.drawable.bike_rent_92,R.drawable.bike_rent_93,R.drawable.bike_rent_94,R.drawable.bike_rent_95,
            R.drawable.bike_rent_96,R.drawable.bike_rent_97,R.drawable.bike_rent_98,R.drawable.bike_rent_99,R.drawable.bike_rent_100,R.drawable.bike_rent_101,
            R.drawable.bike_rent_102,R.drawable.bike_rent_103,R.drawable.bike_rent_104,R.drawable.bike_rent_105,R.drawable.bike_rent_106,R.drawable.bike_rent_107,
            R.drawable.bike_rent_108,R.drawable.bike_rent_109,R.drawable.bike_rent_110,R.drawable.bike_rent_111,R.drawable.bike_rent_112,R.drawable.bike_rent_113,
            R.drawable.bike_rent_114,R.drawable.bike_rent_115,R.drawable.bike_rent_116,R.drawable.bike_rent_117,R.drawable.bike_rent_118,R.drawable.bike_rent_119,
            R.drawable.bike_rent_120,R.drawable.bike_rent_121,R.drawable.bike_rent_122,R.drawable.bike_rent_123,R.drawable.bike_rent_124,R.drawable.bike_rent_125,
            R.drawable.bike_rent_126,R.drawable.bike_rent_127,R.drawable.bike_rent_128,R.drawable.bike_rent_129,R.drawable.bike_rent_130,R.drawable.bike_rent_131,
            R.drawable.bike_rent_132,R.drawable.bike_rent_133,R.drawable.bike_rent_134,R.drawable.bike_rent_135,R.drawable.bike_rent_136,R.drawable.bike_rent_137,
            R.drawable.bike_rent_138,R.drawable.bike_rent_139,R.drawable.bike_rent_140,R.drawable.bike_rent_141,R.drawable.bike_rent_142,R.drawable.bike_rent_143,
            R.drawable.bike_rent_144,R.drawable.bike_rent_145,R.drawable.bike_rent_146,R.drawable.bike_rent_147,R.drawable.bike_rent_148,R.drawable.bike_rent_149,
            R.drawable.bike_rent_150,R.drawable.bike_rent_151,R.drawable.bike_rent_152,R.drawable.bike_rent_153,R.drawable.bike_rent_154,R.drawable.bike_rent_155,
            R.drawable.bike_rent_156,R.drawable.bike_rent_157,R.drawable.bike_rent_158,R.drawable.bike_rent_159,R.drawable.bike_rent_160,R.drawable.bike_rent_161,
            R.drawable.bike_rent_162,R.drawable.bike_rent_163,R.drawable.bike_rent_164,R.drawable.bike_rent_165,R.drawable.bike_rent_166,R.drawable.bike_rent_167,
            R.drawable.bike_rent_168,R.drawable.bike_rent_169,R.drawable.bike_rent_170,R.drawable.bike_rent_171,R.drawable.bike_rent_172,R.drawable.bike_rent_173,
            R.drawable.bike_rent_174,R.drawable.bike_rent_175,R.drawable.bike_rent_176,R.drawable.bike_rent_177,R.drawable.bike_rent_178,R.drawable.bike_rent_179,
            R.drawable.bike_rent_180,R.drawable.bike_rent_181,R.drawable.bike_rent_182,R.drawable.bike_rent_183,R.drawable.bike_rent_184,R.drawable.bike_rent_185,
            R.drawable.bike_rent_186,R.drawable.bike_rent_187,R.drawable.bike_rent_188,R.drawable.bike_rent_189,R.drawable.bike_rent_190,R.drawable.bike_rent_191,
            R.drawable.bike_rent_192,R.drawable.bike_rent_193,R.drawable.bike_rent_194,R.drawable.bike_rent_195,R.drawable.bike_rent_196,R.drawable.bike_rent_197,
            R.drawable.bike_rent_198,R.drawable.bike_rent_199,R.drawable.bike_rent_200};

    private int init= 0;
    private Long Minius;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemap);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        askOpenLocation("請開啟GPS定位");
//        runThread();
//        LayoutInflater inflater =  getLayoutInflater();
////        final View view1 = inflater.inflate(R.layout.googlemap, null);//找出第一個視窗
//        final View view2 = inflater.inflate(R.layout.activity_loading, null);//找出第二個視窗
//        setContentView(view2); //顯示第一個視窗
//        Intent i=new Intent();
//        i.setClass(Ubike.this,LoadingActivity.class);
//        startActivityForResult(i,0);


        getUbikeAPI();

//        do while(! String.valueOf(X).toString().equals("1")){
            try {
                thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            runThread();//跑 LOADING畫面
//        }
//        for(;!String.valueOf(X).toString().equals("1");){
//        }
        final ToggleButton mToggleButton = (ToggleButton) findViewById(R.id.mToggleButton);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mToggleButton.setChecked(false);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final com.google.android.gms.maps.GoogleMap googleMap) {
                Map = googleMap;
                if (ActivityCompat.checkSelfPermission(Ubike.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Ubike.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                ShowRentStation();//顯示可租圖片及可租圖示
//                runThread(2);

                mToggleButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (mToggleButton.isChecked()) {    // 按鈕按下顯示可租按鈕及圖示
                            ShowStopStation();//顯示可停圖示
                        } else {                              // 按鈕按下顯示可停按鈕及圖示
                            ShowRentStation();//顯示可租圖示
                        }
                    }
                });

                googleMap.getUiSettings().setMapToolbarEnabled(false);//隱藏地圖選單
                googleMap.setMyLocationEnabled(true);  //顯示自己的位置
                mLocationManager.requestLocationUpdates
                        (LocationManager.NETWORK_PROVIDER, 0, 10000.0f, LocationChange);
                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(Ubike.this);
                googleMap.setInfoWindowAdapter(adapter);
                googleMap.setOnInfoWindowClickListener(new com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker arg0) {
//                    switchstateMap(false);
//                    markerPosition=arg0.getPosition();
//                    showRoute(arg0.getPosition());
                        String marker_title;
                        markerPosition = arg0.getPosition();
                        marker_title = arg0.getTitle();
                        String marker_title_name;
                        marker_title_name = marker_title.substring(7, marker_title.length());
                        on_click_marker_name = marker_title_name;
                        setMapList();
                    }
                });
            }
        });
        MyDBHelper dbHelper = new MyDBHelper(Ubike.this);
        dbrw = dbHelper.getWritableDatabase();
        dopoisort();
//        //宣告Timer
//        Timer timer01 =new Timer();
//        //設定Timer(task為執行內容，0代表立刻開始,間格1秒執行一次)
//        timer01.schedule(task, 0,1000);



//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                startflag=true;
//                    init++;
//                    switch (init) {
//                        case 1:
//                            //取得目前時間
//                            String a = String.valueOf(init);
//                            Log.e("agn", a);
//                            startTime = System.currentTimeMillis();
//                            //設定定時要執行的方法
//                            handler.removeCallbacks(updateTimer);
//                            //設定Delay的時間
//                            handler.postDelayed(updateTimer, 1000);
//                            break;
//                        case 2:
////                        TimerDestroy();
////                        textView.setText("00:00");
////                        init=0;
//                            break;
//                        case 3:
//
//                            break;
//                    }
//                }
//            });



//        Bundle bundle = getIntent().getExtras();
//        final String[] list = bundle.getStringArray("string-array");
//        final String[] Lat = bundle.getStringArray("Lat");
//        final String[] Lng = bundle.getStringArray("Lng");
//        final String[] available = bundle.getStringArray("available");
//        final String[] total = bundle.getStringArray("total");
    }
    public void getUbikeAPI(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://youbike.pureinformation.net/manifest")
                .build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                        Intent intent = new Intent();
//                       intent.putExtra("json1", response.body().string());
                String mJson = response.body().string();
                Gson gson = new Gson();
//                    MainActivity.Data data = gson.fromJson(mJson, MainActivity.Data.class);
                Ubike.Data data=gson.fromJson(mJson,Ubike.Data.class);

                String[] list_item = new String[data.features.length];
                String[] Lat = new String[data.features.length];
                String[] Lng = new String[data.features.length];
                String[] title_name = new String[data.features.length];
                String[] title_total = new String[data.features.length];
                String[] title_available = new String[data.features.length];
                String[] stationId =new String[data.features.length];
                Log.e("我跑了","1次");
                for (int i = 0; i < data.features.length; i++) {
                    Lat[i] = new String();
                    Lng[i] = new String();
                    list_item[i] = new String();
                    title_name[i] = new String();
                    title_total[i] = new String();
                    title_available[i] =new String();
                    stationId[i]=new String();

                    Lat[i] += data.features[i].geometry.coordinates[0];
                    Lng[i] += data.features[i].geometry.coordinates[1];
                    list_item[i] += "\n車站名稱: " +data.features[i].properties.name;
                    list_item[i] += "\n車輛總數: " + data.features[i].properties.total;
                    list_item[i] += "\n可租借數: " + data.features[i].properties.available;
                    stationId[i] = data.features[i].id;
                    title_name[i] =data.features[i].properties.name;
                    title_total[i] = data.features[i].properties.total;
                    title_available[i] = data.features[i].properties.available;

                    Lat_all = Lat;
                    Lng_all = Lng;
                    list_item_all = list_item;
                    stationId_all = stationId;
                    title_name_all = title_name;
                    title_total_all = title_total;
                    title_available_all = title_available;

                    Log.e("string",data.features[i].properties.name);
                }

            }
        });

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < title_name_all.length; i++) {
            Double j = Double.parseDouble(Lat_all[i]);
            Double k = Double.parseDouble(Lng_all[i]);
            Pois.add(new Poi(title_name_all[i],title_total_all[i],title_available_all[i],k, j));
        }

        MyDBHelper dbHelper = new MyDBHelper(Ubike.this);
        dbrw = dbHelper.getWritableDatabase();

        //通过Resource方式设置背景图片
        final TextView textView =(TextView)findViewById(R.id.timer);

        final TextView moneytext =(TextView)findViewById(R.id.money);
        final int money=5;

        textView.setBackgroundResource(R.drawable.timer);
        //设置透明
        textView.getBackground().setAlpha(200);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startflag=true;
                init++;
                String a =String.valueOf(init);
                Log.e("agn",a);
                switch (init) {
                    case 1:
                    //取得目前時間
                        startTime = System.currentTimeMillis();
                    //設定定時要執行的方法
                        handler.removeCallbacks(updateTimer);
                    //設定Delay的時間
                        handler.postDelayed(updateTimer,1000);
                        moneytext.setText("$"+money);
                        break;
                    case 2:
                        TimerStop();
                        long x=Minius%30;
                        moneytext.setText("$"+((x*10)+5));
                        break;
                    case 3:
                        //初始化
                        moneytext.setText("$");
                        textView.setText("0分");
                        init=0;
                        break;
                }
            }
        });


    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView time = (TextView) findViewById(R.id.timer);
            Long spentTime = System.currentTimeMillis() - startTime;
            //計算目前已過分鐘數
            Long minius = (spentTime/1000)/60;
            Minius=minius;
            //計算目前已過秒數
            Long seconds = (spentTime/1000) % 60;
            handler.postDelayed(this,1000);
            time.setText(minius+"分");
        }
    };

    protected void TimerStop() {
        //將執行緒銷毀掉
        handler.removeCallbacks(updateTimer);

//    private Runnable updateTimer = new Runnable() {
//        public void run() {
//            final TextView time = (TextView) findViewById(R.id.timer);
//            Long spentTime = System.currentTimeMillis() - startTime;
//            //計算目前已過分鐘數
//            Long minius = (spentTime/1000)/60;
//            //計算目前已過秒數
//            Long seconds = (spentTime/1000) % 60;
////            if(init==1){
//            time.setText(minius+":"+seconds);
////            }
////            else {
////                time.setText("00:00");
////                init=0;
////                TimerDestroy();
////            }
//
//            handler.postDelayed(this, 1000);
//        }
//    };

//    protected void TimerDestroy() {
//    //將執行緒銷毀掉
//        handler.removeCallbacks(updateTimer);
//        super.onDestroy();
//    }

    }

    private void ShowStopStation(){
        Map.clear();
        for (int i = 0; i < title_name_all.length; i++) {
            int j = Integer.parseInt(title_available_all[i]);
            int k = Integer.parseInt(title_total_all[i]);
            int resource = k - j;
            String title = "\n車站名稱: " +title_name_all[i];
            String subTitle = "車輛總數: " +title_total_all[i]+"\n可租借數: " +title_available_all[i]+"\n查看更多";
            Double nLat = Double.parseDouble(Lat_all[i]);
            Double nLng = Double.parseDouble(Lng_all[i]);
            if (resource != 0) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(nLng, nLat))
                        .title(title)
                        .snippet(subTitle)
                        .icon(BitmapDescriptorFactory.fromBitmap(CreatMarker(GetDrawable(Integer.parseInt(title_total_all[i]),Integer.parseInt(title_available_all[i]),"park"),100,100)));
                Map.addMarker(markerOptions);
            }
        }
    }
    private void ShowRentStation(){
        Map.clear();
        for (int i = 0; i < title_name_all.length; i++) {
            int j = Integer.parseInt(title_available_all[i]);
            String title = "\n車站名稱: " +title_name_all[i];
            String subTitle = "車輛總數: " +title_total_all[i]+"\n可租借數: " +title_available_all[i]+"\n查看更多";
            Double nLat = Double.parseDouble(Lat_all[i]);
            Double nLng = Double.parseDouble(Lng_all[i]);
            if (j != 0) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions
                        .position(new LatLng(nLng, nLat))
                        .title(title)
                        .snippet(subTitle)
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(CreatMarker(GetDrawable(Integer.parseInt(title_total_all[i]),Integer.parseInt(title_available_all[i]),"rent"),100,100)));
                Map.addMarker(markerOptions);
            }
        }
    }
    private void dopoisort(){
        for (int b = 0; b < title_name_all.length; b++) {
            Double j = Double.parseDouble(Lat_all[b]);
            Double k = Double.parseDouble(Lng_all[b]);
            Pois.add(new Poi(title_name_all[b], title_total_all[b], title_available_all[b], k, j));
        }
    }
    private void ShowAllStation(){
        for (int i = 0; i < title_name_all.length; i++) {
            Double j = Double.parseDouble(Lat_all[i]);
            Double k = Double.parseDouble(Lng_all[i]);
//            Pois.add(new Poi(list_item_all[i], k, j));
        }

        for (int i = 0; i < title_name_all.length; i++) {
            String title = "\n車站名稱: " +title_name_all[i];
            String subTitle = "車輛總數: " +title_total_all[i]+"\n可租借數: " +title_available_all[i]+"\n查看更多";
            Double nLat = Double.parseDouble(Lat_all[i]);
            Double nLng = Double.parseDouble(Lng_all[i]);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(nLng, nLat))
                    .title(title)
                    .snippet(subTitle);
//                    .icon(BitmapDescriptorFactory
//                        .fromBitmap(CreatMarker(GetDrawable(Integer.parseInt(title_total_all[i]),Integer.parseInt(title_available_all[i]),"park"),60,90)));
            Map.addMarker(markerOptions);
        }
    }

    private Bitmap CreatMarker(int resource, int width, int height) {
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(resource);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }
    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals(" ")) {
            Geocoder geocoder = new Geocoder(Ubike.this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            Map.addMarker(new MarkerOptions().position(latLng).title(location));
            Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
        }else{Toast.makeText(Ubike.this,"請輸入地點",Toast.LENGTH_SHORT);
            ShowRentStation();
        }
    }
    //重写onCreateOptionMenu(Menu menu)方法，当菜单第一次被加载时调用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //填充选项菜单（读取XML文件、解析、加载到Menu组件上）
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        //通过代码的方式来添加Menu
        //添加菜单项（组ID，菜单项ID，排序，标题）
//        menu.add(0, START_ITEM, 100, "Start");
//        menu.add(0, OVER_ITEM, 200, "Over");

        return true;
    }
    //重写OptionsItemSelected(MenuItem item)来响应菜单项(MenuItem)的点击事件（根据id来区分是哪个item）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_favorite:
                setContentView(R.layout.list);
                TextView Title = (TextView)findViewById(R.id.title);
                TextView Distance = (TextView)findViewById(R.id.time);
                String index = "sequence\n",title="bookname\n",distance="distance\n";
                String[] colum={"title","distance"};

                Cursor cursor;
                cursor=dbrw.query("myTable",colum,null,null,null,null,null);

                if(cursor.getCount()>0){
                    cursor.moveToFirst();

                    for(int i=0;i<cursor.getCount();i++){
                        index += (i+1)+"\n";
                        title += cursor.getString(0)+"\n";
                        distance += cursor.getString(1)+"\n";
                        cursor.moveToNext();
                    }
                    Title.setText(title);
                    Distance.setText(distance);
                }
                break;
            case R.id.action_list:
                setContentView(R.layout.list);
                listV=(ListView)findViewById(R.id.listview01);

//                button=(Button)findViewById(R.id.favourite);

                button=(Button)findViewById(R.id.favourite);


                clearList(ubike_list);
                for (int i = 0; i < list_item_all.length; i++) {
                    ubike_list.add(new UbikeList(0, Pois.get(i).getName()
                            ,"車輛總數:"+Pois.get(i).getTotal()
                            ,"可租借數:"+Pois.get(i).getAvailable()
                            ,"距離   :"+DistanceText(Pois.get(i).getDistance())));
                }
                adapter = new MyAdapter(Ubike.this,ubike_list,0);
                listV.setAdapter(adapter);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void listOnItemselected(){
        listV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void myDatabase(){
        ContentValues cv =new ContentValues();
        for(int i=0;i<title_name_all.length;i++) {
            cv.put("title", title_name_all[i]);
            cv.put("favorite","0");
            dbrw.insert("myTable", null, cv);
        }
    }
    private void setMapList() {

        ListStr = Ubike.this.getResources().getStringArray(R.array.mapList);
        final AlertDialog setPhotoDialog = new AlertDialog.Builder(Ubike.this).create();
        setPhotoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setPhotoDialog.show();

        LayoutInflater inflater = LayoutInflater.from(Ubike.this);
        View setPhotoView = inflater.inflate(
                R.layout.personal_setuser_photo_dialog, null);
        setPhotoDialog.setContentView(setPhotoView);

        ListSetUserPhotoDialogAdapter setUserPhotoArrayAdapter = new ListSetUserPhotoDialogAdapter(Ubike.this,
                ListStr);
        ListView setPhoto_listView = (ListView) setPhotoView
                .findViewById(R.id.setuser_photo_list);
        setPhoto_listView.setSelector(R.drawable.list_item_selector);
        setPhoto_listView.setAdapter(setUserPhotoArrayAdapter);

        setPhoto_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int position, long id) {
                if (position == 0) {
                    try {
                        Map.clear();
                        setPhotoDialog.cancel();

                        MarkerOptions end = new MarkerOptions();
                        end.position(markerPosition)
                                .title("\n車站名稱: " +title_name_all[getID(title_name_all,on_click_marker_name)])
                                .snippet("車輛總數: " +title_total_all[getID(title_name_all,on_click_marker_name)]+"\n可租借數: " +title_available_all[getID(title_name_all,on_click_marker_name)]+"\n查看更多")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        Map.addMarker(end);
                        donavigation(myPosition,markerPosition,"walking");   //走路導航路線
//                                donavigation(myPosition,markerPosition,"driving"); //開車導航路線
//                                donavigation(myPosition,markerPosition,"transit"); //捷運導航路線
                    } catch (ActivityNotFoundException anfe) {
                        Log.e("內部導航","沒動作");
                    }
                } else if (position == 1) {
                    try {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+markerPosition.latitude
                                +","+markerPosition.longitude+"&mode=d");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    } catch (ActivityNotFoundException anfe) {
                        Log.e("跳googleMap","沒動作");
                    }
                }else if (position == 2) {
                    setPhotoDialog.cancel();
                }
            }
        });
    }
    public int getID(String[] one, String two){
//        ID= Arrays.binarySearch(title_name_all,on_click_marker_name);
        for (int i=0;i<one.length;i++){
            if(one[i].toString().equals(two)){
                ID=i;
                break;
            }else {
                ID=0;
            }
        }
        return ID;
    }
    public int GetDrawable(int total,int available,String str){
        int drawable=R.drawable.bike_park_1;
        int park,rent;
        park=total-available;
        rent=available;
        if (str.toString().equals("park")){
            drawable = drawable_bike_park[park-1];
        }else if (str.toString().equals("rent")){
            drawable = drawable_bike_rent[rent-1];
        }
     return drawable;
    }
    public void clearList(List<UbikeList> ubike_list) {
        int size = ubike_list.size();
        if (size > 0) {
            ubike_list.removeAll(ubike_list);
            adapter.notifyDataSetChanged();
        }
    }

    //物件類別class，名稱為Poi
    public class Poi {
        private String Name;         //名稱
        private double Latitude;    //緯度
        private double Longitude;   //經度
        private double Distance;    //距離
        private String Total;
        private String Available;
        private int Favorite=0;     //最愛

        //建立物件時需帶入名稱、緯度、經度
        public Poi(String name,String total,String available,double latitude, double longitude) {
            //將資訊帶入類別屬性
            Name = name;
            Total =total;
            Available = available;
            Latitude = latitude;
            Longitude = longitude;

        }

        //取得名稱
        public String getName() {
            return Name;
        }
        public String getTotal(){return Total;}
        public String getAvailable(){return Available;}
        //取得緯度
        public double getLatitude() {
            return Latitude;
        }

        //取得經度
        public double getLongitude() {
            return Longitude;
        }

        //寫入距離
        public void setDistance(double distance) {
            Distance = distance;
        }

        //取的距離
        public double getDistance() {
            return Distance;
        }
        public int getfavorite(){
            return Favorite;
        }
    }

    public LocationListener LocationChange = new LocationListener() {
        public void onLocationChanged(Location mLocation) {
            for (Poi mPoi : Pois) {
//                for迴圈將距離帶入，判斷距離為Distance function
//                需帶入使用者取得定位後的緯度、經度、緯度、經度。
                mPoi.setDistance(Distance(mLocation.getLatitude(),
                        mLocation.getLongitude(),
                        mPoi.getLatitude(),
                        mPoi.getLongitude()));
            }

//            依照距離遠近進行List重新排列
            DistanceSort(Pois);

//            印出我的座標-經度緯度
            Log.e("TAG", "我的座標 - 經度 : " + mLocation.getLongitude() + "  , 緯度 : " + mLocation.getLatitude());

//            for迴圈，印出名稱及距離，並依照距離由近至遠排列
//            第一筆為最近的，最後一筆為最遠的
            for (int i = 0; i < Pois.size(); i++) {
                Log.e("TAG", "地點 : " + Pois.get(i).getName() + "  , 距離為 : " + DistanceText(Pois.get(i).getDistance()) );

            }
            LatLng position =new LatLng( mLocation.getLatitude(),mLocation.getLongitude());
            nowposition =  position;
            myPosition = position;
            String a = String.valueOf(nowposition);
            Log.e("position", a );
            Map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowposition,15));
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        askOpenLocationDialog = new AlertDialog.Builder(Ubike.this).setTitle(R.string.DailogTitleExitBlueNet)
//                .setPositiveButton("是",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Ubike.this.finish();
//                            }
//                        })
//                .setNegativeButton("否", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        askOpenLocationDialog.cancel();
//                    }
//                }).create();
//
//        askOpenLocationDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        askOpenLocationDialog.show();

        mLocationManager.removeUpdates(LocationChange);  //程式結束時停止定位更新
    }

    //帶入距離回傳字串 (距離小於一公里以公尺呈現，距離大於一公里以公里呈現並取小數點兩位)
    private String DistanceText(double distance) {
        if (distance < 1000) return String.valueOf((int) distance) + "m";
        else return new DecimalFormat("#.00").format(distance / 1000) + "km";
    }

    //List排序，依照距離由近開始排列，第一筆為最近，最後一筆為最遠
    private void DistanceSort(ArrayList<Poi> poi) {
        Collections.sort(poi, new Comparator<Poi>() {
            @Override
            public int compare(Poi poi1, Poi poi2) {
                return poi1.getDistance() < poi2.getDistance() ? -1 : 1;
            }
        });
    }

    //帶入使用者及點經緯度可計算出距離
    public double Distance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double radLatitude1 = latitude1 * Math.PI / 180;
        double radLatitude2 = latitude2 * Math.PI / 180;
        double l = radLatitude1 - radLatitude2;
        double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2)));
        distance = distance * 6378137.0;
        distance = Math.round(distance * 10000) / 10000;

        return distance;
    }
    private void donavigation(LatLng first,LatLng end,String mode){
        String url = getDirectionsUrl(first,end,mode);//第一個座標LatLng 導航至第二個座標LatLng
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions
        // API
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest,String mode) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        //travel Mode
//        String str_travel = "mode=" + travel;

        // Sensor enabled
//        String sensor = "sensor=false";

        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" +str_travel+"&"+ sensor;
        String parameters = str_origin + "&" + str_dest + "&sensor=false&language=zh-TW&mode="+mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;
        System.out.println("getDerectionsURL--->: " + url);
        return url;
    }

    /**
     * 從URL下載JSON資料的方法
     **/
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
//            Log.e("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * 解析JSON格式
     **/
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(15);  //導航路徑寬度
                lineOptions.color(Color.GREEN); //導航路徑顏色
            }
            // Drawing polyline in the Google Map for the i-th route
            Map.addPolyline(lineOptions);
        }
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode ==102){
//            runThread(3);
        }
    }
}


