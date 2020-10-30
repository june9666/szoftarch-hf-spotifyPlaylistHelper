package hu.bme.playlisthelper.Playlist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hu.bme.playlisthelper.R;


/**
 * A fragment representing a list of Items.
 */
public class PlaylistViewFragment extends Fragment  {
    private RecyclerView recyclerView;
    public PlaylistRecyclerViewAdapter adapter;

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
        initRecyclerView();
    }
    private void initRecyclerView() {
        recyclerView = getView().findViewById(R.id.PlaylistRecyclerView);
        adapter = new PlaylistRecyclerViewAdapter((PlaylistRecyclerViewAdapter.PlaylistItemClickListener) getActivity());
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
    }
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<PlaylistItem>>() {

            @Override
            protected List<PlaylistItem> doInBackground(Void... voids) {
                PlaylistActivity activity = (PlaylistActivity) getActivity();
                return activity.database.playlistItemDao().getAll();
            }

            @Override
            protected void onPostExecute(List<PlaylistItem> playlistItems) {
                adapter.update(playlistItems);
            }
        }.execute();
    }


}