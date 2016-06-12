package com.impression.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.impression.R;

import java.util.ArrayList;

/**
 * Created by ayushgulati on 6/12/16.
 */
public class chooseCardAdapter extends RecyclerView.Adapter<chooseCardAdapter.ViewHolder> {

    Context context ;
    ArrayList<CardModel> data ;
    GenericDataListener<CardModel> clickListener;

    public  chooseCardAdapter(Context c , ArrayList<CardModel> cm , GenericDataListener<CardModel> cl)
    {
        context = c ;
        data = cm ;
        clickListener = cl ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_choose_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         holder.name.setText(data.get(position).CardName);
         holder.tempName.setText(data.get(position).templateId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name , tempName ;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView);
            tempName = (TextView)itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CardModel mod = data.get(getAdapterPosition());
            clickListener.onData(mod);
         }
    }
}
