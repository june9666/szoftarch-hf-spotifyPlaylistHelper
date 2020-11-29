package hu.bme.playlisthelper.api.Connectors;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.bme.playlisthelper.Playlist.PlaylistItem;
import hu.bme.playlisthelper.api.Artist;
import hu.bme.playlisthelper.api.Song;

public class SongService {
    private ArrayList<Song> songs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String names;

    public SongService(Context context,String name) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
        names = name;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public ArrayList<Song> getRecentlyPlayedTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/playlists/"+ names +"/tracks";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {Gson gson = new Gson();
                        JSONArray jsonArray = response.optJSONArray("items");
                        for (int n = 0; n < jsonArray.length(); n++) {

                            Artist artist=new Artist();
                            try {
                                JSONObject objecta = jsonArray.getJSONObject(n);
                                objecta = objecta.optJSONObject("track");
                                JSONArray obja = objecta.optJSONArray("artists");
                                for (int i = 0; i < obja.length(); i++) {
                                    JSONObject object = obja.getJSONObject(i);
                                    artist = gson.fromJson(object.toString(), Artist.class);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONObject object = jsonArray.getJSONObject(n);
                                object = object.optJSONObject("track");
                                Song song = gson.fromJson(object.toString(), Song.class);
                                song.setPop();
                                song.setArtist(artist.getName());
                                songs.add(song);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            if (!response.getString("next").equals("null")){
                                getNext(response.getString("next"),callBack);
                            }else
                            {
                                callBack.onSuccess();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization: ", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }

    public void getNext(String Endpoint,final VolleyCallBack callBack) {
        String endpoint = Endpoint;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {Gson gson = new Gson();
                        JSONArray jsonArray = response.optJSONArray("items");
                        for (int n = 0; n < jsonArray.length(); n++) {

                            Artist artist=new Artist();
                            try {
                                JSONObject objecta = jsonArray.getJSONObject(n);
                                objecta = objecta.optJSONObject("track");
                                JSONArray obja = objecta.optJSONArray("artists");
                                for (int i = 0; i < obja.length(); i++) {
                                    JSONObject object = obja.getJSONObject(i);
                                    artist = gson.fromJson(object.toString(), Artist.class);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONObject object = jsonArray.getJSONObject(n);
                                object = object.optJSONObject("track");
                                Song song = gson.fromJson(object.toString(), Song.class);
                                song.setPop();
                                song.setArtist(artist.getName());
                                songs.add(song);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        try {
                            if (!response.getString("next").equals("null")) {
                                getNext(response.getString("next"), callBack);
                            }else {
                                callBack.onSuccess();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization: ", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }



    public void addSongToLibrary(List<PlaylistItem> songs) {
        JsonObjectRequest jsonObjectRequest = prepareSongLibraryRequest(songs);
        queue.add(jsonObjectRequest);
    }

    private JsonObjectRequest prepareSongLibraryRequest(List<PlaylistItem> payload) {
        String temp ="";
        for (int i=0;i<payload.size();i++){
            if (i==payload.size()-1){
                temp=temp+payload.get(i).trackID;
            }else{
                temp=temp+payload.get(i).trackID+",";
            }

        }
        return new JsonObjectRequest(Request.Method.POST, "https://api.spotify.com/v1/playlists/"+ names+"/tracks?uris="+temp, null, response -> {
            try {
                response.get("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            int err = error.networkResponse.statusCode;
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

    private JSONObject preparePutPayload(ArrayList<Song> song) {
        JSONArray idarray = new JSONArray();
     //   idarray.put(song.getId());
        JSONObject ids = new JSONObject();
        try {
            ids.put("ids", idarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }
}