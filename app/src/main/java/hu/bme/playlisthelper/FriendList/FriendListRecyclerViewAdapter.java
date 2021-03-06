package hu.bme.playlisthelper.FriendList;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hu.bme.playlisthelper.R;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class FriendListRecyclerViewAdapter extends RecyclerView.Adapter<FriendListRecyclerViewAdapter.FriendViewHolder> {

    private final List<FriendItem> items;

    private FriendItemClickListener listener;

    public FriendListRecyclerViewAdapter(FriendItemClickListener listener) {
        this.listener = listener;
        items = new ArrayList<>();
    }

    public interface FriendItemClickListener{
        void onItemChanged(FriendItem item);
        void onItemDeleted(FriendItem item);
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_friend_list, parent, false);
        return new FriendViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        FriendItem item = items.get(position);
        holder.nameTextView.setText(item.name);
        holder.usernameTextView.setText(item.username);
        if (item.category.equals(FriendItem.Category.FAMILY)) {
            holder.iconImageView.setBackgroundResource(R.drawable.family);
        }else {
            holder.iconImageView.setBackgroundResource(R.drawable.friends);
        }


        holder.isDefaultCheckBox.setChecked(item.isDefault);

        holder.item = item;
    }

    public ArrayList<String> getIds(){
        ArrayList<String> temp = new ArrayList<>();
        for (FriendItem i: items
             ) {
            if (i.isDefault)
            {
                temp.add(i.username);
            }

        }

        return temp;
    }
    public ArrayList<String> getIds(int max){
        int m = 1;
        ArrayList<String> temp = new ArrayList<>();
        for (FriendItem i: items
        ) {
            if (i.isDefault)
            {
                if (m<=max){
                    temp.add(i.username);
                    m++;
                }
            }

        }

        return temp;
    }
    public ArrayList<String> getIdsFam(){
        ArrayList<String> temp = new ArrayList<>();
        for (FriendItem i: items
        ) {
            if (i.category == FriendItem.Category.FAMILY) {
                temp.add(i.username);
            }
        }

        return temp;
    }
    public ArrayList<String> getIdsFam(int max){
        int m = 1;
        ArrayList<String> temp = new ArrayList<>();
        for (FriendItem i: items
        ) {
            if (i.category == FriendItem.Category.FAMILY) {
                if (m<=max){
                    temp.add(i.username);
                    m++;
                }
            }
        }

        return temp;
    }
    public ArrayList<String> getIdsFrand(){
        ArrayList<String> temp = new ArrayList<>();
        for (FriendItem i: items
        ) {
            if (i.category == FriendItem.Category.FRIEND) {
                temp.add(i.username);
            }
        }

        return temp;
    }
    public ArrayList<String> getIdsFrand(int max){
        int m = 1;
        ArrayList<String> temp = new ArrayList<>();
        for (FriendItem i: items
        ) {
            if (i.category == FriendItem.Category.FRIEND) {
                if (m<=max){
                    temp.add(i.username);
                    m++;
                }

            }
        }

        return temp;
    }

    public void addItem(FriendItem item) {

        Log.d("Friend", "Adapter addItem called with" + item.name + item.toString());
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<FriendItem> friendItems) {
        items.clear();
        items.addAll(friendItems);
        notifyDataSetChanged();
    }

    private @DrawableRes
    int getImageResource(FriendItem.Category category) {
        @DrawableRes int ret;
        switch (category) {
            case FAMILY:
                ret = R.drawable.family;
                break;
            case FRIEND:
                ret = R.drawable.friends;
                break;

            default:
                ret = 0;
        }
        return ret;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ShoppingItemClickListener{
        void onItemChanged(FriendItem item);
        void onItemDeleted(FriendItem item);
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImageView;
        TextView nameTextView;
        TextView usernameTextView;

        CheckBox isDefaultCheckBox;
        ImageButton removeButton;

        FriendItem item;

        @SuppressLint("ClickableViewAccessibility")
        FriendViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.FriendItemIconImageView);
            nameTextView = itemView.findViewById(R.id.FriendItemNameTextView);
            usernameTextView = itemView.findViewById(R.id.FriendItemUsernameTextView);
            isDefaultCheckBox = itemView.findViewById(R.id.FriendItemIsDefaultCheckBox);
            removeButton = itemView.findViewById(R.id.FriendItemRemoveButton);


            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FriendItem item = items.get(getAdapterPosition());
                    Log.d("FriendListAdapter", "Item Position"+ getAdapterPosition()
                    );
                    items.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    listener.onItemDeleted(item);

                }
            });


            isDefaultCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    if(item != null){
                        item.isDefault = isChecked;
                        listener.onItemChanged(item);
                    }
                }
            });
        }
    }

}