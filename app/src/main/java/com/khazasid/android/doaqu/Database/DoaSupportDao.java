package com.khazasid.android.doaqu.Database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public interface DoaSupportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(List<DoaSupport> doaSupports);

    @Query("UPDATE DoaSupport SET bookmark = :bookmark WHERE doaId = :rowId")
    void updateBookmarkByRowId(short rowId, short bookmark);
}
