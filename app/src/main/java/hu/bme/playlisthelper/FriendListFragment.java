package hu.bme.playlisthelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.room.Room;
import hu.bme.playlisthelper.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 */
public class FriendListFragment extends Fragment implements FriendListRecyclerViewAdapter.FriendItemClickListener, NewFriendDialogFragment.NewFriendDialogListener {

    private FriendListDatabase database;
    private RecyclerView recyclerView;
    private FriendListRecyclerViewAdapter adapter;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewFriendDialogFragment().show(, NewFriendDialogFragment.TAG);
            }
        });

        database = Room.databaseBuilder(
                view.getContext(),
                FriendListDatabase.class,
                "friend-list"
        ).build();

        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.MainRecyclerView);
        adapter = new FriendListRecyclerViewAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemChanged(final FriendItem item) {
        final AsyncTask<Void, Void, Boolean> execute = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {

                database.friendItemDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("MainActivity", "FriendItem update was successful");
            }
        }.execute();
    }

    @Override
    public void onItemDeleted(final FriendItem item) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.friendItemDao().deleteItem(item);
                return;
            }
        /*new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {


                database.shoppingItemDao().deleteItem(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {

                Log.d("MainActivity", "ShoppingItem deleted " + adapter.getItemCount());

            }
        }.execute();
*/
        }).start();
    }
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<FriendItem>>() {

            @Override
            protected List<FriendItem> doInBackground(Void... voids) {
                return database.friendItemDao().getAll();
            }

            @Override
            protected void onPostExecute(List<FriendItem> friendItems) {
                adapter.update(friendItems);
            }
        }.execute();
    }
}