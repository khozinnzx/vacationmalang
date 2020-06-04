package com.example.vacationmalangver1.Ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vacationmalangver1.Adapter.MapRvAdapter;
import com.example.vacationmalangver1.Model.ClusterMarker;
import com.example.vacationmalangver1.Model.LokasiUser;
import com.example.vacationmalangver1.Model.LokasiWisata;
import com.example.vacationmalangver1.Model.PolylineData;
import com.example.vacationmalangver1.Model.UrutanWisata;
import com.example.vacationmalangver1.Model.User;
import com.example.vacationmalangver1.R;
import com.example.vacationmalangver1.util.MyClusterManagerRenderer;
import com.example.vacationmalangver1.util.ViewWeightAnimationWrapper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.vacationmalangver1.Constants.MAPVIEW_BUNDLE_KEY;


public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnPolylineClickListener,
        MapRvAdapter.MapRvClickListener {

    private static final String TAG = "MapFragment";
    //    private static final LatLng MuseumAngkut = new LatLng(-7.885046,112.477094);
//    private static final LatLng OmahKayu = new LatLng(-7.88499,112.477475);
//    private static final LatLng CobanRondo = new LatLng(-7.885161,112.476976);
//    private static final LatLng PantaiTigaWarna = new LatLng(-8.443664,112.677304);
    private static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    private static final int MAP_LAYOUT_STATE_EXPANDED = 1;


    private Marker mMuseumAngkut, mOmahKayu, mCobanRondo, mTigaWarna;

    private RecyclerView urutanWisataRecyclerView;
    private MapView mMapView;
    private RelativeLayout mMapContainer;

    private FirebaseFirestore mDb;
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    public LokasiUser mLokasiUser;
    private LokasiWisata mLokasiWisata;
    private ArrayList<LokasiUser> mLokasiUserList = new ArrayList<>();
    private ArrayList<User> mUserList = new ArrayList<>();
    private ArrayList<LokasiWisata> mLokasiWisataList = new ArrayList<>();
    private ClusterManager mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarker = new ArrayList<>();
    private int mMapLayoutState = 0;
    private GeoApiContext mGeoApiContext = null;
    private ArrayList<PolylineData> mPolylineData = new ArrayList<>();
    private Marker mSelectedMarker = null;
    private ArrayList<Marker> mTripMarkers = new ArrayList<>();

    ArrayList<String> urutanWisataList = new ArrayList<>();
    ArrayList<UrutanWisata> urutanWisataPojo = new ArrayList<>();
    MapRvAdapter mMapRvAdapter;
    int index = 0;

    public MapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle bundle = this.getArguments();
////        if (bundle != null) {
////            //int myInt = bundle.getInt(key, defaultValue);
////            ArrayList<UrutanWisata> urutanWisata = bundle.getParcelableArrayList("intent_list");
////            urutanWisataList.addAll(urutanWisata);
////            Log.d(TAG, "onCreate: "+urutanWisataList.toString());
////
////        }

        // ArrayList<UrutanWisata> urutanWisata = getArguments().getParcelableArrayList("intent_list");
        //urutanWisataList.addAll(urutanWisata);


        mDb = FirebaseFirestore.getInstance();

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        urutanWisataRecyclerView = view.findViewById(R.id.urutan_wisata_recycler_view);
        mMapView = view.findViewById(R.id.map);
        mMapContainer = view.findViewById(R.id.map_container);

        view.findViewById(R.id.btn_full_screen_map).setOnClickListener(this);
        view.findViewById(R.id.btn_reset_map).setOnClickListener(this);

        try{
            urutanWisataList = getArguments().getStringArrayList("intent_list");
            if (urutanWisataList != null){
                Log.d(TAG, "onCreateView: suceess");
                Log.d(TAG, "isi intent list : "+ urutanWisataList.toString());
                initRecyclerView();
                initGoogleMap(savedInstanceState);

                return view;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        initRecyclerView();

        initGoogleMap(savedInstanceState);

//        for (LokasiUser lokasiUser : mLokasiUser){
//            Log.d(TAG,"oncreateview lokasi user: "+lokasiUser.getUser().getUsername());
//            Log.d(TAG,"oncreateview geopoint : "+ lokasiUser.getGeo_point().getLatitude()+","+ lokasiUser.getGeo_point().getLongitude());
//
//        }

//        getUserLocation();

        return view;
    }

    public void initRecyclerView() {

        Log.d(TAG, "initRecyclerView: " + urutanWisataList.toString());
        urutanWisataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        for (int i = 0; i<urutanWisataList.size(); i++){
            urutanWisataPojo.add(new UrutanWisata(urutanWisataList.get(i).toString()));
        }

        Log.d(TAG, "initRecyclerViewPojo: "+ urutanWisataPojo.toString());

        mMapRvAdapter = new MapRvAdapter(urutanWisataPojo, this);
        urutanWisataRecyclerView.setAdapter(mMapRvAdapter);



    }


//    private void getLokasiWisata(){
//        mDb.collection("lokasi wisata")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()){
//
//                        mLokasiWisata = new LokasiWisata(document.getGeoPoint("geo_point_wisata"),
//                                document.getString("nama_wisata"));
//                        mLokasiWisataList.add(mLokasiWisata);
//
//                    }
//                }else{
//                    Log.d(TAG, "error"+ task.getException());
//                }
//            }
//        });
//
//
//    }
////    private void addMarkerWisata(){
////        mMuseumAngkut = mGoogleMap.addMarker(new MarkerOptions()
////                .position(MuseumAngkut)
////                .title("Museum Angkut"));
////        mMuseumAngkut.setTag(0);
////        mOmahKayu = mGoogleMap.addMarker(new MarkerOptions()
////                .position(OmahKayu)
////                .title("Omah Kayu"));
////        mOmahKayu.setTag(0);
////        mCobanRondo = mGoogleMap.addMarker(new MarkerOptions()
////                .position(CobanRondo)
////                .title("Coban Rondo"));
////        mCobanRondo.setTag(0);
////        mTigaWarna = mGoogleMap.addMarker(new MarkerOptions()
////                .position(PantaiTigaWarna)
////                .title("Pantai Tiga Warna"));
//    }



    public void zoomRoute(List<LatLng> lstLatLngRoute) {

        if (mGoogleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }

    private void removeTripMarkers() {
        for (Marker marker : mTripMarkers) {
            marker.remove();
        }
    }

    private void resetSelectedMarker() {
        if (mSelectedMarker != null) {
            mSelectedMarker.setVisible(true);
            mSelectedMarker = null;
            removeTripMarkers();
        }
    }

    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);
                if (mPolylineData.size() > 0) {
                    for (PolylineData polylineData : mPolylineData) {
                        polylineData.getPolyline().remove();
                    }
                    mPolylineData.clear();
                    mPolylineData = new ArrayList<>();
                }
                double duration = 9999999;
                for (DirectionsRoute route : result.routes) {
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.darkGrey));
                    polyline.setClickable(true);
                    mPolylineData.add(new PolylineData(polyline, route.legs[0]));

                    double tempduration = route.legs[0].duration.inSeconds;
                    if (tempduration < duration) {
                        duration = tempduration;
                        onPolylineClick(polyline);
                        zoomRoute(polyline.getPoints());
                    }
                    mSelectedMarker.setVisible(false);

                }
            }
        });
    }

    private void calculateDirections(Marker marker) {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        mLokasiUser.getGeo_point().getLatitude(),
                        mLokasiUser.getGeo_point().getLongitude()
                )
        );
        Log.d(TAG, "calculateDirections: user current :" + mLokasiUser.getGeo_point().toString());
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());

            }
        });
    }

    private void resetMap() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();

            if (mClusterManager != null) {
                mClusterManager.clearItems();
            }

            if (mClusterMarker.size() > 0) {
                mClusterMarker.clear();
                mClusterMarker = new ArrayList<>();
            }

            if (mPolylineData.size() > 0) {
                mPolylineData.clear();
                mPolylineData = new ArrayList<>();
            }
        }
    }

    private void addMapMarker() {
        if (mGoogleMap != null) {
            resetMap();
            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), mGoogleMap);
            }
            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        getActivity(),
                        mGoogleMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);

            }
            mDb.collection("lokasi wisata")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {

//                            mLokasiWisata = new LokasiWisata(document.getGeoPoint("geo_point_wisata"),
//                                    document.getString("nama_wisata"),document.get("id_wisata"));
                            mLokasiWisata = document.toObject(LokasiWisata.class);
                            mLokasiWisataList.add(mLokasiWisata);
                            for (LokasiWisata lokasiWisata : mLokasiWisataList) {
                                Log.d(TAG, "wisata list: " + lokasiWisata.getGeo_point_wisata().toString() + lokasiWisata.getNama_wisata());
                                try {
                                    String snippet = "";
                                    int gambar = 0;
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Banyu Anjlok")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.banyuanjlok;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Hawai Waterpark")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.hawaii;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Batu Night Spectacular")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.bns;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Museum Angkut")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.museumangkut;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Omah Kayu")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.omahkayu;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Air Terjun Coban Rondo")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.cobanrondo;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Pantai Tiga Warna")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.pantaitigawarna;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Pantai Goa Cina")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.goacina;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Pantai Balekambang")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.balekambang;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Sumber Maron")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.sumbermaron;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Wisata Paralayang")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.paralayang;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Predator Fun Park")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.predatorfunpark;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Taman Selecta")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.tamanselecta;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Jawa Timur Park 2")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.jatimpark2;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Jodipan")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.jodipan;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Jatim Park 1")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.jatimpark1;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Pantai Ngliyep")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.pantaingliyep;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Batu Wonderland")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.batuwonderland;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Museum Brawijaya")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.museumbrawijaya;
                                    }
                                    if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Coban Talun")) {
                                        snippet = "cari rute kesana?";
                                        gambar = R.drawable.cobantalun;
                                    }
                                    ClusterMarker newClusterMarker = new ClusterMarker(
                                            new LatLng(lokasiWisata.getGeo_point_wisata().getLatitude(), lokasiWisata.getGeo_point_wisata().getLongitude()),
                                            lokasiWisata.getNama_wisata(),
                                            snippet,
                                            gambar

                                    );
                                    mClusterManager.addItem(newClusterMarker);
                                    mClusterMarker.add(newClusterMarker);


                                } catch (NullPointerException e) {
                                    Log.e(TAG, "addMapMarker: null pointerexception" + e.getMessage());
                                }
                            }
                            mClusterManager.cluster();
                            getUserLocation();
                        }
                    } else {
                        Log.d(TAG, "error" + task.getException());
                    }
                }
            });
