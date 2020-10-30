package hu.bme.playlisthelper.Playlist;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import hu.bme.playlisthelper.FriendList.FriendItem;
import hu.bme.playlisthelper.FriendList.FriendItemDao;

@Database(
        entities = {PlaylistItem.class},
        version = 1
)

public abstract class PlaylistDatabase extends RoomDatabase {
    public abstract PlaylistItemDao playlistItemDao();
}