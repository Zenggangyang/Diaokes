package com.example.administrator.diaokes.Navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.util.ListenableList;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.FoundDate;
import com.example.administrator.diaokes.db.Routes;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2018/8/7.
 */

public class diaryroutes extends AppCompatActivity{
    private MapView mapView;
    private Toolbar toolbar;
    private GraphicsOverlay graphicsOverlay;
    private GraphicsOverlay newGraphicsOverlay;
    private Polyline polyline;
    private PointCollection[] pointCollection;
    private android.graphics.Point mClickPoint;
    private Graphic mGraphic;
    private Graphic mGraphic1;
    private Callout mCallout;
    private List<Routes> list;
    private Button qitian;
    private Button all;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_route_layout);
        mapView = findViewById(R.id.diary_mapView);
        toolbar = findViewById(R.id.diary_toolbar);
        qitian = findViewById(R.id.route_qitian);
        all = findViewById(R.id.route_all);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Window window = diaryroutes.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(diaryroutes.this.getResources().getColor(R.color.floatcolor));

        toolbar.setTitle("我的路线");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArcGISTiledLayer arcGISTiledLayer = new ArcGISTiledLayer(getResources().getString(R.string.tileMap3857));
        Basemap basemap = new Basemap(arcGISTiledLayer);
        final ArcGISMap arcGISMap = new ArcGISMap(basemap);
        final Viewpoint viewpoint = new Viewpoint(25.8509722,114.94027822,50000);
        arcGISMap.setInitialViewpoint(viewpoint);
        mapView.setMap(arcGISMap);
        mCallout = mapView.getCallout();

        graphicsOverlay = new GraphicsOverlay();
        newGraphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(newGraphicsOverlay);
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        list = DataSupport.findAll(Routes.class);
        graphicsOverlay.setVisible(false);

        pointCollection = new PointCollection[list.size()];
        for(int k = 0;k<list.size();k++){
            pointCollection[k] = new PointCollection(SpatialReferences.getWgs84());
        }
        inits();

        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(diaryroutes.this,mapView){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                mClickPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());

                mCallout.dismiss();
                graphicsOverlay.clearSelection();
                newGraphicsOverlay.clearSelection();
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
                                String address = (String)graphic.get(0).getAttributes().get("time");
                                String starttime = (String) graphic.get(0).getAttributes().get("start");
                                String content =(String) graphic.get(0).getAttributes().get("content");
                                showCallout(name,address,starttime,content);
                            }
                        } catch (InterruptedException | ExecutionException ie) {
                            ie.printStackTrace();
                        }
                    }
                });
                final ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphic1 = mapView.identifyGraphicsOverlayAsync(newGraphicsOverlay, mClickPoint, 10.0, false, 2);
                identifyGraphic.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdentifyGraphicsOverlayResult grOverlayResult = identifyGraphic1.get();
                            // get the list of graphics returned by identify graphic overlay
                            List<Graphic> graphic = grOverlayResult.getGraphics();
                            // get size of list in results
                            int identifyResultSize = graphic.size();
                            if (!graphic.isEmpty()) {
                                // show a toast message if graphic was returned
                                graphic.get(0).setSelected(true);
                                mGraphic1 = graphic.get(0);
                                String name = (String) graphic.get(0).getAttributes().get("name");
                                String address = (String)graphic.get(0).getAttributes().get("time");
                                String starttime = (String) graphic.get(0).getAttributes().get("start");
                                String content =(String) graphic.get(0).getAttributes().get("content");
                                showCallout1(name,address,starttime,content);
                            }
                        } catch (InterruptedException | ExecutionException ie) {
                            ie.printStackTrace();
                        }
                    }
                });
                return super.onSingleTapConfirmed(e);
            }
        });

        qitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                graphicsOverlay.setVisible(false);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String data1 = df.format(new Date()).toString();
                for(int j = 0;j<newGraphicsOverlay.getGraphics().size();j++) {
                    newGraphicsOverlay.getGraphics().remove(newGraphicsOverlay.getGraphics().remove(j));
                }
                ListenableList<Graphic>  graphics = graphicsOverlay.getGraphics();
                for(int i = 0;i<graphics.size();i++){
                    if(Integer.parseInt(data1.split("-")[2])-7>0) {
                        if (Integer.parseInt(graphics.get(i).getAttributes().get("time").toString().split(" ")[0].split("-")[2]) > Integer.parseInt(data1.split("-")[2]) - 7) {
                            Geometry graphic = graphics.get(i).getGeometry();
                            Graphic graphic1 = new Graphic(graphic, graphics.get(i).getAttributes(), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 5));
                            newGraphicsOverlay.getGraphics().add(graphic1);
                        }
                    }else {
                        if((Integer.parseInt(data1.split("-")[2])+(30-Integer.parseInt(graphics.get(i).getAttributes().get("time").toString().split(" ")[0].split("-")[2])))<7){
                            Geometry graphic = graphics.get(i).getGeometry();
                            Graphic graphic1 = new Graphic(graphic, graphics.get(i).getAttributes(), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 5));
                            newGraphicsOverlay.getGraphics().add(graphic1);
                        }
                    }
                }
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGraphicsOverlay.setVisible(false);
                graphicsOverlay.setVisible(true);
            }
        });
    }

    private void inits(){
        try {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getLocate().split(" ").length; j++) {
                    Point point = new Point(Double.parseDouble(list.get(i).getLocate().split(" ")[j].split(",")[0]), Double.parseDouble(list.get(i).getLocate().split(" ")[j].split(",")[1]));
                    pointCollection[i].add(point);
                }
                polyline = new Polyline(pointCollection[i]);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", list.get(0).getName());
                map.put("time", list.get(0).getTime());
                map.put("content",list.get(0).getContent());
                map.put("start",list.get(0).getStart());
                Graphic graphic = new Graphic(polyline, map, new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 5));
                graphicsOverlay.getGraphics().add(graphic);
            }
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
    }

    private void showCallout(String name,String time,String starttime1,String content1){
        LayoutInflater layoutInflater = LayoutInflater.from(diaryroutes.this);
        View view = layoutInflater.inflate(R.layout.callout_route_layout,null);
        TextView name1 = view.findViewById(R.id.route_title);
        TextView time1 = view.findViewById(R.id.route_time);
        TextView starttime = view.findViewById(R.id.route_time1);
        TextView content = view.findViewById(R.id.route_content);
        name1.setText("路线名称："+name);
        time1.setText("结束："+time);
        content.setText(content1);
        starttime.setText("开始："+starttime1);
        mCallout.setGeoElement(mGraphic,null);
        mCallout.setContent(view);
        mCallout.show();
    }
    private void showCallout1(String name,String time,String starttime1,String content1){
        LayoutInflater layoutInflater = LayoutInflater.from(diaryroutes.this);
        View view = layoutInflater.inflate(R.layout.callout_route_layout1,null);
        TextView name1 = view.findViewById(R.id.route_title_1);
        TextView time1 = view.findViewById(R.id.route_time_1);
        TextView starttime = view.findViewById(R.id.route_time1_1);
        TextView content = view.findViewById(R.id.route_content_1);
        name1.setText("路线名称："+name);
        time1.setText("结束："+time);
        content.setText(content1);
        starttime.setText("开始："+starttime1);
        mCallout.setGeoElement(mGraphic1,null);
        mCallout.setContent(view);
        mCallout.show();
    }
}
