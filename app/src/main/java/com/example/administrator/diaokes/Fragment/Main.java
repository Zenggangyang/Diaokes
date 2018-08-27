package com.example.administrator.diaokes.Fragment;


import android.Manifest;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esri.arcgisruntime.LicenseStatus;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.CoordinateFormatter;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.popup.Popup;
import com.esri.arcgisruntime.mapping.popup.PopupDefinition;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureFillSymbol;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.UniqueValueRenderer;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.example.administrator.diaokes.Add.addaddress;
import com.example.administrator.diaokes.Add.addshop;
import com.example.administrator.diaokes.MainActivity;
import com.example.administrator.diaokes.Navigation.navigation;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.Rank.rank;
import com.example.administrator.diaokes.db.addressdate;
import com.example.administrator.diaokes.db.shopitem;
import com.example.administrator.diaokes.db.shops;
import com.example.administrator.diaokes.spinner.ItemData;
import com.example.administrator.diaokes.spinner.SpinnerAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/6/27.
 */

public class Main extends Fragment {
    private MapView mapView;
    private LocationDisplay mLocationDisplay;
    private int requestCode = 2;
    private ServiceFeatureTable serviceFeatureTable2;
    private FeatureLayer featureLayer2;
    private FeatureLayer featureLayer1;
    private ServiceFeatureTable serviceFeatureTable;
    private android.graphics.Point mClickPoint;
    private ArcGISFeature mSelectedArcGISFeature;
    private Graphic mGraphic;
    private Callout mCallout;
    private FloatingActionButton mDirectionFab;
    private GraphicsOverlay graphicsOverlay;
    private double[] doubles = new double[2];
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};
    private ImageView imageView;
    private ImageView home;
    private ImageView add;
    private PictureMarkerSymbol shopSourceSymbol;
    private PictureMarkerSymbol fishSourceSymbol;
    private shops item;
    private addressdate item1;
    private static String username = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_layout,container,false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mDirectionFab = (FloatingActionButton) view.findViewById(R.id.routeTask);
        imageView = view.findViewById(R.id.spinner);
        home = view.findViewById(R.id.main_home);
        add = view.findViewById(R.id.main_add);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getActivity().getResources().getColor(R.color.haokan));

        final SearchView searchView = view.findViewById(R.id.addressSearchView);
        searchView.setQueryHint("请输入查询地点...");
        searchView.setSubmitButtonEnabled(true);
        if(searchView != null){
            try {
                Class<?> argClass = searchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                java.lang.reflect.Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchView);
                //--设置背景
                mView.setBackgroundResource(R.drawable.recshape);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForState(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("TAG","内容: " + newText);
                return true;
            }
        });

        ArcGISTiledLayer arcGISTiledLayer = new ArcGISTiledLayer(getResources().getString(R.string.tileMap38571));
        Basemap basemap = new Basemap(arcGISTiledLayer);
        ArcGISMap arcGISMap = new ArcGISMap(basemap);
        serviceFeatureTable = new ServiceFeatureTable(getResources().getString(R.string.feature));
        featureLayer1 = new FeatureLayer(serviceFeatureTable);
        arcGISMap.getOperationalLayers().add(featureLayer1);

        serviceFeatureTable2 = new ServiceFeatureTable(getResources().getString(R.string.alladdress));
        featureLayer2 = new FeatureLayer(serviceFeatureTable2);
        arcGISMap.getOperationalLayers().add(featureLayer2);

        Viewpoint viewpoint = new Viewpoint(25.85097,114.940278,14);
        arcGISMap.setInitialViewpoint(viewpoint);
        mapView.setMap(arcGISMap);
        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        BitmapDrawable pinDrawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.yu);
        try{
            shopSourceSymbol = PictureMarkerSymbol.createAsync(pinDrawable).get();
        }catch (InterruptedException | ExecutionException e){
            Log.e("nav", "Picture Marker Symbol error: " + e.getMessage());
            Toast.makeText(getActivity(), "Failed to load pin drawable.", Toast.LENGTH_LONG).show();
        }

        BitmapDrawable pinDrawable1 = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.fishshop);
        try {
            fishSourceSymbol = PictureMarkerSymbol.createAsync(pinDrawable1).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("nav", "Picture Marker Symbol error: " + e.getMessage());
            Toast.makeText(getActivity(), "Failed to load pin drawable.", Toast.LENGTH_LONG).show();
        }

        UniqueValueRenderer uniqueValRenderer = new UniqueValueRenderer(null, null, null, fishSourceSymbol);
        uniqueValRenderer.getFieldNames().add("type");

        List<Object> redValue = new ArrayList<>();
        redValue.add("渔具");
        UniqueValueRenderer.UniqueValue redUV = new UniqueValueRenderer.UniqueValue(null, null, fishSourceSymbol, redValue);
        uniqueValRenderer.getUniqueValues().add(redUV);

        List<Object> blueValue = new ArrayList<>();
        blueValue.add("钓场");
        UniqueValueRenderer.UniqueValue blueUV = new UniqueValueRenderer.UniqueValue(null, null, shopSourceSymbol, blueValue);
        uniqueValRenderer.getUniqueValues().add(blueUV);

        featureLayer2.setRenderer(uniqueValRenderer);

        inits();
        mCallout = mapView.getCallout();

       mDirectionFab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getActivity(),navigation.class);
               intent.putExtra("location",doubles);
               startActivity(intent);
           }
       });

       home.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Viewpoint viewpoint1 = new Viewpoint(25.8509722,114.94027822,50000);
               mapView.setViewpointAsync(viewpoint1);
           }
       });

       add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog.Builder builder;
               AlertDialog alertDialog;
               Context mContext = getContext();
               LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View layout = inflater.inflate(R.layout.add_layout,null);
               builder = new AlertDialog.Builder(mContext);
               builder.setView(layout);
               alertDialog = builder.create();
               alertDialog.show();
               ImageView shop = layout.findViewById(R.id.add_shop);
               ImageView address = layout.findViewById(R.id.add_address);

               shop.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent(getActivity(),addshop.class);
                       startActivity(intent);
                   }
               });
               address.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent(getActivity(),addaddress.class);
                       startActivity(intent);
                   }
               });
           }
       });

        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(getActivity(),mapView){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
                mClickPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());
                Point point = mapView.screenToLocation(mClickPoint);
                String strin = CoordinateFormatter.toLatitudeLongitude(point,CoordinateFormatter.LatitudeLongitudeFormat.DECIMAL_DEGREES,4);

                Point point1 = new Point(Float.parseFloat((strin.split(" ")[1].split("E")[0])),Float.parseFloat((strin.split("N")[0])) ,SpatialReferences.getWgs84());
                //Graphic graphic = new Graphic(point1,symbol);
                //graphicsOverlay.getGraphics().add(graphic);
                //android.graphics.Point point2 = new android.graphics.Point((int) point1.getX(),(int) point1.getY());
                featureLayer2.clearSelection();
                graphicsOverlay.clearSelection();
                mSelectedArcGISFeature = null;
                mCallout.dismiss();

                final ListenableFuture<IdentifyLayerResult> identifyFeature = mapView.identifyLayerAsync(featureLayer2,mClickPoint,20,false,1);
                identifyFeature.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            IdentifyLayerResult identifyLayerResult = identifyFeature.get();
                            List<GeoElement> retureGeoElements = identifyLayerResult.getElements();

                            if(retureGeoElements.size()>0){
                                if(retureGeoElements.get(0) instanceof ArcGISFeature){
                                    mSelectedArcGISFeature = (ArcGISFeature) retureGeoElements.get(0);
                                    featureLayer2.selectFeature(mSelectedArcGISFeature);
                                    String name = (String) mSelectedArcGISFeature.getAttributes().get("name");
                                    String address = (String)mSelectedArcGISFeature.getAttributes().get("address");
                                    String score = (String) mSelectedArcGISFeature.getAttributes().get("score");
                                    String type = (String) mSelectedArcGISFeature.getAttributes().get("type");
                                    String pay = (String) mSelectedArcGISFeature.getAttributes().get("pay");
                                    String content = (String) mSelectedArcGISFeature.getAttributes().get("content");
                                    String path = (String) mSelectedArcGISFeature.getAttributes().get("image_");
                                    showCallout(name,address,score,type,pay,content,path);
                                }
                            }else {
                                mCallout.dismiss();
                            }
                        }catch (Exception e){
                            Log.e("选择要素失败",e.getMessage());
                        }
                    }
                });

                final ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphic = mapView.identifyGraphicsOverlayAsync(graphicsOverlay, mClickPoint, 10.0, false, 2);
                identifyGraphic.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdentifyGraphicsOverlayResult grOverlayResult = identifyGraphic.get();
                            // get the list of graphics returned by identify graphic overlay
                            List<Graphic> graphic = grOverlayResult.getGraphics();
                            // get size of list in results
                            int identifyResultSize = graphic.size();
                            if (!graphic.isEmpty()) {
                                // show a toast message if graphic was returned
                                graphic.get(0).setSelected(true);
                                mGraphic = graphic.get(0);
                                String name = (String) graphic.get(0).getAttributes().get("name");
                                String address = (String)graphic.get(0).getAttributes().get("address");
                                String lianxi = (String) graphic.get(0).getAttributes().get("lianxi");
                                String content = (String)graphic.get(0).getAttributes().get("detail");
                                String type = (String) graphic.get(0).getAttributes().get("type");
                                String costtype = (String) graphic.get(0).getAttributes().get("costtype");
                                String yu = (String) graphic.get(0).getAttributes().get("yu");
                                showCallout1(name,address,lianxi,type,yu,content,costtype);
                            }
                        } catch (InterruptedException | ExecutionException ie) {
                            ie.printStackTrace();
                        }
                    }
                });
                return super.onSingleTapConfirmed(e);
            }
        });

        mLocationDisplay = mapView.getLocationDisplay();
        //监听
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
                if (dataSourceStatusChangedEvent.isStarted())
                    return;

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null)
                    return;

                // If an error is found, handle the failure to start.
                // Check permissions to see if failure may be due to lack of permissions.
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(), reqPermissions[0]) ==
                        PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), reqPermissions[1]) ==
                        PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1 && permissionCheck2)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(getActivity(), reqPermissions, requestCode);
                }else {
                    String message = String.format("error in program");
                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                }
            }
        });
        mLocationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
            @Override
            public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
                if(mLocationDisplay.isStarted()) {
                    doubles[0] = mLocationDisplay.getLocation().getPosition().getY();
                    doubles[1] = mLocationDisplay.getLocation().getPosition().getX();
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                if (!mLocationDisplay.isStarted()) {
                    mLocationDisplay.startAsync();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(3000);
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("locate",doubles[0]+","+doubles[1])
                                        .add("name",username)
                                        .build();
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://192.168.1.103:8080/tomcats/updatelocate")
                                        .post(requestBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String data = response.body().string();
                                String s = data;
                            }catch (Exception e){
                                Log.e("错误信息",e.getMessage());
                            }
                        }
                    }).start();
                }
            }
        });
        return view;
    }

    private GraphicsOverlay addGraphicsOverlay(MapView mapView) {
        //create the graphics overlay
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        //add the overlay to the map view
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        return graphicsOverlay;
    }

    private void inits(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.1.103:8080/tomcats/queryshop")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    final String re = response.body().string();
                    final String s = re.substring(1,re.length()-3);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            item = gson.fromJson(s, shops.class);
                            for(int i = 0;i<item.getList().size();i++) {
                                Point point = new Point(Float.parseFloat(item.getList().get(i).getLocate().split(",")[0]),Float.parseFloat(item.getList().get(i).getLocate().split(",")[1]),SpatialReferences.getWgs84());
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("name",item.getList().get(i).getName());
                                map.put("address",item.getList().get(i).getAddress());
                                map.put("contact",item.getList().get(i).getLianxi());
                                map.put("detail",item.getList().get(i).getDetail());
                                Graphic graphic = new Graphic(point,map,shopSourceSymbol);
                                graphicsOverlay.getGraphics().add(graphic);
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("错误信息",e.getMessage());
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody1 = new FormBody.Builder()
                            .build();
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            .url("http://192.168.1.103:8080/tomcats/queryaddress")
                            .post(requestBody1)
                            .build();
                    Response response1 = client1.newCall(request1).execute();
                    final String re1 = response1.body().string();
                    final String s1 = re1.substring(1, re1.length() - 3);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson1 = new Gson();
                            item1 = gson1.fromJson(s1, addressdate.class);
                            for (int i = 0; i < item1.getList().size(); i++) {
                                Point point = new Point(Float.parseFloat(item1.getList().get(i).getLocate().split(",")[0]), Float.parseFloat(item1.getList().get(i).getLocate().split(",")[1]), SpatialReferences.getWgs84());
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("name", item1.getList().get(i).getName());
                                map.put("type", item1.getList().get(i).getType());
                                map.put("costtype", item1.getList().get(i).getCosttype());
                                map.put("yu", item1.getList().get(i).getYu());
                                map.put("address",item1.getList().get(i).getAddress());
                                map.put("lianxi", item1.getList().get(i).getLianxi());
                                map.put("detail", item1.getList().get(i).getDetail());
                                Graphic graphic = new Graphic(point, map, fishSourceSymbol);
                                graphicsOverlay.getGraphics().add(graphic);
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("错误信息",e.getMessage());
                }
            }
        }).start();

    }
    private void showCallout(String title,String address,String score,String type,String pay,String content,String path){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.callout_layout,null);
        TextView textView1 = view.findViewById(R.id.title1);
        textView1.setText("名称："+title);
        TextView textView2 = view.findViewById(R.id.call_address);
        textView2.setText("地址："+address);
        TextView textView3 = view.findViewById(R.id.call_score);
        textView3.setText("评分："+score);
        TextView textView4 = view.findViewById(R.id.call_type);
        textView4.setText("类型："+type);
        TextView textView5 = view.findViewById(R.id.call_pay);
        textView5.setText("收费类型："+pay);
        TextView textView6 = view.findViewById(R.id.call_content);
        textView6.setText("描述："+content);
        ImageView imageView = view.findViewById(R.id.call_img);
        if(!path.equals(" ")){
            Glide.with(getContext()).load(path).into(imageView);
        }else {
            Glide.with(getContext()).load(R.drawable.error).into(imageView);
        }
        mCallout.setGeoElement(mSelectedArcGISFeature,null);
        mCallout.setContent(view);
        /*
        RelativeLayout relativeLayout = new RelativeLayout(getActivity().getApplicationContext());
        TextView textView = new TextView(getActivity().getApplicationContext());
        textView.setId(R.id.textview);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(18);

        textView.setText(title);

        relativeLayout.addView(textView);
        mCallout.setGeoElement(mSelectedArcGISFeature,null);
        mCallout.setContent(relativeLayout);
        */
        mCallout.show();
    }

    private void showCallout1(String title,String address,String lianxi,String type,String yu,String content,String cost){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.callout_layout_new,null);
        TextView textView1 = view.findViewById(R.id.title2);
        textView1.setText("名称："+title);
        TextView textView2 = view.findViewById(R.id.call_address_new);
        textView2.setText("地址："+address);
        TextView textView3 = view.findViewById(R.id.call_lianxi_new);
        textView3.setText("联系方式："+lianxi);
        TextView textView4 = view.findViewById(R.id.call_type_new);
        textView4.setText("类型："+type);
        TextView textView5 = view.findViewById(R.id.call_yu_new);
        textView5.setText("鱼类型："+yu);
        TextView textView6 = view.findViewById(R.id.call_detail_new);
        textView6.setText("描述："+content);
        TextView textView7 = view.findViewById(R.id.call_cost_new);
        textView7.setText("收费类型："+cost);
        mCallout.setGeoElement(mGraphic,null);
        mCallout.setContent(view);
        /*
        RelativeLayout relativeLayout = new RelativeLayout(getActivity().getApplicationContext());
        TextView textView = new TextView(getActivity().getApplicationContext());
        textView.setId(R.id.textview);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(18);

        textView.setText(title);

        relativeLayout.addView(textView);
        mCallout.setGeoElement(mSelectedArcGISFeature,null);
        mCallout.setContent(relativeLayout);
        */
        mCallout.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mLocationDisplay.startAsync();
        }else {
            Toast.makeText(getContext(),"refuse permission",Toast.LENGTH_LONG).show();
        }
    }

    private void searchForState(final String searchString){
        featureLayer2.clearSelection();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setWhereClause("name LIKE '%" + searchString + "%'" );

        final ListenableFuture<FeatureQueryResult> featurex = serviceFeatureTable2.queryFeaturesAsync(queryParameters);
        featurex.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = featurex.get();
                    if(result.iterator().hasNext()){
                        Feature feature1 = result.iterator().next();
                        Envelope envelope = feature1.getGeometry().getExtent();
                        mapView.setViewpointGeometryAsync(envelope, 200);
                        featureLayer2.selectFeature(feature1);
                    }else {
                        Toast.makeText(getActivity(),"该地点不存在",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Feature search failed for: " + searchString + ". Error=" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.resume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.dispose();
    }

    public static Fragment newInstance(String username1){
        Main _main = new Main();
        username = username1;
        return _main;
    }
}
