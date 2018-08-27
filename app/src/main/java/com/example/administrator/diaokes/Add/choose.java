package com.example.administrator.diaokes.Add;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.esri.arcgisruntime.geometry.CoordinateFormatter;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.example.administrator.diaokes.R;

/**
 * Created by Administrator on 2018/8/2.
 */

public class choose extends AppCompatActivity {
    private MapView mapView;
    private Toolbar toolbar;
    private android.graphics.Point mClickPoint;
    private GraphicsOverlay graphicsOverlay;
    private TextView submit;
    private String strin = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_layout);

        toolbar = findViewById(R.id.choose_toolbar);
        setSupportActionBar(toolbar);
        mapView = findViewById(R.id.choose_mapView);
        submit = findViewById(R.id.choose_submit);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.haokan));
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("选择地点");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArcGISTiledLayer arcGISTiledLayer = new ArcGISTiledLayer(getResources().getString(R.string.tileMap38571));
        Basemap basemap = new Basemap(arcGISTiledLayer);
        ArcGISMap arcGISMap = new ArcGISMap(basemap);
        Viewpoint viewpoint = new Viewpoint(25.85097,114.940278,14);
        arcGISMap.setInitialViewpoint(viewpoint);
        mapView.setMap(arcGISMap);

        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(choose.this,mapView){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                graphicsOverlay.getGraphics().clear();
                mClickPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());
                Point point = mapView.screenToLocation(mClickPoint);
                strin = CoordinateFormatter.toLatitudeLongitude(point,CoordinateFormatter.LatitudeLongitudeFormat.DECIMAL_DEGREES,4);

                Point point1 = new Point(Float.parseFloat((strin.split(" ")[1].split("E")[0])),Float.parseFloat((strin.split("N")[0])) , SpatialReferences.getWgs84());
                Graphic graphic = new Graphic(point1,new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12));
                graphicsOverlay.getGraphics().add(graphic);
                return super.onSingleTapConfirmed(e);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("x",Float.parseFloat((strin.split(" ")[1].split("E")[0]))+"");
                intent.putExtra("y",Float.parseFloat((strin.split("N")[0]))+"");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
}
