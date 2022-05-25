package com.khazasid.android.doaqu.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts3;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
@Fts3
public class Doa {
    @PrimaryKey
    @ColumnInfo( name = "rowid")
    short rowId;
    String title;
    String category;
    String translate;
    String footnote;

    @Ignore
    public Doa(String title, String category, String translate, String footnote){
        this.title = title;
        this.category = category;
        this.translate = translate;
        this.footnote = footnote;
    }

    public Doa(){}

}
