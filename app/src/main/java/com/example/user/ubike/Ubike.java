package com.example.user.ubike;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
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
import java.util.ArrayList;
import java.util.Arrays;
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
    private com.google.android.gms.maps.GoogleMap Map;
    private ListView listV;
    private List<UbikeList> ubike_list = new ArrayList<UbikeList>();
    private String[] ListStr = new String[2];
    private MyAdapter adapter;
    private AlertDialog askOpenLocationDialog;
    private Handler handler = new Handler();
    private Long startTime;
    private LatLng nowposition ,markerPosition,myPosition;
    SQLiteDatabase dbrw;
    private String list_item_all[] ,Lat_all[] ,Lng_all[] ,title_name_all[] ,title_total_all[] ,title_available_all[] ,stationId_all[];
    private String on_click_marker_name;
    private int ID;            //站編號
    private Button button;

    private int init= 0;
    private boolean startflag=false;
    private int tsec=0,csec=0,cmin=0;
    private TextView textView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.googlemap);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final TextView textView =(TextView)findViewById(R.id.timer);

//        Bundle bundle = getIntent().getExtras();
//        final String[] list = bundle.getStringArray("string-array");
//        final String[] Lat = bundle.getStringArray("Lat");
//        final String[] Lng = bundle.getStringArray("Lng");
//        final String[] available = bundle.getStringArray("available");
//        final String[] total = bundle.getStringArray("total");

//        askOpenLocation("請開啟GPS定位");

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
            Thread.sleep(5000);
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

//        //宣告Timer
//        Timer timer01 =new Timer();
//        //設定Timer(task為執行內容，0代表立刻開始,間格1秒執行一次)
//        timer01.schedule(task, 0,1000);

        //通过Resource方式设置背景图片
        textView.setBackgroundResource(R.drawable.timer);
        //设置透明
        textView.getBackground().setAlpha(200);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startflag=true;
                init++;
                switch (init) {
                    case 1:
                    //取得目前時間
                        String a =String.valueOf(init);
                        Log.e("agn",a);
                    startTime = System.currentTimeMillis();
                    //設定定時要執行的方法
                    handler.removeCallbacks(updateTimer);
                    //設定Delay的時間
                    handler.postDelayed(updateTimer, 1000);
                        break;
                    case 2:
//                        TimerDestroy();
//                        textView.setText("00:00");
//                        init=0;
                        break;
                    case 3:

                        break;
                }
            }
        });

        final ToggleButton mToggleButton = (ToggleButton) findViewById(R.id.mToggleButton);
        mToggleButton.setChecked(false);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

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
                ShowRentStation();
                mToggleButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (mToggleButton.isChecked()) {    // 當按鈕第一次被點擊時候響應的事件
                            ShowRentStation();
                        }
                        else {                              // 當按鈕再次被點擊時候響應的事件
                            ShowStopStation();
                        }
                    }
                });
//                googleMap.set
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
                        marker_title =arg0.getTitle();
                        String marker_title_name;
                        marker_title_name = marker_title.substring(7,marker_title.length());
                        on_click_marker_name = marker_title_name;
                        setMapList();
                    }
                });
            }
        });
    }
    public void askOpenLocation(final String text) {
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                PowerManager pm = (PowerManager) Ubike.this.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "My_App");

                wl.acquire();

                Toast.makeText(Ubike.this,text, Toast.LENGTH_SHORT).show();

                if(askOpenLocationDialog!=null)askOpenLocationDialog.cancel();
                askOpenLocationDialog = new AlertDialog.Builder(Ubike.this).setTitle("未獲得定位資訊")
                        .setMessage(text+"\n"//
                                +"是否啟用定位功能？")
                        .setPositiveButton("啟用",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 使用Intent物件啟動設定程式來更改GPS設定
                                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Ubike.this.startActivity(i);
                                        askOpenLocationDialog.cancel();
                                    }
                                })
                        .setNegativeButton("不啟用", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askOpenLocationDialog.cancel();
                            }
                        }).create();

                askOpenLocationDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                askOpenLocationDialog.show();
                wl.release();
                Looper.loop();
            }
        }).start();
    }
    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView time = (TextView) findViewById(R.id.timer);
            Long spentTime = System.currentTimeMillis() - startTime;
            //計算目前已過分鐘數
            Long minius = (spentTime/1000)/60;
            //計算目前已過秒數
            Long seconds = (spentTime/1000) % 60;
//            if(init==1){
            time.setText(minius+":"+seconds);
//            }
//            else {
//                time.setText("00:00");
//                init=0;
//                TimerDestroy();
//            }

            handler.postDelayed(this, 1000);
        }
    };

    protected void TimerDestroy() {
    //將執行緒銷毀掉
        handler.removeCallbacks(updateTimer);
        super.onDestroy();
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
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
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
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                Map.addMarker(markerOptions);
            }
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
            Map.addMarker(markerOptions);
        }
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(Ubike.this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            Map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
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
            case R.id.action_compose:
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
            case R.id.action_delete:
                setContentView(R.layout.list);
                listV=(ListView)findViewById(R.id.listview01);
                button=(Button)findViewById(R.id.favourite);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                clearList(ubike_list);
                for (int i = 0; i < list_item_all.length; i++) {
                    ubike_list.add(new UbikeList(0, Pois.get(i).getName()
                            ,"車輛總數:"+Pois.get(i).getTotal()
                            ,"可租借數:"+Pois.get(i).getAvailable()
                            ,DistanceText(Pois.get(i).getDistance())));
                }
                adapter = new MyAdapter(Ubike.this,ubike_list);
                listV.setAdapter(adapter);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

        setPhoto_listView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View v,
                                            int position, long id) {
                        if (position == 0) {
                            try {

                            } catch (ActivityNotFoundException anfe) {
//                                Toast.makeText(Ubike.this, "加入最愛沒動作", Toast.LENGTH_SHORT).show();
                                Log.e("加入我的最愛","沒動作");
                            }
                        } else if (position == 1) {
                            try {
                                Map.clear();
                                setPhotoDialog.cancel();

                                MarkerOptions end = new MarkerOptions();
                                end.position(markerPosition)
                                        .title("\n車站名稱: " +title_name_all[getID()])
                                        .snippet("車輛總數: " +title_total_all[getID()]+"\n可租借數: " +title_available_all[getID()]+"\n查看更多")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                Map.addMarker(end);
                                donavigation(myPosition,markerPosition,"walking");   //走路導航路線
//                                donavigation(myPosition,markerPosition,"driving"); //開車導航路線
//                                donavigation(myPosition,markerPosition,"transit"); //捷運導航路線
                            } catch (ActivityNotFoundException anfe) {
                                Log.e("內部導航","沒動作");
                            }
                        }else if (position == 2) {
                            try {
                                Uri gmmIntentUri = Uri.parse("google.navigation:q="+markerPosition.latitude
                                        +","+markerPosition.longitude+"&mode=d");
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            } catch (ActivityNotFoundException anfe) {
                                Log.e("跳googleMap","沒動作");
                            }
                        }else if (position == 3) {
                            setPhotoDialog.cancel();
                        }

                    }
                });
    }
    public  int getID(){
//        ID= Arrays.binarySearch(title_name_all,on_click_marker_name);
        for (int i=0;i<title_name_all.length;i++){
            if(title_name_all[i].toString().equals(on_click_marker_name)){
                ID=i;
                break;
            }else {
                ID=0;
            }
        }
        return ID;
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
//        mLocationManager.removeUpdates(LocationChange);  //程式結束時停止定位更新
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
}


