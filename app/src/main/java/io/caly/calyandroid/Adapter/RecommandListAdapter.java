package io.caly.calyandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.caly.calyandroid.Activity.MapActivity;
import io.caly.calyandroid.R;

/**
 * Created by jspiner on 2017. 2. 27..
 */

public class RecommandListAdapter extends RecyclerView.Adapter<RecommandListAdapter.ViewHolder>  {


    private ArrayList<Object> dataList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imv_reco_food)
        ImageView imvFood;

        @Bind(R.id.imv_reco_map)
        ImageView imvMap;

        Context context;

        public ViewHolder(Context context, View view){
            super(view);

            this.context = context;
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.imv_reco_map)
        void onMapClick(){
            Intent intent = new Intent(context, MapActivity.class);
            context.startActivity(intent);
        }
    }

    public RecommandListAdapter(ArrayList<Object> dataList){
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recolist_row, parent, false);

        ViewHolder holder = new ViewHolder(parent.getContext(), view);
        Picasso.with(parent.getContext())
                .load(R.drawable.sample_food_1)
                .into(holder.imvFood);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}