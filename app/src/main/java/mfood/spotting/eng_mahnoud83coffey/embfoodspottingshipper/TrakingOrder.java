package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper;

import android.Manifest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.util.DirectionConverter;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Common.Common;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.DataMessage;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.FeedbackDelivery;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.MyResponse;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.SuccessfulRequest;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.Token;



import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Remote.ApiService;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrakingOrder extends FragmentActivity implements OnMapReadyCallback {


    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11101;
    private SupportMapFragment mapFragment;
    private MaterialAnimatedSwitch animatedSwitch;
    private Button btnShipped, btnCallClint;
    //----------------------------
    private FirebaseDatabase database;
    private DatabaseReference refDriversLocation;
    private DatabaseReference referenceSuccessfulRequest;
    private DatabaseReference referenceRequests;
    private DatabaseReference referenceOrderNeedShipped;
    private DatabaseReference referenceFeedbackService;
    //---------------------------
    private GoogleMap mMap;

    //-------------
    Marker markerYorLocation;
    Marker markerClintLocation;

    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    GeoFire geoFireDataBase; //هخزن فيها خطوط الطول والعرض للمكان

    //---------Direction--------//
    LatLng shipperCurrentLocation, clintCurrentLocation;

    //------------------
    private ProgressDialog progressDialog;

    private ApiService mApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traking_order);
        //------Id--------//

        initViews();
        progressDialog = new ProgressDialog(this);

        //Init Service
        mApiService = Common.getFCMClinet();

        //-------init--------//
        database = FirebaseDatabase.getInstance();
        refDriversLocation = database.getReference(Common.DRIVER_LOCATION);
        referenceSuccessfulRequest = database.getReference(Common.SUCCESSFUL_RREQUEST_TO_CLIENT);
        referenceRequests = database.getReference(Common.REQUESTS);
        referenceOrderNeedShipped = database.getReference(Common.ORDER_NEED_SHIP);
        referenceFeedbackService=database.getReference(Common.FEEDBACK_SERVICE);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geoFireDataBase = new GeoFire(refDriversLocation);

        //--Get Location Clint
        if (getIntent() != null) {

            String latLngClint = getIntent().getStringExtra("Latlng");
            String[] separatedLocation = latLngClint.split(",");

            separatedLocation[0] = separatedLocation[0].trim();
            separatedLocation[1] = separatedLocation[1].trim();

            clintCurrentLocation = new LatLng(Double.parseDouble(separatedLocation[0]), Double.parseDouble(separatedLocation[1]));


        }

        //--------Check open GPS ------------//
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        //mMap.setMinZoomPreference(10);


    }


    private void initViews() {
        //-------------Id------------------//
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        animatedSwitch = (MaterialAnimatedSwitch) findViewById(R.id.pin);
        btnShipped = (Button) findViewById(R.id.btnShipped);
        btnCallClint = (Button) findViewById(R.id.btnCall);


        //------Event---------------//

        //---Run Map and Gel your Location and Draw Rout to ClintLocation
        animatedSwitch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean b) {
                if (b) //True
                {
                    startGettingLocation();
                } else //False
                {

                    stopGettingLocation();
                }
            }
        });


        //--Remove Order From tables Requests ,
        btnShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                //we will using current.Millis to Key
                final String ShipperorderNumber = String.valueOf(System.currentTimeMillis());

                SuccessfulRequest successfulRequest = new SuccessfulRequest(Common.currentShipper.getName(), Common.currentShipper.getPhone(), Common.currentKey, "0", "not Feedback",
                        Common.currentKey, Common.currentRequest.getAddress(), Common.currentRequest.getPaymentMethod(), Common.currentRequest.getTotal(), "Successful", ShipperorderNumber,Common.currentRequest.getName(),Common.currentRequest.getPhoneClient());

                //Set Data TO Table SuccessfulRequestTable
                referenceSuccessfulRequest.child(ShipperorderNumber).setValue(successfulRequest)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //--Remove Item Order From Table "OrderNeedShip"
                                referenceOrderNeedShipped.child(Common.currentShipper.getPhone()).child(Common.currentKey).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                //--Remove Item Order From Table "Requests"
                                                referenceRequests.child(Common.currentKey).removeValue()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                //--Set Data to Model  "FeedbackService"
                                                                FeedbackDelivery feedbackDelivery=new FeedbackDelivery(Common.currentShipper.getName()
                                                                                                                       ,"0",
                                                                                                                       Common.currentKey,ShipperorderNumber);
                                                                //--Set Data to Table  "FeedbackService"
                                                                referenceFeedbackService.child(Common.currentRequest.getPhoneClient())
                                                                                        .child(Common.currentKey)
                                                                                        .setValue(feedbackDelivery)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid)
                                                                                            {
                                                                                                sendNotificationToServer(Common.currentKey);
                                                                                                sendNotificationToClient(Common.currentKey);

                                                                                                progressDialog.dismiss();
                                                                                                Toast.makeText(TrakingOrder.this, " finished successfully", Toast.LENGTH_SHORT).show();

                                                                                                finish();



                                                                                            }
                                                                                        });
                                                            }
                                                        });

                                            }
                                        });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(TrakingOrder.this, "Failure" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        btnCallClint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = Common.currentRequest.getPhoneClient();

                String uri = "tel:" + phone.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));

                if (ActivityCompat.checkSelfPermission(TrakingOrder.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


                    return;
                }
                startActivity(intent);



            }
        });



    }




    //---------Get Your Location-------//
    private void startGettingLocation()
    {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this
                    ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}
                    ,MY_PERMISSIONS_REQUEST_LOCATION);
        }else
        {
            prepareLocationRequest();
            prepareCallBack();
            fusedLocationProviderClientPermission();
        }


    }

    private  void stopGettingLocation()
    {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        fusedLocationProviderClient.removeLocationUpdates(locationCallback); //هوقف الresponse اللى راجع من السيرفر
        mMap.setMyLocationEnabled(false);

        Snackbar.make(mapFragment.getView(),"Your OffLine",Snackbar.LENGTH_SHORT).show();


    }

    private void fusedLocationProviderClientPermission()
    {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);

        Snackbar.make(mapFragment.getView(),"Your Online",Snackbar.LENGTH_SHORT).show();

    }

    private void prepareLocationRequest()
    {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setSmallestDisplacement(.00001f);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void prepareCallBack()
    {
        locationCallback=new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {super.onLocationResult(locationResult);

                //Your Location
                List<Location>locations=locationResult.getLocations();

                if (locations.size()>0)
                {

                    final Location mLastLocation=locations.get(locations.size()-1);//First Rout Storage in List

                    //---Storage Location In Firebase
                    geoFireDataBase.setLocation(Common.currentShipper.getPhone(),//ال key اللى هتحفظ تحتها الlocation
                            new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), //الlocation اللى عاوز تحفظه
                            new GeoFire.CompletionListener() {// listiner هيرجع بالنتيجه العمليه
                                @Override
                                public void onComplete(String key, DatabaseError error)
                                {
                                    //  Toast.makeText(MapsActivityTest.this, "Success save Location "+key+"In Firebase", Toast.LENGTH_SHORT).show();
                                    //Marker Your Location Shipper
                                    if (markerYorLocation!=null)//يعنى موجود على الخريطه وواخد location
                                        markerYorLocation.remove(); //احذف العلامه ديه عشان هديله الlocation الاحدث لموقعى

                                     //Add Marker On Your Location
                                    markerYorLocation=mMap.addMarker(new MarkerOptions()
                                            .title(Common.currentShipper.getName())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.markershipper))
                                            .position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));

                                    //markerYorLocation.setDraggable(true);//عشان محدش يحركه بأيده

                                    //معنى انك تضيف الanimatCamer هنا انك هتعملzome على الyourLocation بالمسافه اللى حددتها
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),15) );



                                    //Marker Clint
                                    if (markerClintLocation!=null)//يعنى موجود على الخريطه وواخد location
                                        markerClintLocation.remove(); //احذف العلامه ديه عشان هديله الlocation الاحدث لموقعى

                                    //Add Clint Marker
                                    markerClintLocation=mMap.addMarker(new MarkerOptions()
                                            .title(Common.currentRequest.getName())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerclint))
                                            .position(new LatLng(clintCurrentLocation.latitude,clintCurrentLocation.longitude) ));

                                    //Add Circle Around Location Clint
                                    CircleOptions circleOptions = new CircleOptions();
                                    circleOptions.center(new LatLng(clintCurrentLocation.latitude,clintCurrentLocation.longitude));
                                    circleOptions.radius(50);
                                    circleOptions.fillColor(Color.BLUE);
                                    circleOptions.strokeColor(Color.RED);
                                    circleOptions.strokeWidth(4);

                                    mMap.addCircle(circleOptions);



                                    //---After Call Back Draw And Chose Route between 2 Points
                                    shipperCurrentLocation =new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());


                                   // getDirectionAndDraw();
                                    if (shipperCurrentLocation!=null&&clintCurrentLocation!=null)
                                    {

                                        getDirectionAndDraw(shipperCurrentLocation,clintCurrentLocation);
                                    }


                                }
                            });



                }


            }


        };



    }

    //Method Open Gps
    private void showGPSDisabledAlertToUser()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:

                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED)
                {
                    prepareLocationRequest();
                    prepareCallBack();
                    fusedLocationProviderClientPermission();

                }else
                {
                    Toast.makeText(this, "محتاجين السماحيات يابيه ", Toast.LENGTH_SHORT).show();
                }

                break;




        }



    }

    private void getDirectionAndDraw(LatLng shipperCurrentLocation, LatLng clintCurrentLocation)
    {
        //المكتبه هتحددلك الاتجاهات فقط وبعدين انت ترسم بأيدك
        GoogleDirection.withServerKey(getResources().getString(R.string.google_directions_key))
                .from(this.shipperCurrentLocation)
                .to(this.clintCurrentLocation)
                .transportMode(TransportMode.DRIVING) //بتحددله انك راكب عشان يقترحلك الطرق المناسبه
                //.avoid(AvoidType.HIGHWAYS) //بتقوله هنا تجنب الطرق العاليه
                //.avoid(AvoidType.FERRIES)
                .transitMode(TransitMode.BUS)
                .unit(Unit.METRIC)
                .execute(new DirectionCallback() {
                    @Override//فى حال حدد الاتجاه بين المنطقتين هيخش هنا
                    public void onDirectionSuccess(Direction direction, String rawBody) //وطبعا أنت لا تحدد الاتجاهات فقط لمجرد تحديد الاتجاهات بل لتقوم بامر اخر والذى يكون فى اغلب الاحيان رسم خط على الخريطة يوضح هذه الاتجاهات
                    {
                        //نتيجة الاتصال السابق على هيئة List تحتوى على Route أو اكثر حيث أحيانا يكون هناك عدة طرق للوصول لمكان اخر من هذا المكان
                        //يحتوى الـ Route على Leg أو أكثر  وهو المسافه بين مكان واخر
                        //ويحتوى الـ Leg على step وهى المسافة بين مكان واخر داخل نفس الـ Leg
                        //ذلك نستدعى ذلك من النتيجة ونقوم بناءا عليه بإنشاء Polyline ونرسمه على الخريطة بناءا على بيانات الاتجاهات الى حصلنا عليها كما يتضح من الكود التالى

                        if (direction.isOK()) {

                            Leg leg = direction.getRouteList().get(0).getLegList().get(0);

                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(TrakingOrder.this, directionPositionList, 5, Color.RED);//.jointType(JointType.ROUND);

                            mMap.addPolyline(polylineOptions);

                            //قمنا بجلب الـ Leg
                            //  ثم استخدمنا الميثود getDirectionPoint والتى تعطى لنا امكانية الحصول على الاتجاهاات على هيئة مصفوفة من خطوط الطول والعرض المتتالية
                            // ثم قمنا باستخدام الميثود createPolyline الموجودة بكلاس DirectionCoverter والتى أخذت منا باراميترات (الكونتكست ، ليست الاتجاهات ، سمك الخط ، اللون )
                            // ثم قمنا باضافة هذا الـ polyline للخريطة .

                            Log.e("Direction", "onDirectionSuccess: ");
                        } else {

                            Log.e("Direction", direction.getErrorMessage());

                        }

                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.e("Direction", "faild" + t.getLocalizedMessage());
                    }
                });
    }

    //--Send Notification from Shipper to Server--//
    private void sendNotificationToServer(final String orderNumber)
    {
        final DatabaseReference referenceTokens=FirebaseDatabase.getInstance().getReference("Tokens");

        Query queryData =referenceTokens.orderByChild("serverToken").equalTo(true);// get All node isServerToken is True


        queryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Token serverToken=snapshot.getValue(Token.class);

                    //Create Raw payload to send
                   // Notification notification=new Notification("Shipper : "+Common.currentShipper.getName(),"done Successful send Request "+orderNumber);
                //    Sender content=new Sender(serverToken.getToken(),notification);


                    Map<String,String> dataSend = new HashMap<>();
                    dataSend.put("title","Food Spotting");
                    dataSend.put("body","Done Send to Order "+orderNumber);

                    DataMessage dataMessage = new DataMessage(serverToken.getToken(),dataSend);



                    mApiService.sendNotification(dataMessage).enqueue(new Callback<MyResponse>()
                    {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response)
                        {


                            if (response.code()==200)
                            {
                                if (response.body().success == 1) {

                                    Toast.makeText(TrakingOrder.this, "Successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(TrakingOrder.this, "Failed !!!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t)
                        {
                            Log.e("Error ", t.getMessage() );
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //--Send Notification Feedback from Shipper to Client--//
    private void sendNotificationToClient(final String currentKey)
    {
        final DatabaseReference referenceToken=database.getReference("Tokens");


        referenceToken.child(Common.currentRequest.getPhoneClient())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            Token token=dataSnapshot.getValue(Token.class);

                            Map<String,String> dataSend = new HashMap<>();
                            dataSend.put("title","Food Spotting");
                            dataSend.put("body","Please rate this service for this "+currentKey);

                            DataMessage dataMessage = new DataMessage(token.getToken(),dataSend);


                            mApiService.sendNotification(dataMessage)
                                    .enqueue(new Callback<MyResponse>()
                                    {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response)
                                        {
                                            if (response.body().success==1)
                                            {
                                                Toast.makeText(TrakingOrder.this, "Order Was Update", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                Toast.makeText(TrakingOrder.this, "Order Was Update but failed send Notification !", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t)
                                        {

                                            Log.e("Error", t.getMessage());
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });

    }

}




