package hu.bme.playlisthelper.api.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hu.bme.playlisthelper.api.Song;
import hu.bme.playlisthelper.api.User;

public class PlaylistService {
    private static final String ENDPOINT = "https://api.spotify.com/v1/me";
    private static SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private User user;
    private SharedPreferences.Editor editor;

    public PlaylistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
        editor = context.getSharedPreferences("SPOTIFY", 0).edit();
    }

    public void addSongToLibrary(Song song) {
        JSONObject payload = preparePutPayload(song);
        JsonObjectRequest jsonObjectRequest = prepareSongLibraryRequest(payload);
        queue.add(jsonObjectRequest);
    }

    private JsonObjectRequest prepareSongLibraryRequest(JSONObject payload) {
        return new JsonObjectRequest(Request.Method.POST, "https://api.spotify.com/v1/users/"+ sharedPreferences.getString("userid","") +"/playlists", payload, response -> {
            try {
                editor.putString("playlistid", response.getString("id"));
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
    }

    private JSONObject preparePutPayload(Song song) {

        JSONObject ids = new JSONObject();
        if (sharedPreferences.getString("playlistname","").equals("")){
            try {
                ids.put("name","party list");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                ids.put("name",sharedPreferences.getString("playlistname",""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return ids;
    }



}
