package com.example.administrator.diaokes.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.location.AndroidLocationDataSource;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import com.esri.arcgisruntime.tasks.geocode.SuggestResult;
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.SQLServer.sqlConn;
import com.example.administrator.diaokes.db.FoundDate;
import com.example.administrator.diaokes.db.Routes;
import com.example.administrator.diaokes.spinner.ItemData;
import com.example.administrator.diaokes.spinner.SpinnerAdapter;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/9.
 */

public class navigation extends AppCompatActivity{
    private MapView mapView;
    private RouteTask mRouteTask;
    private RouteParameters mRouteParams;
    private Route mRoute;
    private SimpleLineSymbol mRouteSymbol;
    private SearchView editText;
    private SearchView editTextS;
    private GraphicsOverlay graphicsOverlay;
    private ProgressDialog mProgressDialog;
    private double[] doubles = new double[2];
    private double[] doublesend = new double[2];
    private LocatorTask mLocatorTask;
    private LocationDisplay mLocationDisplay;
    private Spinner mSpinner;
    private GeocodeParameters mAddressGeocodeParameters;
    private final String COLUMN_NAME_ADDRESS = "name";
    private final String[] mColumnNames = { BaseColumns._ID, COLUMN_NAME_ADDRESS };
    private PictureMarkerSymbol endSourceSymbol;
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};
    private int requestCode = 2;
    private Polyline polyline;
    private PointCollection pointCollection;
    private ImageView imageViewHome;

    private DrawerLayout drawerLayout;
    private ListView listView;
    private GraphicsOverlay selectGraphicRoute;
    private Handler handler;
    private ListView auto;
    private SimpleCursorAdapter suggestionAdapter;
    private String[] list = new String[5];
    private SearchView.SearchAutoComplete aa;
    private String locationmsg = "";
    private String starttime = "";
    private ImageView save;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);
        editTextS = findViewById(R.id.inputstart);
        editText = findViewById(R.id.inputend);
        editTextS.setQueryHint("输入起点");
        editText.setQueryHint("输入终点");
        aa = findViewById(R.id.search_src_text);
        aa.setThreshold(1);
        mapView = findViewById(R.id.mapView1);
        mSpinner = (Spinner) findViewById(R.id.spinner1);
        imageViewHome = findViewById(R.id.homewidget);
        drawerLayout = findViewById(R.id.drawes1);
        listView = findViewById(R.id.left_drawer);
        auto = findViewById(R.id.autoCom);
        save = findViewById(R.id.nav_save_route);

        pointCollection = new PointCollection(SpatialReferences.getWgs84());
        mLocationDisplay = mapView.getLocationDisplay();
        mLocationDisplay.setShowPingAnimation(true);
        mLocationDisplay.setUseCourseSymbolOnMovement(true);
        //获取活动传递数据
        Intent intent = getIntent();
        doubles = intent.getDoubleArrayExtra("location");
        if (doubles[0] != 0.0 && doubles[1] != 0.0){
            editTextS.setQuery("我的位置",false);
            editTextS.setIconifiedByDefault(false);
            editText.setFocusable(false);
            editText.setIconified(false);
            editText.clearFocus();
            editTextS.clearFocus();
            mLocationDisplay.startAsync();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            starttime = df.format(new Date()).toString();
        }
        LitePal.initialize(navigation.this);
