package AppMenu.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import AppMenu.database.entity.TypeEntity;
import ch.hevs.AppMenu.intro.R;
import AppMenu.util.RecyclerViewItemClickListener;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<TypeEntity> data;
    private RecyclerViewItemClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView textView;
        ImageView imageView;
        ViewHolder(TextView textView, ImageView imageView)
        {
            super(textView);
            this.textView = textView;
            this.imageView = imageView;
        }
    }


    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);
        ImageView i = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_image_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v,i);
        v.setOnClickListener(view -> listener.onItemClick(view, viewHolder.getAdapterPosition()));
        v.setOnLongClickListener(view -> {
            listener.onItemLongClick(view, viewHolder.getAdapterPosition());
            return true;
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        ImageToByteConvertor convertor = new ImageToByteConvertor();
        TypeEntity item = data.get(position);
        holder.imageView.setImageBitmap(convertor.ToBitmap(item.getImage()) );
        holder.textView.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<TypeEntity> data) {
        if (this.data == null) {
            this.data = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RecyclerAdapter.this.data.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

                    if (RecyclerAdapter.this.data instanceof TypeEntity) {
                        return (RecyclerAdapter.this.data.get(oldItemPosition)).getId().equals(
                                (data.get(newItemPosition)).getId());
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (RecyclerAdapter.this.data instanceof TypeEntity) {
                        TypeEntity newType = data.get(newItemPosition);
                        TypeEntity oldType = RecyclerAdapter.this.data.get(newItemPosition);
                        return Objects.equals(newType.getId(), oldType.getId())
                                && Objects.equals(newType.getName(), oldType.getName())
                                && Objects.equals(newType.getImage(), oldType.getImage());
                    }
                    return false;
                }

            });
            this.data = data;
            result.dispatchUpdatesTo(this);
        }
    }
}
