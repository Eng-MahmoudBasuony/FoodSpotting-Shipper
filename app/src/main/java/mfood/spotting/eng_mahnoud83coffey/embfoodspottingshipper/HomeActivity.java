package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Common.Common;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.Request;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.Token;


import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.ViewHolder.HomeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class HomeActivity extends AppCompatActivity {




    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference shipperorders;

    private FirebaseRecyclerAdapter<Request,HomeViewHolder> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


             //-----------------ID----------------------//
             recyclerView=(RecyclerView)findViewById(R.id.recyclerview_homeActivity);

             layoutManager=new LinearLayoutManager(this);
             recyclerView.setLayoutManager(layoutManager);
             recyclerView.setHasFixedSize(true);



            //Init   Firebase
            database=FirebaseDatabase.getInstance();
            shipperorders=database.getReference(Common.ORDER_NEED_SHIP);

              //Storage your Token app to FirebaseDatabse
             updateTokenShipper(FirebaseInstanceId.getInstance().getToken());


            loadAllOrderNeededShip(Common.currentShipper.getPhone());


    }



    private void loadAllOrderNeededShip(String phone)
    {

        DatabaseReference orderOnChildOfShipper=shipperorders.child(phone);

        FirebaseRecyclerOptions<Request>options=new FirebaseRecyclerOptions.Builder<Request>()
                                                    .setQuery(orderOnChildOfShipper,Request.class)
                                                    .build();

        adapter=new FirebaseRecyclerAdapter<Request, HomeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HomeViewHolder holder, final int position, @NonNull final Request model)
            {

                //Send Image Name to Recyclerview
                holder.textOrderId.setText(adapter.getRef(position).getKey());
                Common.currentIdOrder=adapter.getRef(position).getKey();
                holder.textOrderStatus.setText(Common.converCodeToStatus(model.getStatus()));
                holder.textOrderPhone.setText(model.getPhoneClient());
                holder.textOrderAddress.setText(model.getAddress());
                holder.textOrderTimeDate.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));


                holder.btnShipping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {


                            Common.currentRequest = model;
                            Common.currentKey = adapter.getRef(position).getKey();

                            Intent mapIntent = new Intent(HomeActivity.this, TrakingOrder.class);
                                   mapIntent.putExtra("Latlng",model.getLatlng());
                            startActivity(mapIntent);

                        }




                });




            }

            @NonNull
            @Override
            public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);

                return new HomeViewHolder(view);
            }

        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void updateTokenShipper(String token)
    {

        DatabaseReference tokens=database.getReference("Tokens");
        Token data=new Token(token,false);

        tokens.child(Common.currentShipper.getPhone()).setValue(data);

    }



    //-----Life Cycle-----//
    @Override
    protected void onResume()
    {
        super.onResume();

        loadAllOrderNeededShip(Common.currentShipper.getPhone());

        }
    @Override
    protected void onPause()
    {
        super.onPause();

        if (adapter!=null)
            adapter.stopListening();


    }
    @Override
    protected void onRestart()
    {
        super.onRestart();

        loadAllOrderNeededShip(Common.currentShipper.getPhone());
    }
    @Override
    protected void onStop()
    {
        super.onStop();

        if (adapter!=null)
           adapter.stopListening();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



      /*
     * انشاء Reference من الـ location manager ثم يقوم بفحص اذا ما كان ال gps مفعلا ام لا
     *اذا كان مفعل اظهر toast انه مفعل واذا كان غير مفعل قم باظهار Dialog توضيحية للمستخدم
     *ظهر بها رسالة توضيحية للمستخدم ان ال gps غير مفعل هل تريد تفعيله
     *وزر يذهب مباشرة للاعدادات الخاصة بالLocation لكى يقوم المستخدم بالتفعيل وبالتالى يمكن للتطبيق الخاص بنا تحديد موقعه .
     * */

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")

                .setCancelable(false)

                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){

                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}
