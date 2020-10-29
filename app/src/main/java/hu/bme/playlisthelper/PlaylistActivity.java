package hu.bme.playlisthelper;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

public class PlaylistActivity extends AppCompatActivity {

    RadioGroup includeGroup;
    RadioButton groupFamily;
    RadioButton groupFriends;
    RadioButton groupCustom;
    RadioButton intersectAll;
    RadioButton intersectCustom;
    Button createList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        includeGroup = findViewById(R.id.radioGroup);
        groupFamily = findViewById(R.id.radio_family);
        groupFriends = findViewById(R.id.radio_friends);
        groupCustom= findViewById(R.id.radio_custom);
        intersectAll= findViewById(R.id.radio_intersect_all);
        intersectCustom= findViewById(R.id.radio_intersect_custom);
        createList = findViewById(R.id.button_createList);


        groupCustom.setOnClickListener(view ->
                Toast.makeText(view.getContext(),"inflate emberpicker",  Toast.LENGTH_LONG).show());

        intersectCustom.setOnClickListener(view ->
                Toast.makeText(view.getContext(),"inflate numberpicker",  Toast.LENGTH_LONG).show());

        createList.setOnClickListener(view ->
                Toast.makeText(view.getContext(),"create list",  Toast.LENGTH_LONG).show());
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
}
