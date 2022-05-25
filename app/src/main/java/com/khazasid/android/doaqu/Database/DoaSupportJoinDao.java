package com.khazasid.android.doaqu.Database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RoomWarnings;

@Dao
public interface DoaSupportJoinDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query(" SELECT * FROM Doa INNER JOIN" +
            " DoaSupport ON Doa.`rowid` = DoaSupport.doaId")
    LiveData<List<DoaSupportJoin>> getAllDoaList();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query(" SELECT * FROM Doa INNER JOIN" +
            " DoaSupport ON Doa.`rowid` = DoaSupport.doaId WHERE DoaSupport.bookmark = 1")
    LiveData<List<DoaSupportJoin>> getAllBookmarkDoaList();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM Doa INNER JOIN DoaSupport ON" +
            " DoaSupport.doaId = Doa.`rowid` WHERE doa.`rowid` = :rowId")
    DoaSupportJoin getAllDoaDetail(int rowId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM Doa INNER JOIN DoaSupport ON DoaSupport.doaId = Doa.`rowid` WHERE" +
            " Doa MATCH :query")
    LiveData<List<DoaSupportJoin>> getSearchDoaList(String query);
}
