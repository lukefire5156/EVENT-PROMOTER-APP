package com.lukefire.dummyevent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull final View itemView) {
        super(itemView);
        
        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), , Toast.LENGTH_SHORT).show();
            }
        });*/
        mView= itemView;
        /////
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mRegistration= v.findViewById(R.id.rRegistration);
                String nam= mRegistration.getText().toString();
                //String nam2= nam.replace("Register Here:","");

                /*
                Intent i = new Intent(itemView.getContext(), test.class);
                i.putExtra("key", (Serializable) v);
                itemView.getContext().startActivity(i);
                */

                //String pos= String.valueOf(v.getVerticalScrollbarPosition());
                //Toast.makeText(itemView.getContext(), nam, Toast.LENGTH_SHORT).show();
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nam));
                    itemView.getContext().startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(itemView.getContext(), "Registration Link is not ready yet...comming soon", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void setDetails(Context ctx,String title, String description, String image,String Logo,String Registration_Link){

        TextView mTitleTv= mView.findViewById(R.id.rTitleTv);
        TextView mDetailTv = mView.findViewById(R.id.rDescriptionTv);
        TextView mRegistration = mView.findViewById(R.id.rRegistration);
        ImageView mImageIv= mView.findViewById(R.id.rImageView);
        ImageView mLogoTv= mView.findViewById(R.id.rLogoView);
        mTitleTv.setText(title);
        mDetailTv.setText("About Event:- "+description);
        mRegistration.setText(Registration_Link);
        Picasso.with(ctx).load(image).into(mImageIv);
        Picasso.with(ctx).load(Logo).into(mLogoTv);
    }



}
