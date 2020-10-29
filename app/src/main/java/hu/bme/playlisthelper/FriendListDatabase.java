package hu.bme.playlisthelper;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {FriendItem.class},
        version = 1
)
@TypeConverters(value = {FriendItem.Category.class})
public abstract class FriendListDatabase extends RoomDatabase {
    public abstract FriendItemDao friendItemDao();
}