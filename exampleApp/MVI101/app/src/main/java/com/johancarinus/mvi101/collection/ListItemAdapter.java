package com.johancarinus.mvi101.collection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.johancarinus.mvi101.R;
import com.johancarinus.mvi101.models.ListItemData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {

    private List<ListItemData> listItemData;
    private PublishSubject<Integer> removeItemIntentObservable;

    public ListItemAdapter(List<ListItemData> listItemData) {
        this.listItemData = listItemData;
        this.removeItemIntentObservable = PublishSubject.create();
    }

    public void update(List<ListItemData> listItemData) {
        this.listItemData = listItemData;
        this.notifyDataSetChanged();
    }

    public Observable<Integer> removeItemIntents() {
        return this.removeItemIntentObservable;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemViewHolder holder, int position) {
        holder.description.setText(listItemData.get(position).getDescription());
        holder.position = position;
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItemAdapter.this.removeItemIntentObservable.onNext(holder.position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemData.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_description)
        TextView description;

        @BindView(R.id.button_remove)
        Button remove;

        int position;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