//            for (LokasiWisata lokasiWisata : mLokasiWisataList) {
//                Log.d(TAG, "addMapMarker: location :" + lokasiWisata.getGeo_point_wisata().toString());
//            try {
//                String snippet = "";
//                int gambar = 0;
//                if (lokasiWisata.getNama_wisata().equalsIgnoreCase("Museum Angkut")) {
//                    snippet = "Museum Angkut";
//                    gambar = R.drawable.museumangkut;
//                }
//                if (lokasiWisata.getNama_wisata() == " Omah Kayu") {
//                    snippet = "Omah Kayu";
//                    gambar = R.drawable.omahkayu;
//                }
//                ClusterMarker newClusterMarker = new ClusterMarker(
//                        new LatLng(lokasiWisata.getGeo_point_wisata().getLatitude(), lokasiWisata.getGeo_point_wisata().getLongitude()),
//                        lokasiWisata.getNama_wisata(),
//                        snippet,
//                        gambar
//
//                );
//                mClusterManager.addItem(newClusterMarker);
//                mClusterMarker.add(newClusterMarker);
//
//
//            } catch (NullPointerException e) {
//                Log.e(TAG, "addMapMarker: null pointerexception" + e.getMessage());
//            }
//
//
//        }
//            mClusterManager.cluster();
//            getUserLocation();
        }
    }


    private void getUserLocation() {
        mLokasiUser = new LokasiUser();
        DocumentReference userLocRef = mDb.collection("lokasi user")
                .document(FirebaseAuth.getInstance().getUid());
        userLocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: sukses dapatkan user lok");
                    mLokasiUser = task.getResult().toObject(LokasiUser.class);
                    Log.d(TAG, "onComplete: get lokasi dengan nama : " + mLokasiUser.getUser() + " : " + mLokasiUser.getGeo_point().getLatitude() + "," + mLokasiUser.getGeo_point().getLongitude());

                    double bottomBoundary = mLokasiUser.getGeo_point().getLatitude() - .1;
                    double leftBoundary = mLokasiUser.getGeo_point().getLongitude() - .1;
                    double topBoundary = mLokasiUser.getGeo_point().getLatitude() + .1;
                    double rightBoundary = mLokasiUser.getGeo_point().getLongitude() + .1;

                    mMapBoundary = new LatLngBounds(new LatLng(bottomBoundary, leftBoundary),
                            new LatLng(topBoundary, rightBoundary)

                    );

                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));

                }
            }
        });
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_map_api_key))
                    .build();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        //mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        mGoogleMap = map;
        mGoogleMap.setOnPolylineClickListener(this);
        addMapMarker();
        mGoogleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private void expandMapAnimation() {
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                50,
                100);
        mapAnimation.setDuration(800);

        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(urutanWisataRecyclerView);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
                "weight",
                50,
                0);
        recyclerAnimation.setDuration(800);

        recyclerAnimation.start();
        mapAnimation.start();
    }

    private void contractMapAnimation() {
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                100,
                50);
        mapAnimation.setDuration(800);

        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(urutanWisataRecyclerView);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
                "weight",
                0,
                50);
        recyclerAnimation.setDuration(800);

        recyclerAnimation.start();
        mapAnimation.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_full_screen_map: {

                if (mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED) {
                    mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
                    expandMapAnimation();
                } else if (mMapLayoutState == MAP_LAYOUT_STATE_EXPANDED) {
                    mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
                    contractMapAnimation();
                }
                break;
            }
            case R.id.btn_reset_map: {
                addMapMarker();
                break;
            }

        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        if(marker.getTitle().contains("rute: " + index)){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Open Google Maps?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            String latitude = String.valueOf(marker.getPosition().latitude);
                            String longitude = String.valueOf(marker.getPosition().longitude);
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");

                            try{
                                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivity(mapIntent);
                                }
                            }catch (NullPointerException e){
                                Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage() );
                                Toast.makeText(getActivity(), "Couldn't open map", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(marker.getSnippet())
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        resetSelectedMarker();
                        mSelectedMarker = marker;
                        calculateDirections(marker);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

        for (PolylineData polylineData : mPolylineData) {
            index++;
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());
            if (polyline.getId().equals(polylineData.getPolyline().getId())) {
                polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.blue1));
                polylineData.getPolyline().setZIndex(1);

                LatLng endLocation = new LatLng(
                        polylineData.getLeg().endLocation.lat,
                        polylineData.getLeg().endLocation.lng
                );
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(endLocation)
                        .title("rute: " + index)
                        .snippet("estimasi perjalanan: " + polylineData.getLeg().duration)
                );

                Log.d(TAG, "jarak ::: " + polylineData.getLeg().distance);

                marker.showInfoWindow();
                mTripMarkers.add(marker);


            } else {
                polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.darkGrey));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }

    @Override
    public void onMapRvClicked(int position) {
        Log.d(TAG, "onMapRvClicked: "+urutanWisataPojo.get(position).getNamaWisata());

        String selectedList = urutanWisataPojo.get(position).getNamaWisata();

        mDb.collection("lokasi wisata")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

//                            mLokasiWisata = new LokasiWisata(document.getGeoPoint("geo_point_wisata"),
//                                    document.getString("nama_wisata"),document.get("id_wisata"));
                        mLokasiWisata = document.toObject(LokasiWisata.class);
                        if (selectedList.equals(mLokasiWisata.getNama_wisata())){
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLokasiWisata.getGeo_point_wisata().getLatitude(),mLokasiWisata.geo_point_wisata.getLongitude()),19),
                                    600,
                                    null
                            );
                            break;
                        }

                    }
                }
            }

        });


    }
}
