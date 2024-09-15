package com.codebee.waterrider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebee.waterrider.dto.AuthDTO;
import com.codebee.waterrider.dto.OrderDto;
import com.codebee.waterrider.service.RequestService;
import com.codebee.waterrider.service.WaterAppService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackOrderFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap map;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private Marker markerCurrent;


    private List<MarkerOptions> clientLocations = new ArrayList<>();
    private Location currentlocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrackOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackOrderFragment newInstance(String param1, String param2) {
        TrackOrderFragment fragment = new TrackOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View inflate = inflater.inflate(R.layout.fragment_track_order, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        System.out.println(mapFragment);
        mapFragment.getMapAsync(this);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        return inflate;
    }


    @Override
    public void onViewCreated(@NonNull View inflate, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(inflate, savedInstanceState);
//        View view=getLayoutInflater().inflate(R.layout.fragment_track_order,inflate.findViewById(R.id.mapView), false);



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        SharedPreferences prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "no");
        String email = prefs.getString("email", "no");


        WaterAppService requestService = RequestService.getRequestService();
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(email);
        Call<List<OrderDto>> orders = requestService.getOrders(authDTO, token);
        orders.enqueue(new Callback<List<OrderDto>>() {
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                if (response.isSuccessful()) {

                    List<OrderDto> body = response.body();
                    int i = 0;

                    for (int j = 0; j < body.size(); j++) {


                        MarkerOptions options = new MarkerOptions()
                                .title("client :" + (j + 1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar_icon))
                                .position(new LatLng(Double.parseDouble(body.get(j).getLatitude()), Double.parseDouble(body.get(j).getLongitude())));

                        clientLocations.add(options);

                    }

                }

            }

            @Override
            public void onFailure(Call<List<OrderDto>> call, Throwable t) {

            }
        });


        inflate.findViewById(R.id.getClientLocationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                clientLocations.forEach(marker -> {

                    map.addMarker(marker);
                });
                setClientLocations();
            }
        });

        inflate.findViewById(R.id.trackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccess();
            }
        });

    }

    public void setClientLocations() {

    }

    public void getAccess() {
        if (checkPermission()) {
//            map.setMyLocationEnabled(true);
            getLastLocation();

        } else {
//            map.setMyLocationEnabled(true);

            requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        }


    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        LatLng latLng = new LatLng(8.0163701, 79.8417505);
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(8);

        map.moveCamera(center);
//        map.animateCamera(zoom);
        getAccess();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();

            } else {

                Snackbar.make(getActivity().findViewById(R.id.productFragmentContainer), "Turn on Location", Snackbar.LENGTH_INDEFINITE).setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }
                }).show();
            }
        }

    }


    private boolean checkPermission() {

        boolean permission = false;
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            permission = true;

        }
        return permission;
    }

    private void getLastLocation() {
        if (checkPermission()) {

//            Task<Location> task = fusedLocationProviderClient.getLastLocation();
//            task.addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        currentlocation = location;
//                        LatLng latLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,70));
//                        map.addMarker(new MarkerOptions().position(latLng).title("my location"));
//                    }
//                }
//            });


            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                    .setWaitForAccurateLocation(true)
                    .setMinUpdateIntervalMillis(500)
                    .setMaxUpdateDelayMillis(1000)
                    .build();

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);


                    currentlocation = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));
//                    map.addMarker(new MarkerOptions().position(latLng).title("my location"));
                    CameraUpdate center =
                            CameraUpdateFactory.newLatLng(latLng);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                    map.moveCamera(center);
                    map.animateCamera(zoom);
                    if (markerCurrent == null) {
                        MarkerOptions options = new MarkerOptions()
                                .title("my location")
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                                .position(latLng);
                        markerCurrent = map.addMarker(options);


                    } else {
                        markerCurrent.setPosition(latLng);
                    }
                    System.out.println(latLng);
//                    moveCamera(latLng);
                }


            }, Looper.getMainLooper());

        }
    }
}