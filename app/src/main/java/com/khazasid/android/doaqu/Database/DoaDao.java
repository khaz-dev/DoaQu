package com.khazasid.android.doaqu.Database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface DoaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(List<Doa> doas);

}
