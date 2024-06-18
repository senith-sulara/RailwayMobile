package com.example.railridemate;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.railridemate.Adapters.InfoWindowAdapter;
import com.example.railridemate.Utils.DirectionsJSONParser;
import com.example.railridemate.Utils.HttpUtils;
import com.example.railridemate.Utils.NotificationHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private GoogleMap mGoogleMap;
    private Spinner trainNameDropdown;
    private NotificationHelper notificationHelper;
    private Handler mHandler;
    private static final long LOCATION_UPDATE_INTERVAL = 10000; // 10 seconds


//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        }
//    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        notificationHelper = new NotificationHelper(requireContext());
// Initialize Handler
        mHandler = new Handler(Looper.getMainLooper());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Handle location updates here
            }
        };

        // Initialize the dropdown menu
        trainNameDropdown = view.findViewById(R.id.train_name_dropdown);

        // Set up the listener for dropdown selection
        trainNameDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTrainName = (String) parent.getItemAtPosition(position);
                // Retrieve location data for the selected train name and display it on the map
                getLocationForTrain(selectedTrainName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Inflate the layout for this fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mGoogleMap = googleMap;
                // Request continuous location updates
                requestLocationUpdates();
            }
        });

        return view;
    }

    // Method to fetch data for dropdown menu from Firebase Realtime Database
    private void fetchDataForDropdown() {
        DatabaseReference trainsRef = FirebaseDatabase.getInstance().getReference("users");
        trainsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> trainNames = new ArrayList<>();
                for (DataSnapshot trainSnapshot : dataSnapshot.getChildren()) {
                    String trainName = trainSnapshot.child("trainName").getValue(String.class);
                    if (trainName != null) {
                        trainNames.add(trainName);
                    }
                }
                // Populate the dropdown menu with the fetched train names
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, trainNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                trainNameDropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    // Request continuous location updates
    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // 10 seconds

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    // Method to retrieve location data for the selected train name and display it on the map
//    private void getLocationForTrain(String trainName) {
//        if (trainName != null && !trainName.isEmpty()) {
//            DatabaseReference trainsRef = FirebaseDatabase.getInstance().getReference("users");
//            trainsRef.orderByChild("trainName").equalTo(trainName).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot trainSnapshot : dataSnapshot.getChildren()) {
//                        String userId = trainSnapshot.getKey(); // Assuming each train has a unique user id associated with it
//                        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("location");
//                        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
//                                Double longitude = dataSnapshot.child("longitude").getValue(Double.class);
//                                if (latitude != null && longitude != null) {
//                                    LatLng trainLocation = new LatLng(latitude, longitude);
//                                    // Clear existing markers
//                                    mGoogleMap.clear();
//                                    // Add marker to the map
//                                    mGoogleMap.addMarker(new MarkerOptions().position(trainLocation).title(trainName));
//                                    // Move camera to train location
//                                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(trainLocation, 15));
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                // Handle errors
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle errors
//                }
//            });
//        } else {
//            // Check if location permission is granted
//            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                // Permission is granted, fetch current location
//                fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                            // Clear existing markers
//                            mGoogleMap.clear();
//                            // Add marker to the map
//                            mGoogleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
//                            // Move camera to current location
//                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
//                        } else {
//                            Toast.makeText(requireContext(), "Unable to retrieve current location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            } else {
//                // Permission is not granted, request the permission from the user
//                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
//            }
//        }
//    }

    private void getLocationForTrain(String trainName) {
        if (trainName != null && !trainName.isEmpty()) {
            DatabaseReference trainsRef = FirebaseDatabase.getInstance().getReference("users");
            trainsRef.orderByChild("trainName").equalTo(trainName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot trainSnapshot : dataSnapshot.getChildren()) {
                        String userId = trainSnapshot.getKey(); // Assuming each train has a unique user id associated with it
                        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("location");
                        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                                Double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                                if (latitude != null && longitude != null) {
                                    LatLng trainLocation = new LatLng(latitude, longitude);
                                    // Fetch directions from current location to train's location
                                    fetchDirections(trainLocation);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle errors
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }

    private void fetchDirections(LatLng destination) {
        if (mGoogleMap != null) {
            // Your current location
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, fetch current location
                fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
                            // Fetch directions using Directions API
                            fetchDirections(origin, destination);
                        } else {
                            Toast.makeText(requireContext(), "Unable to retrieve current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // Permission is not granted, request the permission from the user
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void fetchDirections(LatLng origin, LatLng destination) {
        // Fetch directions from current location to destination using Directions API
        String url = getDirectionsUrl(origin, destination);
        FetchDirectionsTask fetchDirectionsTask = new FetchDirectionsTask();
        fetchDirectionsTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin, LatLng destination) {
        // Origin of route
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String strDest = "destination=" + destination.latitude + "," + destination.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = strOrigin + "&" + strDest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getResources().getString(R.string.google_maps_key);
    }

    private class FetchDirectionsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = HttpUtils.downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            DirectionsParserTask directionsParserTask = new DirectionsParserTask();
            directionsParserTask.execute(result);
        }
    }

    private class DirectionsParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            PolylineOptions lineOptions = null;
            String duration = "";
            String distance = "";
            LatLng origin = null;
            LatLng destination = null;
            ArrayList<LatLng> points = new ArrayList<>(); // Initialize points list

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if (j == 0) { // Get duration and distance from the first point
                        duration = point.get("duration");
                        distance = point.get("distance");
                    } else { // Get latitude and longitude for the polyline
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null && mGoogleMap != null && !points.isEmpty()) {
                // Clear previous markers and polylines
                mGoogleMap.clear();
                // Add new polyline
                mGoogleMap.addPolyline(lineOptions);

                // Set origin and destination markers
                origin = points.get(0);
                destination = points.get(points.size() - 1);

                // Add markers for origin and destination
                mGoogleMap.addMarker(new MarkerOptions().position(origin)
                        .title("Destination")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mGoogleMap.addMarker(new MarkerOptions().position(destination)
                        .title("origin")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                // Move camera to fit both markers
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(destination);
                LatLngBounds bounds = builder.build();
                int padding = 100; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mGoogleMap.animateCamera(cu);

                // Show distance and duration in a custom info window
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View infoWindow = inflater.inflate(R.layout.custom_info_window, null);

                TextView textDistance = infoWindow.findViewById(R.id.text_distance);
                TextView textDuration = infoWindow.findViewById(R.id.text_duration);

                textDistance.setText("Distance: " + distance);
                textDuration.setText("Duration: " + duration);

                GoogleMap.InfoWindowAdapter infoWindowAdapter = new InfoWindowAdapter(infoWindow);
                mGoogleMap.setInfoWindowAdapter(infoWindowAdapter);

                // After adding the custom info window adapter
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(destination)  // Set marker position closer to the destination
                        .title("Route Info")
                        .snippet("Distance: " + distance + "\nDuration: " + duration));
                marker.showInfoWindow(); // Show the info window for this marker

                // Save the duration to Firebase
                String selectedTrainName = trainNameDropdown.getSelectedItem().toString();
                saveDurationToFirebase(selectedTrainName, duration);
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    int tt = 0;
                    String[] parts = duration.split(" ");
                    if (parts.length == 4) { // Assuming format is "X hours Y mins"
                        int hours = Integer.parseInt(parts[0]);
                        int mins = Integer.parseInt(parts[2]);
                        tt = hours * 60 + mins; // Convert hours to minutes and add with mins
                    } else if (parts.length == 2 && parts[1].equals("mins")) { // Assuming format is "X mins"
                        tt = Integer.parseInt(parts[0]);
                    }

                    if (tt < 15) {
                        notificationHelper.sendNotification("Train Alert", "Train is near, get ready!");
                    }
                } else {
                    // Permission is not granted, request the permission from the user
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY, Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
                }
            }
        }


    }
    private void saveDurationToFirebase(String trainName, String duration) {
        DatabaseReference trainsRef = FirebaseDatabase.getInstance().getReference("users");
        trainsRef.orderByChild("trainName").equalTo(trainName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot trainSnapshot : dataSnapshot.getChildren()) {
                    String userId = trainSnapshot.getKey(); // Assuming each train has a unique user id associated with it
                    DatabaseReference durationRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("duration");
                    durationRef.setValue(duration);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


    private void startLocationUpdates() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocationForTrain(trainNameDropdown.getSelectedItem().toString());
                // Schedule the next execution
                mHandler.postDelayed(this, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start periodic location updates when the fragment is resumed
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop periodic location updates when the fragment is paused
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Fetch data for the dropdown menu when the fragment starts
        fetchDataForDropdown();
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        // Stop location updates when the fragment is paused
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//    }
}