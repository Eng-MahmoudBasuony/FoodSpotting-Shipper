package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.Request;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.Shipper;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.ShippingInformation;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Remote.ApiService;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Remote.RetrofitClint;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Common
{

    public static final String ORDER_NEED_SHIP="OrdersNeedShip";
    public static final String SHIPPERS_TABLE="Shippers";
    public static final String SHIPPING_INFO_TABLE="ShippingOrders";
    public static final String SUCCESSFUL_RREQUEST_TO_CLIENT="SuccessfulRequestToClient";
    public static final String DRIVER_LOCATION="DriverLocation";
    public static final String REQUESTS="Requests";
    public static final String FEEDBACK_SERVICE="FeedbackService";


    //This link provides a reference for the HTTP syntax used to pass messages from your app server to client apps via Firebase Cloud Messaging.
    public static final String BASE_URL="https://fcm.googleapis.com";


    public static Shipper currentShipper;
    public static Request currentRequest;
    public static String  currentKey; //رقم الطلب Number Of The Order

    public static String currentIdOrder;






    //Convert Time System
    //هحول الوقت اللى باللملى ثانيه اللى جاى مع الاوردر لوقت حقيقى
    public static String getDate(long time)
    {
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date=new StringBuilder(DateFormat.format("dd-MM-yyyy hh:mm – E",calendar).toString());


        return date.toString();
    }

    public static String converCodeToStatus(String Status)
    {
        if (Status.equals("0"))
            return "Placed"; //وضعت

        else if (Status.equals("1"))
            return "On my Way"; //انا في طريقي

        else if (Status.equals("2"))
            return "Shipping"; //تم شحنها

        else
            return "Shipped"; //تم تسليمها
    }


    //  استخدام كائن الـ retrofit وربطه بالـ API الخاص بنا
    public static ApiService getFCMClinet()
    {
        return   RetrofitClint.getClint(BASE_URL).create(ApiService.class);
    }


    public static void createShippingOrder(String key, String phone, Location mlastLocation)
    {
        ShippingInformation shippingInformation=new ShippingInformation(key,phone,mlastLocation.getLatitude(),mlastLocation.getLongitude());

                            //shippingInformation.setOrderId(key);
                            //shippingInformation.setShipperPhone(phone);
                            //shippingInformation.setLat(mlastLocation.getLatitude());
                            //shippingInformation.setLng(mlastLocation.getLongitude());

        //Create New Item on ShipperInformation table
        FirebaseDatabase.getInstance()
                        .getReference(Common.SHIPPING_INFO_TABLE)
                        .child(key)
                        .setValue(shippingInformation)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Log.d("Error:", e.getMessage());

                            }
                        });

    }


    public static void updateShippingInformation(String currentKey, Location mlastLocation)
    {

        Map<String,Object>updateLocation=new HashMap<>();

                          updateLocation.put("lat",mlastLocation.getLatitude());
                          updateLocation.put("lng",mlastLocation.getLongitude());


        FirebaseDatabase.getInstance().
                          getReference(SHIPPING_INFO_TABLE)
                          .child(currentKey)
                          .updateChildren(updateLocation)
                          .addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e)
                              {

                                  Log.d("Error:",e.getMessage());
                              }
                          });


    }

    //
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight)
    {
        Bitmap scaleBitmap=Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_4444);

        float scaleX=newWidth/(float)bitmap.getWidth();
        float scaleY=newHeight/(float)bitmap.getHeight();

        float pivotX=0,pivotY=0;

        Matrix scaleMatrix=new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas=new Canvas(scaleBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));


        return scaleBitmap;
    }




}
