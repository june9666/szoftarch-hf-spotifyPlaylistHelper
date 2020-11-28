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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hu.bme.playlisthelper.api.Artist;
import hu.bme.playlisthelper.api.Playlist;
import hu.bme.playlisthelper.api.Song;
import hu.bme.playlisthelper.api.User;

public class PlaylistService {
    private static final String ENDPOINT = "https://api.spotify.com/v1/me";
    private static SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String user;
    private SharedPreferences.Editor editor;
    private ArrayList<String> playlistIds = new ArrayList<>();

    public ArrayList<String> getPlaylistIds() {
        return playlistIds;
    }

    public PlaylistService(Context context,String u) {
        this.user=u;
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
        editor = context.getSharedPreferences("SPOTIFY", 0).edit();
    }

    public void addSongToLibrary(Song song, VolleyCallBack callBack) {
        JSONObject payload = preparePutPayload(song);
        JsonObjectRequest jsonObjectRequest = prepareSongLibraryRequest(payload,callBack);
        queue.add(jsonObjectRequest);
    }

    private JsonObjectRequest prepareSongLibraryRequest(JSONObject payload,final VolleyCallBack callBack) {
        return new JsonObjectRequest(Request.Method.POST, "https://api.spotify.com/v1/users/"+ sharedPreferences.getString("userid","") +"/playlists", payload, response -> {
            try {
                editor.putString("playlistid", response.getString("id"));
                editor.apply();
                callBack.onSuccess();
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

    public void getUserPlaylists( VolleyCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = playlistIdRequest(callBack);
        queue.add(jsonObjectRequest);
    }

    private JsonObjectRequest playlistIdRequest(final VolleyCallBack callBack) {
        return new JsonObjectRequest(Request.Method.GET, "https://api.spotify.com/v1/users/"+user+"/playlists", null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("items");
            for (int n = 0; n < jsonArray.length(); n++) {

                try {
                    JSONObject object = jsonArray.getJSONObject(n);
                    Playlist play = gson.fromJson(object.toString(), Playlist.class);
                    playlistIds.add(play.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            callBack.onSuccess();


    }, error -> {
            int asd =error.networkResponse.statusCode;
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


}
