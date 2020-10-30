package hu.bme.playlisthelper.Playlist;

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
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import hu.bme.playlisthelper.R;

public class PlaylistCreationFragment extends Fragment {


    RadioGroup includeGroup;
    RadioButton groupFamily;
    RadioButton groupFriends;
    RadioButton groupSelect;
    RadioButton intersectAll;
    RadioButton intersectCustom;
    Button createList;
    int intersectPickerNumber;



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

            transaction.replace(R.id.fragment_playlist_creation, new PlaylistViewFragment());
            transaction.commit();
        });


    }
}
