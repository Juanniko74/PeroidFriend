package com.example.peroidfriend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterUpdateData extends RecyclerView.Adapter<AdapterUpdateData.MyViewHolder> {

    private List<ModelData> dataList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String documentId);
        void onDeleteClick(String documentId, int position);
    }

    public AdapterUpdateData(List<ModelData> dataList, OnItemClickListener onItemClickListener) {
        this.dataList = dataList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.updatedataitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelData data = dataList.get(position);
        holder.startDateTextView.setText(data.getStartDate());
        holder.finishDateTextView.setText(data.getFinishDate());
        holder.expressionTextView.setText(data.getExpression());
        holder.painScaleTextView.setText(data.getPainScale());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(data.getDocumentId());
            }
        });

        holder.deleteImageView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDeleteClick(data.getDocumentId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView startDateTextView;
        TextView finishDateTextView;
        TextView expressionTextView;
        TextView painScaleTextView;
        ImageView deleteImageView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            finishDateTextView = itemView.findViewById(R.id.finishDateTextView);
            expressionTextView = itemView.findViewById(R.id.expressionTextView);
            painScaleTextView = itemView.findViewById(R.id.painScaleTextView);
            deleteImageView = itemView.findViewById(R.id.deletedata);
        }
    }
}
