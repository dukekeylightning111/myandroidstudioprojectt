package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private Button confirmButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    LatLng selectedLocation = googleMap.getCameraPosition().target;
                    googleMap.addMarker(new MarkerOptions().position(selectedLocation));

                    Bundle result = new Bundle();
                    result.putParcelable("selected_location", selectedLocation);

                    getParentFragmentManager().setFragmentResult("map_result", result);
                    requireActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng israelLocation = new LatLng(31.0461, 34.8516); // Israel coordinates
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israelLocation, 7));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear(); // Clear existing markers
                googleMap.addMarker(new MarkerOptions().position(latLng)); // Add a marker at the clicked location
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if (googleMap != null) {
            LatLng location = new LatLng(0, 0);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 100));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void setFragmentResultListener(String resultKey, DesignSportActivity designSportActivity, FragmentResultListener fragmentResultListener) {
        getParentFragmentManager().setFragmentResultListener(resultKey, this, fragmentResultListener);
    }
}