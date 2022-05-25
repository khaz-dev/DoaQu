package com.khazasid.android.doaqu.Database;

import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DoaDao_Impl implements DoaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Doa> __insertionAdapterOfDoa;

  public DoaDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDoa = new EntityInsertionAdapter<Doa>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Doa` (`rowid`,`title`,`category`,`translate`,`footnote`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Doa value) {
        stmt.bindLong(1, value.rowId);
        if (value.title == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.title);
        }
        if (value.category == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.category);
        }
        if (value.translate == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.translate);
        }
        if (value.footnote == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.footnote);
        }
      }
    };
  }

  @Override
  public void saveAll(final List<Doa> doas) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDoa.insert(doas);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
