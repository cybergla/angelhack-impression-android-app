package com.impression.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.impression.R;

/**
 * Created by Pulkit Juneja on 12-Jun-16.
 */
public class templateAdapter extends RecyclerView.Adapter<templateAdapter.ViewHolder> {


    public templateAdapter(Context c , int []  res , GenericDataListener<Integer> oc)
    {
        context = c;
        layoutIds = res;
        onClickListener=oc;
    }


    int [] layoutIds;
    Context context;
    ViewGroup par ;
    GenericDataListener<Integer> onClickListener;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView screen ;

        public ViewHolder(View itemView) {
            super(itemView);
            screen = (TextView) itemView.findViewById(R.id.img_screen);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int resID = layoutIds[getAdapterPosition()];
            onClickListener.onData(resID);
        }
    }

    public void setOnClickListener(GenericDataListener<Integer> l){onClickListener=l;}

    @Override
    public templateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         if(par==null)
             par=parent;
            View v = LayoutInflater.from(context).inflate(R.layout.adapter_template_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(templateAdapter.ViewHolder holder, int position) {

        holder.screen.setText(context.getResources().getResourceEntryName(layoutIds[position]));

    }

    @Override
    public int getItemCount() {
        return layoutIds.length;
    }
}
