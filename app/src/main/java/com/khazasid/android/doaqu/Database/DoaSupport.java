package com.khazasid.android.doaqu.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DoaSupport {
    @PrimaryKey
    short doaId;
    String arabic;
    String latin;
    short bookmark;

    public DoaSupport(short doaId, String arabic, String latin, short bookmark){
        this.doaId = doaId;
        this.arabic = arabic;
        this.latin = latin;
        this.bookmark = bookmark;
    }

}
