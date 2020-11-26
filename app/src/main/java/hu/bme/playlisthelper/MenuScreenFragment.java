package hu.bme.playlisthelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import hu.bme.playlisthelper.FriendList.FriendListActivity;
import hu.bme.playlisthelper.Playlist.PlaylistActivity;
import hu.bme.playlisthelper.Playlist.PlaylistDatabase;

public class MenuScreenFragment extends Fragment {
    public PlaylistDatabase database;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_screen, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                PlaylistDatabase.class,
                "play-list"
        ).build();

        view.findViewById(R.id.button_create_new_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clear();
                Intent myIntent = new Intent(getActivity(), PlaylistActivity.class);
                getActivity().startActivity(myIntent);

            }
        });

        view.findViewById(R.id.button_add_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), FriendListActivity.class);

                getActivity().startActivity(myIntent);

              //  NavHostFragment.findNavController(MenuScreenFragment.this)
              //          .navigate(R.id.action_MenuScreen_to_FriendList);
            }
        });

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MenuScreenFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    public void clear() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.playlistItemDao().nukeTable();
                return true;
            }
            @Override
            protected void onPostExecute(Boolean result){

            }

        }.execute();
    }
}