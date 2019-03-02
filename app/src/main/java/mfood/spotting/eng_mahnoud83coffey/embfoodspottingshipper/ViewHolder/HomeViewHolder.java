package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.R;


public class HomeViewHolder extends RecyclerView.ViewHolder


{


    public TextView textOrderId,textOrderStatus,textOrderPhone,textOrderAddress,textOrderTimeDate;
    public Button btnShipping;




    public HomeViewHolder(View itemView)
    {
        super(itemView);

        textOrderId=(TextView)itemView.findViewById(R.id.order_status_Id);
        textOrderStatus=(TextView)itemView.findViewById(R.id.order_status);
        textOrderPhone=(TextView)itemView.findViewById(R.id.order_status_phone);
        textOrderAddress=(TextView)itemView.findViewById(R.id.order_status_address);
        textOrderTimeDate=(TextView)itemView.findViewById(R.id.order_date_Id);

        btnShipping=(Button)itemView.findViewById(R.id.btnshipping);


    }






}
