package hu.bme.playlisthelper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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



/**
 * A fragment representing a list of Items.
 */
public class FriendListFragment extends Fragment  implements FriendListRecyclerViewAdapter.FriendItemClickListener {
    private RecyclerView recyclerView;
    private FriendListRecyclerViewAdapter adapter;
    public FriendListDatabase database;

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
                FragmentManager fm = getActivity().getSupportFragmentManager();
                NewFriendDialogFragment newFriendDialogFragment = NewFriendDialogFragment.newInstance();
                newFriendDialogFragment.show(fm, newFriendDialogFragment.TAG);
            }
        });

        MainActivity main = (MainActivity) getActivity();
        database = main.getFriendListDatabase();
        initRecyclerView(view);

    }
    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.MainRecyclerView);
        adapter = new FriendListRecyclerViewAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
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


}