package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Common.Common;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model.Shipper;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private TextInputEditText editPhone,editPassword;
    private Button btnSignIn;

    private FirebaseDatabase database;
    private DatabaseReference shipersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--------------Id----------------------//
        editPhone=(TextInputEditText)findViewById(R.id.Phone_Number_SignIn);
        editPassword=(TextInputEditText)findViewById(R.id.Password_SignIn);
        btnSignIn=(Button)findViewById(R.id.Button_SignIn);


        //-----------Firebase-----------------//
        database=FirebaseDatabase.getInstance();
        shipersRef=database.getReference(Common.SHIPPERS_TABLE);


        //------Event---------//
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                   login(editPhone.getText().toString(),editPassword.getText().toString());
            }
        });



    }

    private void login(String phone, final String password)
    {

        shipersRef.child(phone)
                     .addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                         {
                              if (dataSnapshot.exists())
                              {

                                  Shipper shipper=dataSnapshot.getValue(Shipper.class);

                                  if (shipper.getPassword().equals(password))
                                  {
                                      //Login Success
                                      Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                                      startActivity(intent);

                                      Common.currentShipper =shipper;

                                       finish();
                                  }
                                  else
                                      {
                                          editPassword.setError("Password incorrect!");
                                      }

                              }
                              else
                                  {
                                      Toast.makeText(MainActivity.this, "Your Shipper's  phone not exist", Toast.LENGTH_SHORT).show();
                                      editPhone.setError("phone not exist");
                                  }
                             
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError)
                         {

                         }
                     });

    }


}
