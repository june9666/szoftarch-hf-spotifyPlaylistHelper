package hu.bme.playlisthelper.Playlist;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

import hu.bme.playlisthelper.FriendList.FriendItem;
import hu.bme.playlisthelper.FriendList.FriendListRecyclerViewAdapter;
import hu.bme.playlisthelper.FriendList.NewFriendDialogFragment;
import hu.bme.playlisthelper.R;
import hu.bme.playlisthelper.SplashActivity;
import hu.bme.playlisthelper.api.Connectors.SongService;

public class PlaylistActivity extends AppCompatActivity implements PlaylistCreationFragment.NewPlaylistDialogListener,PlaylistRecyclerViewAdapter.PlaylistItemClickListener {

    public PlaylistRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    public PlaylistDatabase database;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = Room.databaseBuilder(
                getApplicationContext(),
                PlaylistDatabase.class,
                "play-list"
        ).build();

        adapter = new PlaylistRecyclerViewAdapter(this);
        sharedPreferences = getSharedPreferences("SPOTIFY",0);
    }



    public void initRecyclerView() {
        recyclerView = findViewById(R.id.PlaylistRecyclerView);
        adapter = new PlaylistRecyclerViewAdapter(this) ;
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<PlaylistItem>>() {

            @Override
            protected List<PlaylistItem> doInBackground(Void... voids) {

                return database.playlistItemDao().getAll();
            }

            @Override
            protected void onPostExecute(List<PlaylistItem> playlistItems) {
                adapter.update(playlistItems);
            }
        }.execute();
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

        //TODO ennek kéne új gomb valami értelmes helyen, ezeket a függvényeket hívd meg, lehetőleg ebben az activityben
        if (id == R.id.action_settings) {
            SongService create = new SongService(getApplicationContext(),sharedPreferences.getString("playlistid",""));
            create.addSongToLibrary(adapter.getallsong());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemChanged(final PlaylistItem item) {
        final AsyncTask<Void, Void, Boolean> execute = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {

                database.playlistItemDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("MainActivity", "FriendItem update was successful");
            }
        }.execute();
    }

    @Override
    public void onItemDeleted(final PlaylistItem item) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.playlistItemDao().deleteItem(item);
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


    @Override
    public void onPlaylistItemCreated(PlaylistItem newItem) {
        new AsyncTask<Void, Void, PlaylistItem>() {

            @Override
            protected PlaylistItem doInBackground(Void... voids) {
                database.playlistItemDao().insertAll(newItem);

                return newItem;
            }

            @Override
            protected void onPostExecute(PlaylistItem playlistItem) {
                if (playlistItem != null){
                    adapter.addItem(playlistItem);
                }
                initRecyclerView();


            }
        }.execute();
    }

}
