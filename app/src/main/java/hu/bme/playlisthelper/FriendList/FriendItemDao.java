package hu.bme.playlisthelper.FriendList;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FriendItemDao {
    @Query("SELECT * FROM FriendItem")
    List<FriendItem> getAll();

    @Insert
    void insertAll(FriendItem... friendItems);

    @Update
    void update(FriendItem friendItem);

    @Delete
    void deleteItem(FriendItem friendItem);

}