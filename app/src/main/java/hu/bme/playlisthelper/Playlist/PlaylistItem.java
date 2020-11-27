package hu.bme.playlisthelper.Playlist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

@Entity(tableName = "playlistitem")
public class PlaylistItem {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "matches")
    public int matches;

    @ColumnInfo(name = "trackid")
    public String trackID;

    @ColumnInfo(name = "artistname")
    public String artistName;

    @ColumnInfo(name = "trackname")
    public String trackname;


}

