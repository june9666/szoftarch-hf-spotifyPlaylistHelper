package hu.bme.playlisthelper.Playlist;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hu.bme.playlisthelper.FriendList.FriendItem;
import hu.bme.playlisthelper.FriendList.FriendListDatabase;
import hu.bme.playlisthelper.FriendList.FriendListRecyclerViewAdapter;
import hu.bme.playlisthelper.R;
import hu.bme.playlisthelper.api.Connectors.PlaylistService;
import hu.bme.playlisthelper.api.Connectors.SongService;
import hu.bme.playlisthelper.api.Connectors.VolleyCallBack;
import hu.bme.playlisthelper.api.Song;

//Itt sok a magic, ehhez ne nyúlj ha nem muszáj.

public class PlaylistCreationFragment extends Fragment implements FriendListRecyclerViewAdapter.FriendItemClickListener{

    public FriendListRecyclerViewAdapter adapter;
    RadioGroup includeGroup;
    RadioButton groupFamily;
    RadioButton groupFriends;
    RadioButton groupSelect;
    RadioButton intersectAll;
    RadioButton intersectCustom;
    EditText text;
    private FriendListDatabase database;
    Button createList;
    int intersectPickerNumber;
    NewPlaylistDialogListener listener;
    int k=0;
    int size=0;
    ArrayList<Song> rp;
    ArrayList<Song> playlist = new ArrayList<>();

    String lastMen;

    ArrayList<String> playlistIds;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onItemChanged(FriendItem item) {

    }

    @Override
    public void onItemDeleted(FriendItem item) {

    }

    public interface NewPlaylistDialogListener {
        void onPlaylistItemCreated(PlaylistItem newItem);
        void onItemChanged(final PlaylistItem item);
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_playlist_creation, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        sharedPreferences = getContext().getSharedPreferences("SPOTIFY",0);

        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                FriendListDatabase.class,
                "friend-list"
        ).build();

        editor = getContext().getSharedPreferences("SPOTIFY", 0).edit();
        adapter = new FriendListRecyclerViewAdapter(this);
        loadItemsInBackground();
        listener = (NewPlaylistDialogListener) getActivity();

        text = view.findViewById(R.id.editTextPlaylistName);
        includeGroup = view.findViewById(R.id.radioGroup);
        groupFamily = view.findViewById(R.id.radio_family);
        groupFriends = view.findViewById(R.id.radio_friends);
        groupSelect = view.findViewById(R.id.radio_select);
        intersectAll = view.findViewById(R.id.radio_intersect_all);
        intersectCustom = view.findViewById(R.id.radio_intersect_custom);
        createList = view.findViewById(R.id.button_createList);
        intersectPickerNumber = 1;


        intersectCustom.setOnClickListener(view12 -> {
            NumberPicker picker = new NumberPicker(view12.getContext());

            picker.setMinValue(1);
            picker.setMaxValue(10);
            picker.setValue(intersectPickerNumber);

            FrameLayout layout = new FrameLayout(view12.getContext());
            layout.addView(picker, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER));

            new AlertDialog.Builder(view12.getContext())
                    .setView(layout)
                    .setTitle("Number of peoples")
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        intersectPickerNumber = picker.getValue();
                        Toast.makeText(view12.getContext(), "Selected " + intersectPickerNumber + ".", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        }
        );

        createList.setOnClickListener(view1 -> {

            editor.putString("playlistname", text.getText().toString());
            editor.apply();
         //   PlaylistService create = new PlaylistService(getActivity().getApplicationContext());
           // create.addSongToLibrary(null);
           // create.postNewComment(getActivity().getApplicationContext());
            getPlaylist();
            transaction.replace(R.id.fragment_playlist_creation, new PlaylistViewFragment());
            transaction.commit();

        });



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
    void getPlaylist(){
        ArrayList<String> ids = new ArrayList<>();

            if (groupFamily.isChecked()) {
                ids = adapter.getIdsFam();
            }
            if (groupFriends.isChecked()) {
                ids = adapter.getIdsFrand();
            }
            if (groupSelect.isChecked()) {
                ids = adapter.getIds();

            }





        k=0;
        size = 0;
        for (String s:ids
             ) {



            PlaylistService play = new PlaylistService(getActivity().getApplicationContext(),s);
            play.getUserPlaylists( new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    playlistIds=play.getPlaylistIds();
                    size += playlistIds.size();
                    for (String id:playlistIds
                         ) {

                        SongService songService = new SongService(getActivity().getApplicationContext(),id);
                        songService.getRecentlyPlayedTracks(new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                rp=songService.getSongs();



                                updateSong(s);
                                if (k==size-1){
                                    enterDatabase();
                                }
                                k++;
                            }
                        });

                    }
                }
            });




        }

                //  in.initRecyclerView();


    }

    private void enterDatabase() {

        Song temp;
        for (int i =0 ;i<playlist.size()-2;i++){
            for (int j = i+1;j<playlist.size()-1;j++){
                if (playlist.get(i).getPop()<playlist.get(j).getPop()){
                    temp = playlist.get(i);
                    playlist.set(i,playlist.get(j));
                    playlist.set(j,temp);
                }
            }
        }
        for (Song entry:playlist
        ) {
            if (intersectCustom.isChecked() && entry.getPop()>=intersectPickerNumber){
                PlaylistItem p= new PlaylistItem();
                p.trackID = entry.getUri();
                p.trackname = entry.getName();
                p.artistName = entry.getArtist();
                p.matches = entry.getPop();
                listener.onPlaylistItemCreated(p);
            }else if (intersectAll.isChecked()){
                PlaylistItem p= new PlaylistItem();
                p.trackID = entry.getUri();
                p.trackname = entry.getName();
                p.artistName = entry.getArtist();
                p.matches = entry.getPop();
                listener.onPlaylistItemCreated(p);
            }

        }

    }

    void updateSong(String id){

        for (Song track:rp )
        {
            int i=0;
            while (i<playlist.size() && !track.getId().equals(playlist.get(i).getId()))
            {

                i++;

            }
            if (i<playlist.size()&& !id.equals(playlist.get(i).firstUser)) {
                playlist.get(i).setPop();
            }
            if (i>=playlist.size()){
                track.firstUser = id;
                playlist.add(track);
            }



        }




    }
}
