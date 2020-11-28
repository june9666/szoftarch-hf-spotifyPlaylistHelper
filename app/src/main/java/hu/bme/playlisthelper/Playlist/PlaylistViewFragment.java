package hu.bme.playlisthelper.Playlist;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hu.bme.playlisthelper.R;
import hu.bme.playlisthelper.api.Connectors.PlaylistService;
import hu.bme.playlisthelper.api.Connectors.SongService;


/**
 * A fragment representing a list of Items.
 */


public class PlaylistViewFragment extends Fragment   {
    private RecyclerView recyclerView;
    public List<PlaylistItem> adapter;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState


    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist_view, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}