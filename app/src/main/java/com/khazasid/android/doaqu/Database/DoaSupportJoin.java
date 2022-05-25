package com.khazasid.android.doaqu.Database;

import androidx.room.Embedded;

public class DoaSupportJoin {
    @Embedded
    Doa doa;
    @Embedded
    DoaSupport doaSupport;

    public short getRowId(){
        return this.doaSupport.doaId;
    }

    public String getTitle(){
        return this.doa.title;
    }

    public String getTranslate(){
        return this.doa.translate;
    }

    public short getBookmark(){
        return this.doaSupport.bookmark;
    }

    public String getArabic(){
        return this.doaSupport.arabic;
    }

    public String getLatin(){
        return this.doaSupport.latin;
    }

    public String getFootnote(){
        return this.doa.footnote;
    }

}
