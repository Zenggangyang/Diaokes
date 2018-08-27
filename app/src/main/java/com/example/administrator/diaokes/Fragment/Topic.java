package com.example.administrator.diaokes.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.administrator.diaokes.R;

/**
 * Created by Administrator on 2018/6/27.
 */

public class Topic extends Fragment {
    private Toolbar toolbar;
    private MapView mapView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topic_layout,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        mapView = (MapView) view.findViewById(R.id.mapView);
        ArcGISTiledLayer arcGISTiledLayer = new ArcGISTiledLayer(getResources().getString(R.string.tileMap38571));
        Basemap basemap = new Basemap(arcGISTiledLayer);
        ArcGISMap arcGISMap = new ArcGISMap(basemap);
        ArcGISTiledLayer arcGISTiledLayer1 = new ArcGISTiledLayer(getResources().getString(R.string.tileMap3857));
        arcGISMap.getOperationalLayers().add(arcGISTiledLayer1);

        Viewpoint viewpoint = new Viewpoint(25.85097,114.940278,14);
        arcGISMap.setInitialViewpoint(viewpoint);
        mapView.setMap(arcGISMap);
        return view;
    }

    public static Fragment newInstance(){
        Topic _topic = new Topic();
        return _topic;
    }
}
