package com.devforxkill.androidcrowdfunding.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devforxkill.androidcrowdfunding.Models.Project;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.devforxkill.androidcrowdfunding.Config.ApiEndPoints;
import com.devforxkill.androidcrowdfunding.R;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView description;
        public ImageView thumbnail;
        public TextView montant;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tvTitle);
            description = (TextView) itemView.findViewById(R.id.tvDescription);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageView);
            montant = (TextView) itemView.findViewById(R.id.tvMontant);
        }
    }

    private List<Project> mListData;
    private Context mContext;

    public ProjectsAdapter(Context context, List<Project> listData){
        mListData = listData;
        mContext = context;
    }

    private Context getmContext(){return mContext;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.projects_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Project m = mListData.get(position);
        Log.d("test",m.getDescription().toString());
        holder.title.setText(m.getTitle());
        holder.description.setText(m.getDescription());
        holder.montant.setText("Objectif : " + m.getMontant() + " €");
        Picasso.get().load(ApiEndPoints.BASE + m.getPicture()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }


}
