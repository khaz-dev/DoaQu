package com.khazasid.android.doaqu.Database;

import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DoaSupportDao_Impl implements DoaSupportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DoaSupport> __insertionAdapterOfDoaSupport;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBookmarkByRowId;

  public DoaSupportDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDoaSupport = new EntityInsertionAdapter<DoaSupport>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `DoaSupport` (`doaId`,`arabic`,`latin`,`bookmark`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DoaSupport value) {
        stmt.bindLong(1, value.doaId);
        if (value.arabic == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.arabic);
        }
        if (value.latin == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.latin);
        }
        stmt.bindLong(4, value.bookmark);
      }
    };
    this.__preparedStmtOfUpdateBookmarkByRowId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE DoaSupport SET bookmark = ? WHERE doaId = ?";
        return _query;
      }
    };
  }

  @Override
  public void saveAll(final List<DoaSupport> doaSupports) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDoaSupport.insert(doaSupports);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateBookmarkByRowId(final short rowId, final short bookmark) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBookmarkByRowId.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, bookmark);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, rowId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateBookmarkByRowId.release(_stmt);
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
