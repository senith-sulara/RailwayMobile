package com.example.railridemate.Adapters;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private View mInfoWindow;

    public InfoWindowAdapter(View infoWindow) {
        mInfoWindow = infoWindow;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return mInfoWindow;
    }
}
