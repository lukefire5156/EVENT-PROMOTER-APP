package com.lukefire.dummyevent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class clubViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public clubViewHolder(@NonNull final View itemView) {
        super(itemView);
        
        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), , Toast.LENGTH_SHORT).show();
            }
        });*/
        mView= itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mUID= v.findViewById(R.id.rUID);
                String UID= mUID.getText().toString();
                Intent intent = new Intent(mView.getContext(), EditEvent.class);
                intent.putExtra("message",UID);
                mView.getContext().startActivity(intent);
            }
        });

        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mRegistration= v.findViewById(R.id.rRegistration);
                String nam= mRegistration.getText().toString();
                //String nam2= nam.replace("Register Here:","");



                //String pos= String.valueOf(v.getVerticalScrollbarPosition());
                //Toast.makeText(itemView.getContext(), nam, Toast.LENGTH_SHORT).show();
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nam));
                    itemView.getContext().startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(itemView.getContext(), "Registration Link is not ready yet...comming soon", Toast.LENGTH_SHORT).show();
                }


            }
        });*/
    }

    public void setDetails(Context ctx,String title,String image,String UID){

        TextView mTitleTv= mView.findViewById(R.id.rTitleTv);
        TextView mUID= mView.findViewById(R.id.rUID);
        ImageView mImageIv= mView.findViewById(R.id.rImageView);
        mTitleTv.setText(title);
        mUID.setText(UID);
        Picasso.with(ctx).load(image).into(mImageIv);

    }



}
