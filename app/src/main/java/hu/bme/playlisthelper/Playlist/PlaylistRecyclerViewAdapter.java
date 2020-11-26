package hu.bme.playlisthelper.Playlist;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hu.bme.playlisthelper.R;

public class PlaylistRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistRecyclerViewAdapter.PlaylistItemViewHolder> {

    private final List<PlaylistItem> items;

    private PlaylistItemClickListener listener;

    public PlaylistRecyclerViewAdapter(PlaylistItemClickListener listener) {
        this.listener = listener;
        items = new ArrayList<>();

    }

    public interface PlaylistItemClickListener{
        void onItemChanged(PlaylistItem item);
        void onItemDeleted(PlaylistItem item);
    }

    @NonNull
    @Override
    public PlaylistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);
        return new PlaylistItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull PlaylistItemViewHolder holder, int position) {
        PlaylistItem item = items.get(position);
        holder.trackName.setText(item.artistName);
        holder.artistName.setText(item.trackname);

        holder.item = item;
    }

    public void addItem(PlaylistItem item) {

        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<PlaylistItem> friendItems) {
        items.clear();
        items.addAll(friendItems);
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }


    public List<PlaylistItem> getallsong()
    {return items;}
    class PlaylistItemViewHolder extends RecyclerView.ViewHolder {


        TextView trackName;
        TextView artistName;

        ImageButton removeButton;
        PlaylistItem item;

        @SuppressLint("ClickableViewAccessibility")
        PlaylistItemViewHolder(View itemView) {
            super(itemView);

            trackName = itemView.findViewById(R.id.PlaylistItemTrackNameTextView);
            artistName = itemView.findViewById(R.id.PlaylistItemArtistTextView);
            removeButton = itemView.findViewById(R.id.PlaylistItemRemoveButton);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PlaylistItem item = items.get(getAdapterPosition());
                    items.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    listener.onItemDeleted(item);
                }
            });

        }
    }

}