/*
        if(mLocationDisplay.isStarted()){
            AndroidLocationDataSource androidLocationDataSource = new AndroidLocationDataSource(navigation.this);
            androidLocationDataSource.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10);
        }
        */
        List<Routes> list11 = DataSupport.findAll(Routes.class);
        DataSupport.deleteAll(Routes.class,"name == ?","第一次");
        sendRequest("http://192.168.1.103:8080/tomcats/myServlet");
        //构造一个PictureMarker
        BitmapDrawable pinDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.end);
        try{
            endSourceSymbol = PictureMarkerSymbol.createAsync(pinDrawable).get();
        }catch (InterruptedException | ExecutionException e){
            Log.e("nav", "Picture Marker Symbol error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Failed to load pin drawable.", Toast.LENGTH_LONG).show();
        }
        endSourceSymbol.setWidth(18);
        endSourceSymbol.setHeight(18);
        endSourceSymbol.loadAsync();

        //创建地理编码任务
        mLocatorTask = new LocatorTask(getResources().getString(R.string.locate_service));
        //设置路线样式
        mRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 5);

        //设置toolbar等
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar2);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        collapsingToolbarLayout.setTitle(" ");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //设置地图底图
        ArcGISTiledLayer arcGISTiledLayer = new ArcGISTiledLayer(getResources().getString(R.string.tileMap3857));
        Basemap basemap = new Basemap(arcGISTiledLayer);
        final ArcGISMap arcGISMap = new ArcGISMap(basemap);
        final Viewpoint viewpoint = new Viewpoint(25.8509722,114.94027822,50000);
        arcGISMap.setInitialViewpoint(viewpoint);
        mapView.setMap(arcGISMap);

        //创建几何层并添加
        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        selectGraphicRoute = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(selectGraphicRoute);

        //监听
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
                if (dataSourceStatusChangedEvent.isStarted()) {
                    return;
                }

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null)
                    return;

                // If an error is found, handle the failure to start.
                // Check permissions to see if failure may be due to lack of permissions.
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(navigation.this, reqPermissions[0]) ==
                        PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(navigation.this, reqPermissions[1]) ==
                        PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1 && permissionCheck2)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(navigation.this, reqPermissions, requestCode);
                }else {
                    String message = String.format("error in program");
                    Toast.makeText(navigation.this,message,Toast.LENGTH_LONG).show();
                    mSpinner.setSelection(0,true);
                }
            }
        });
        mLocationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
            @Override
            public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
                if(mLocationDisplay.isStarted()) {
                    LocationDataSource.Location location = locationChangedEvent.getLocation();
                    Point point = location.getPosition();
                    pointCollection.add(point);
                    locationmsg += point.getX() + "," + point.getY()+" ";
                    if (pointCollection.size() > 2) {
                        polyline = new Polyline(pointCollection);
                        Graphic graphic = new Graphic(polyline, new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 5));
                        graphicsOverlay.getGraphics().add(graphic);
                    }
                    Log.i("address",locationmsg);
                }
            }
        });

        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData("停止", R.drawable.locationdisplaydisabled));
        list.add(new ItemData("开始", R.drawable.locationdisplayon));
        list.add(new ItemData("定位", R.drawable.locationdisplayrecenter));
        list.add(new ItemData("导航", R.drawable.locationdisplaynavigation));
        list.add(new ItemData("罗盘", R.drawable.locationdisplayheading));

        SpinnerAdapter adapter = new SpinnerAdapter(navigation.this, R.layout.spinner_layout, R.id.txt, list);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        if (mLocationDisplay.isStarted()) {
                            editText.clearFocus();
                            editTextS.clearFocus();
                            mLocationDisplay.stop();
                        }
                        break;
                    case 1:
                        if (!mLocationDisplay.isStarted()) {
                            editText.clearFocus();
                            editTextS.clearFocus();
                            mLocationDisplay.startAsync();
                        }
                        break;
                    case 2:
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                        if (!mLocationDisplay.isStarted()) {
                            editText.clearFocus();
                            editTextS.clearFocus();
                            mLocationDisplay.startAsync();
                        }
                        break;
                    case 3:
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                        if (!mLocationDisplay.isStarted()) {
                            editText.clearFocus();
                            editTextS.clearFocus();
                            mLocationDisplay.startAsync();
                        }
                        break;
                    case 4:
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        if (!mLocationDisplay.isStarted()) {
                            editText.clearFocus();
                            editTextS.clearFocus();
                            mLocationDisplay.startAsync();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Viewpoint viewpoint1 = new Viewpoint(25.8509722,114.94027822,50000);
                mapView.setViewpointAsync(viewpoint1);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                final AlertDialog alertDialog;
                Context mContext = navigation.this;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.save_route_layout,null);
                builder = new AlertDialog.Builder(mContext);
                builder.setView(layout);
                alertDialog = builder.create();
                alertDialog.show();
                final TextView name = layout.findViewById(R.id.nav_route_name);
                final TextView content = layout.findViewById(R.id.nav_route_content);
                Button saveroute = layout.findViewById(R.id.nav_route_save);
                saveroute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(locationmsg != "") {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String data1 = df.format(new Date()).toString();
                            Routes routes = new Routes();
                            routes.setName(name.getText().toString());
                            routes.setContent(content.getText().toString());
                            routes.setStart(starttime);
                            routes.setTime(data1);
                            routes.setLocate(locationmsg);
                            routes.save();
                            Toast.makeText(navigation.this,"保存路线成功",Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
                        }else {
                            Toast.makeText(navigation.this,"当前地图没有路线",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_title));
        mProgressDialog.setMessage(getString(R.string.progress_message));

        setupAddressSearchView();
    }

    private void setupAddressSearchView(){
        mAddressGeocodeParameters = new GeocodeParameters();
        mAddressGeocodeParameters.getResultAttributeNames().add("name");
        mAddressGeocodeParameters.getResultAttributeNames().add("address");
        mAddressGeocodeParameters.setMaxResults(1);
        editText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                geoCodeTypedAddress(query);
                routeProcess(doubles);
                editText.clearFocus();
                editTextS.clearFocus();
                auto.setVisibility(View.INVISIBLE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if(!newText.equals("")){
                    auto.setVisibility(View.VISIBLE);
                    handler = new Handler(){
                        public void handleMessage(Message msg){
                            switch (msg.what){
                                case 1:
                                    list = (String[]) msg.obj;
                                    break;
                            }
                        }
                    };

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            list = sqlConn.QuerySQL(newText);
                            Message message = new Message();
                            message.what = 1;
                            message.obj = list;
                            handler.sendMessage(message);
                        }
                    };
                    new Thread(runnable).start();

                    final MatrixCursor suggestionsCursor = new MatrixCursor(mColumnNames);
                    int key = 0;
                    for(String item : list){
                        suggestionsCursor.addRow(new Object[]{key++,item});
                    }
                    setAdapter(suggestionsCursor);
                    auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            MatrixCursor selectedRow = (MatrixCursor) suggestionAdapter.getItem(i);
                            // get the row's index
                            int selectedCursorIndex = selectedRow.getColumnIndex("name");
                            // get the string from the row at index
                            String address = selectedRow.getString(selectedCursorIndex);
                            editText.setQuery(address,true);
                        }
                    });
                }
                return true;
            }
        });
    }



    private void setAdapter(Cursor cursor){
        if(auto.getAdapter() == null){
            suggestionAdapter = new SimpleCursorAdapter(navigation.this,R.layout.suggestion,cursor,new String[]{"name"},new int[]{R.id.suggestion_address},0);
            auto.setAdapter(suggestionAdapter);
        }else {
            ((SimpleCursorAdapter) auto.getAdapter()).changeCursor(cursor);
        }
    }
    private void routeProcess(final double[] doubles){
        mProgressDialog.show();
        graphicsOverlay.getGraphics().clear();
        selectGraphicRoute.getGraphics().clear();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mRouteTask = new RouteTask(getApplicationContext(),getResources().getString(R.string.route_service));
        final ListenableFuture<RouteParameters> listenableFuture = mRouteTask.createDefaultParametersAsync();
        listenableFuture.addDoneListener(new Runnable() {
            @Override
            public void run(){
                try {
                    if (listenableFuture.isDone()) {
                        int i =0;
                        mRouteParams = listenableFuture.get();
                        Stop stop1 = new Stop(new Point(doubles[1],doubles[0], SpatialReferences.getWgs84()));
                        Stop stop2 = new Stop(new Point(doublesend[1],doublesend[0],SpatialReferences.getWgs84()));
                        List<Stop> routeStops = new ArrayList<>();
                        routeStops.add(stop1);
                        routeStops.add(stop2);
                        mRouteParams.setStops(routeStops);
                        mRouteParams.setReturnDirections(true);
                        RouteResult routeResult = mRouteTask.solveRouteAsync(mRouteParams).get();
                        final List routes = routeResult.getRoutes();
                        mRoute = (Route) routes.get(0);
                        Graphic routeGraphic = new Graphic(mRoute.getRouteGeometry(), mRouteSymbol);
                        graphicsOverlay.getGraphics().add(routeGraphic);

                        final List<DirectionManeuver> directions = mRoute.getDirectionManeuvers();
                        String[] directionsArr = new String[directions.size()];

                        for(DirectionManeuver dm : directions){
                            directionsArr[i++] = dm.getDirectionText();
                        }

                        listView.setAdapter(new ArrayAdapter<>(navigation.this,R.layout.directions_layout,directionsArr));
                        if(mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (selectGraphicRoute.getGraphics().size()>0){
                                    selectGraphicRoute.getGraphics().clear();
                                }
                                drawerLayout.closeDrawers();
                                DirectionManeuver dm = directions.get(i);
                                Geometry gm = dm.getGeometry();
                                Viewpoint vp = new Viewpoint(gm.getExtent(),20);
                                mapView.setViewpointAsync(vp,3);
                                SimpleLineSymbol selectedRoute = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,Color.GREEN,5);
                                Graphic graphic = new Graphic(directions.get(i).getGeometry(),selectedRoute);
                                selectGraphicRoute.getGraphics().add(graphic);
                            }
                        });
                    }
                }catch (Exception e){
                    Log.e("Main", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mLocationDisplay.startAsync();
        }else {
            Toast.makeText(navigation.this,"refuse permission",Toast.LENGTH_LONG).show();
            mSpinner.setSelection(0,true);
        }
    }

    private void geoCodeTypedAddress(final String address){
        if(address != null){
            mLocatorTask.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    if(mLocatorTask.getLoadStatus() == LoadStatus.LOADED){
                        final ListenableFuture<List<GeocodeResult>> geocodeResult = mLocatorTask.geocodeAsync(address,mAddressGeocodeParameters);
                        geocodeResult.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    List<GeocodeResult> geocodeResults = geocodeResult.get();
                                    if(geocodeResults.size() > 0){
                                        displaySearchResult(geocodeResults.get(0));
                                    }else {
                                        Toast.makeText(navigation.this,"没有找到位置"+address,Toast.LENGTH_LONG).show();
                                    }
                                }catch (InterruptedException | ExecutionException e){
                                    Log.e("nav","Geocode error:"+e.getMessage());
                                    Toast.makeText(navigation.this,"定位器错误",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else {
                        Log.i("nav","重新执行定位器任务");
                        mLocatorTask.retryLoadAsync();
                    }
                }
            });
            mLocatorTask.loadAsync();
        }
    }
    private void displaySearchResult(GeocodeResult geocodeResult){
        Point point = geocodeResult.getDisplayLocation();
        doublesend[1] = point.getX();
        doublesend[0] = point.getY();
        Graphic graphic = new Graphic(point,geocodeResult.getAttributes(),endSourceSymbol);
        graphicsOverlay.getGraphics().add(graphic);
    }
    private void sendRequest(final String url){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id","1")
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String re = response.body().string();
                    String s = re;
                }catch (Exception e){
                    Log.e("错误信息",e.getMessage());
                }
            }
        };
        new Thread(runnable).start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
