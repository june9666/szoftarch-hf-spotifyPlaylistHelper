package hu.bme.playlisthelper.FriendList;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import hu.bme.playlisthelper.FriendList.FriendItem;
import hu.bme.playlisthelper.FriendList.FriendItemDao;

@Database(
        entities = {FriendItem.class},
        version = 1
)
@TypeConverters(value = {FriendItem.Category.class})
public abstract class FriendListDatabase extends RoomDatabase {
    public abstract FriendItemDao friendItemDao();
}