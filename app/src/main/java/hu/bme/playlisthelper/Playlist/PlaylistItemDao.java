package hu.bme.playlisthelper.Playlist;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import hu.bme.playlisthelper.FriendList.FriendItem;

@Dao
public interface PlaylistItemDao {
    @Query("SELECT * FROM PlaylistItem")
    List<PlaylistItem> getAll();

    @Insert
    void insertAll(PlaylistItem... playlistItems);

    @Update
    void update(PlaylistItem playlistItem);

    @Delete
    void deleteItem(PlaylistItem playlistItem);

}