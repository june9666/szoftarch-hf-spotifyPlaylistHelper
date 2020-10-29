package hu.bme.playlisthelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class FriendListActivity extends AppCompatActivity implements NewFriendDialogFragment.NewFriendDialogListener, FriendListRecyclerViewAdapter.FriendItemClickListener {

    public FriendListRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private FriendListDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = Room.databaseBuilder(
                getApplicationContext(),
                FriendListDatabase.class,
                "friend-list"
        ).build();


        initRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Friend", "onFABClickcalled");
                FragmentManager fm = getSupportFragmentManager();
                NewFriendDialogFragment newFriendDialogFragment = NewFriendDialogFragment.newInstance();
                newFriendDialogFragment.show(fm, newFriendDialogFragment.TAG);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFriendItemCreated(FriendItem newItem) {
        new AsyncTask<Void, Void, FriendItem>() {

            @Override
            protected FriendItem doInBackground(Void... voids) {
                database.friendItemDao().insertAll(newItem);

                return newItem;
            }

            @Override
            protected void onPostExecute(FriendItem friendItem) {
                Log.d("Friend","onPostExcecute "+ friendItem.name + friendItem.toString());
                adapter.addItem(friendItem);

            }
        }.execute();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new FriendListRecyclerViewAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
