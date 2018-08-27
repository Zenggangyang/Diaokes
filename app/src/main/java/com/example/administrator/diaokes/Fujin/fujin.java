package com.example.administrator.diaokes.Fujin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.example.administrator.diaokes.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2018/7/26.
 */

public class fujin extends AppCompatActivity {
    private MapView mapView;
    private ServiceFeatureTable serviceFeatureTable;
    private FeatureLayer featureLayer;
    private GraphicsOverlay graphicsOverlay;
    private LocationDisplay mLocationDisplay;
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};
    private int requestCode = 2;
    private ImageView locate;
    private Toolbar toolbar;
    private double[] doubles = new double[2];
    private ArcGISFeature mSelectedArcGISFeature;
    private Callout mCallout;
    private ImageView home;
    private PictureMarkerSymbol shopSourceSymbol;
    private String name = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fujin_layout);
        mapView = findViewById(R.id.fmapView);
        home = findViewById(R.id.fujin_home);

        toolbar = findViewById(R.id.fujintoolbar);
        locate = findViewById(R.id.fujinlocate);
        setSupportActionBar(toolbar);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.floatcolor));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("附近");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ArcGISTiledLayer arcGISTiledLayer = new ArcGISTiledLayer(getResources().getString(R.string.tileMap38571));
        Basemap basemap = new Basemap(arcGISTiledLayer);
        ArcGISMap arcGISMap = new ArcGISMap(basemap);
        Viewpoint viewpoint = new Viewpoint(25.85097, 114.940278, 14);
        arcGISMap.setInitialViewpoint(viewpoint);
        mapView.setMap(arcGISMap);

        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        graphicsOverlay.setSelectionColor(0xFFFFF00);

        serviceFeatureTable = new ServiceFeatureTable(getResources().getString(R.string.users));
        featureLayer = new FeatureLayer(serviceFeatureTable);
        arcGISMap.getOperationalLayers().add(featureLayer);

        mCallout = mapView.getCallout();

        BitmapDrawable pinDrawable = (BitmapDrawable) ContextCompat.getDrawable(fujin.this, R.drawable.plocate);
        try{
            shopSourceSymbol = PictureMarkerSymbol.createAsync(pinDrawable).get();
        }catch (InterruptedException | ExecutionException e){
            Log.e("nav", "Picture Marker Symbol error: " + e.getMessage());
            Toast.makeText(fujin.this, "Failed to load pin drawable.", Toast.LENGTH_LONG).show();
        }
        shopSourceSymbol.setWidth(40);
        shopSourceSymbol.setHeight(40);
        SimpleRenderer simpleRenderer = new SimpleRenderer(shopSourceSymbol);
        featureLayer.setRenderer(simpleRenderer);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Viewpoint viewpoint1 = new Viewpoint(25.8509722,114.94027822,50000);
                mapView.setViewpointAsync(viewpoint1);
            }
        });
        /*
        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Point point = new Point((int) e.getX(), (int) e.getY());
                final ListenableFuture<IdentifyLayerResult> identifyFeature = mapView.identifyLayerAsync(featureLayer, point, 20, false, 1);
                identifyFeature.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            graphicsOverlay.getGraphics().clear();
                            featureLayer.clearSelection();
                            IdentifyLayerResult identifyLayerResult = identifyFeature.get();
                            List<GeoElement> retureGeoElements = identifyLayerResult.getElements();
                            if (retureGeoElements.size() > 0) {
                                if (retureGeoElements.get(0) instanceof ArcGISFeature) {
                                    ArcGISFeature mSelectedArcGISFeature = (ArcGISFeature) retureGeoElements.get(0);
                                    featureLayer.selectFeature(mSelectedArcGISFeature);
                                    Geometry point = mSelectedArcGISFeature.getGeometry();
                                    Polygon polygon = GeometryEngine.buffer(point, 20000);
                                    SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.FORWARD_DIAGONAL, 0xFF00FF00,
                                            new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF00FF00, 2));
                                    Graphic polygonGraphic = new Graphic(polygon, polygonSymbol);
                                    graphicsOverlay.getGraphics().add(polygonGraphic);
                                    queryFeature(polygon);
                                }
                            }
                        } catch (Exception e) {
                            Log.e("选择要素失败", e.getMessage());
                        }
                    }
                });
                return true;
            }
        });
*/
        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this,mapView){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                featureLayer.clearSelection();
                mSelectedArcGISFeature = null;
                mCallout.dismiss();
                Point point = new Point((int) e.getX(), (int) e.getY());
                final ListenableFuture<IdentifyLayerResult> identifyFeature = mapView.identifyLayerAsync(featureLayer, point, 20, false, 1);
                identifyFeature.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdentifyLayerResult identifyLayerResult = identifyFeature.get();
                            List<GeoElement> retureGeoElements = identifyLayerResult.getElements();
                            if (retureGeoElements.size() > 0) {
                                if (retureGeoElements.get(0) instanceof ArcGISFeature) {
                                    mSelectedArcGISFeature = (ArcGISFeature) retureGeoElements.get(0);
                                    featureLayer.selectFeature(mSelectedArcGISFeature);
                                    name = (String) mSelectedArcGISFeature.getAttributes().get("name");
                                    String content = (String)mSelectedArcGISFeature.getAttributes().get("personal");
                                    String rank = (String) mSelectedArcGISFeature.getAttributes().get("rank");
                                    showCallout(name,content,rank);
                                }
                            }else {
                                mCallout.dismiss();
                            }
                        }catch (Exception e) {
                            Log.e("选择要素失败", e.getMessage());
                        }
                    }
                });
                return true;
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
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(fujin.this, reqPermissions[0]) ==
                        PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(fujin.this, reqPermissions[1]) ==
                        PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1 && permissionCheck2)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(fujin.this, reqPermissions, requestCode);
                }else {
                    String message = String.format("error in program");
                    Toast.makeText(fujin.this,message,Toast.LENGTH_LONG).show();
                }
            }
        });
        mLocationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
            @Override
            public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
                if(mLocationDisplay.isStarted()) {
                    graphicsOverlay.getGraphics().clear();
                    doubles[0] = mLocationDisplay.getLocation().getPosition().getY();
                    doubles[1] = mLocationDisplay.getLocation().getPosition().getX();
                    com.esri.arcgisruntime.geometry.Point point = new com.esri.arcgisruntime.geometry.Point(doubles[1], doubles[0], SpatialReferences.getWgs84());//经度在前，即x,纬度在后,即y
                    Polygon polygon = GeometryEngine.buffer(point, 0.03);
                    SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x330000FF,
                            new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0x33000000, 2));
                    Graphic polygonGraphic = new Graphic(polygon, polygonSymbol);
                    graphicsOverlay.getGraphics().add(polygonGraphic);
                    queryFeature(polygon);
                }
            }
        });
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                if(!mLocationDisplay.isStarted()){
                    mLocationDisplay.startAsync();
                }
            }
        });
    }

    private void showCallout(String title,String content,String rank) {
        LayoutInflater layoutInflater = LayoutInflater.from(fujin.this);
        View view = layoutInflater.inflate(R.layout.talk_callout_layout, null);
        TextView textView1 = view.findViewById(R.id.talk_title);
        textView1.setText(title);
        TextView textView2 = view.findViewById(R.id.talk_content);
        textView2.setText(content);
        TextView textView = view.findViewById(R.id.talk_rank);
        textView.setText(rank);
        Button send = view.findViewById(R.id.talk_button);
        mCallout.setGeoElement(mSelectedArcGISFeature, null);
        mCallout.setContent(view);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fujin.this,talking.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        mCallout.show();
    }
    private void queryFeature(Geometry geometry) {
        try {
            QueryParameters args = new QueryParameters();
            args.setReturnGeometry(true);// 是否返回Geometry
            args.setGeometry(geometry); // 查询范围面
            args.setOutSpatialReference(SpatialReferences.getWgs84());
            args.setSpatialRelationship(QueryParameters.SpatialRelationship.DISJOINT);
            //获取查询结果result
            final ListenableFuture<FeatureQueryResult> result = featureLayer.getFeatureTable()
                    .queryFeaturesAsync(args);
            result.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        FeatureQueryResult features = result.get();
                        featureLayer.setFeaturesVisible(features,false);
                    }catch (Exception e) {
                        Log.e("选择要素失败", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mLocationDisplay.startAsync();
        }else {
            Toast.makeText(fujin.this,"refuse permission",Toast.LENGTH_LONG).show();
        }
    }
}
