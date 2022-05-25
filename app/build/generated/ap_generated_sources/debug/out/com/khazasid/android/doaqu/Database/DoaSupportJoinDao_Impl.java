package com.khazasid.android.doaqu.Database;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DoaSupportJoinDao_Impl implements DoaSupportJoinDao {
  private final RoomDatabase __db;

  public DoaSupportJoinDao_Impl(RoomDatabase __db) {
    this.__db = __db;
  }

  @Override
  public LiveData<List<DoaSupportJoin>> getAllDoaList() {
    final String _sql = " SELECT * FROM Doa INNER JOIN DoaSupport ON Doa.`rowid` = DoaSupport.doaId";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"Doa","DoaSupport"}, false, new Callable<List<DoaSupportJoin>>() {
      @Override
      public List<DoaSupportJoin> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfTranslate = CursorUtil.getColumnIndexOrThrow(_cursor, "translate");
          final int _cursorIndexOfFootnote = CursorUtil.getColumnIndexOrThrow(_cursor, "footnote");
          final int _cursorIndexOfDoaId = CursorUtil.getColumnIndexOrThrow(_cursor, "doaId");
          final int _cursorIndexOfArabic = CursorUtil.getColumnIndexOrThrow(_cursor, "arabic");
          final int _cursorIndexOfLatin = CursorUtil.getColumnIndexOrThrow(_cursor, "latin");
          final int _cursorIndexOfBookmark = CursorUtil.getColumnIndexOrThrow(_cursor, "bookmark");
          final List<DoaSupportJoin> _result = new ArrayList<DoaSupportJoin>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DoaSupportJoin _item;
            final Doa _tmpDoa;
            if (! (_cursor.isNull(_cursorIndexOfTitle) && _cursor.isNull(_cursorIndexOfCategory) && _cursor.isNull(_cursorIndexOfTranslate) && _cursor.isNull(_cursorIndexOfFootnote))) {
              _tmpDoa = new Doa();
              if (_cursor.isNull(_cursorIndexOfTitle)) {
                _tmpDoa.title = null;
              } else {
                _tmpDoa.title = _cursor.getString(_cursorIndexOfTitle);
              }
              if (_cursor.isNull(_cursorIndexOfCategory)) {
                _tmpDoa.category = null;
              } else {
                _tmpDoa.category = _cursor.getString(_cursorIndexOfCategory);
              }
              if (_cursor.isNull(_cursorIndexOfTranslate)) {
                _tmpDoa.translate = null;
              } else {
                _tmpDoa.translate = _cursor.getString(_cursorIndexOfTranslate);
              }
              if (_cursor.isNull(_cursorIndexOfFootnote)) {
                _tmpDoa.footnote = null;
              } else {
                _tmpDoa.footnote = _cursor.getString(_cursorIndexOfFootnote);
              }
            }  else  {
              _tmpDoa = null;
            }
            final DoaSupport _tmpDoaSupport;
            if (! (_cursor.isNull(_cursorIndexOfDoaId) && _cursor.isNull(_cursorIndexOfArabic) && _cursor.isNull(_cursorIndexOfLatin) && _cursor.isNull(_cursorIndexOfBookmark))) {
              final short _tmpDoaId;
              _tmpDoaId = _cursor.getShort(_cursorIndexOfDoaId);
              final String _tmpArabic;
              if (_cursor.isNull(_cursorIndexOfArabic)) {
                _tmpArabic = null;
              } else {
                _tmpArabic = _cursor.getString(_cursorIndexOfArabic);
              }
              final String _tmpLatin;
              if (_cursor.isNull(_cursorIndexOfLatin)) {
                _tmpLatin = null;
              } else {
                _tmpLatin = _cursor.getString(_cursorIndexOfLatin);
              }
              final short _tmpBookmark;
              _tmpBookmark = _cursor.getShort(_cursorIndexOfBookmark);
              _tmpDoaSupport = new DoaSupport(_tmpDoaId,_tmpArabic,_tmpLatin,_tmpBookmark);
            }  else  {
              _tmpDoaSupport = null;
            }
            _item = new DoaSupportJoin();
            _item.doa = _tmpDoa;
            _item.doaSupport = _tmpDoaSupport;
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<DoaSupportJoin>> getAllBookmarkDoaList() {
    final String _sql = " SELECT * FROM Doa INNER JOIN DoaSupport ON Doa.`rowid` = DoaSupport.doaId WHERE DoaSupport.bookmark = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"Doa","DoaSupport"}, false, new Callable<List<DoaSupportJoin>>() {
      @Override
      public List<DoaSupportJoin> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfTranslate = CursorUtil.getColumnIndexOrThrow(_cursor, "translate");
          final int _cursorIndexOfFootnote = CursorUtil.getColumnIndexOrThrow(_cursor, "footnote");
          final int _cursorIndexOfDoaId = CursorUtil.getColumnIndexOrThrow(_cursor, "doaId");
          final int _cursorIndexOfArabic = CursorUtil.getColumnIndexOrThrow(_cursor, "arabic");
          final int _cursorIndexOfLatin = CursorUtil.getColumnIndexOrThrow(_cursor, "latin");
          final int _cursorIndexOfBookmark = CursorUtil.getColumnIndexOrThrow(_cursor, "bookmark");
          final List<DoaSupportJoin> _result = new ArrayList<DoaSupportJoin>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DoaSupportJoin _item;
            final Doa _tmpDoa;
            if (! (_cursor.isNull(_cursorIndexOfTitle) && _cursor.isNull(_cursorIndexOfCategory) && _cursor.isNull(_cursorIndexOfTranslate) && _cursor.isNull(_cursorIndexOfFootnote))) {
              _tmpDoa = new Doa();
              if (_cursor.isNull(_cursorIndexOfTitle)) {
                _tmpDoa.title = null;
              } else {
                _tmpDoa.title = _cursor.getString(_cursorIndexOfTitle);
              }
              if (_cursor.isNull(_cursorIndexOfCategory)) {
                _tmpDoa.category = null;
              } else {
                _tmpDoa.category = _cursor.getString(_cursorIndexOfCategory);
              }
              if (_cursor.isNull(_cursorIndexOfTranslate)) {
                _tmpDoa.translate = null;
              } else {
                _tmpDoa.translate = _cursor.getString(_cursorIndexOfTranslate);
              }
              if (_cursor.isNull(_cursorIndexOfFootnote)) {
                _tmpDoa.footnote = null;
              } else {
                _tmpDoa.footnote = _cursor.getString(_cursorIndexOfFootnote);
              }
            }  else  {
              _tmpDoa = null;
            }
            final DoaSupport _tmpDoaSupport;
            if (! (_cursor.isNull(_cursorIndexOfDoaId) && _cursor.isNull(_cursorIndexOfArabic) && _cursor.isNull(_cursorIndexOfLatin) && _cursor.isNull(_cursorIndexOfBookmark))) {
              final short _tmpDoaId;
              _tmpDoaId = _cursor.getShort(_cursorIndexOfDoaId);
              final String _tmpArabic;
              if (_cursor.isNull(_cursorIndexOfArabic)) {
                _tmpArabic = null;
              } else {
                _tmpArabic = _cursor.getString(_cursorIndexOfArabic);
              }
              final String _tmpLatin;
              if (_cursor.isNull(_cursorIndexOfLatin)) {
                _tmpLatin = null;
              } else {
                _tmpLatin = _cursor.getString(_cursorIndexOfLatin);
              }
              final short _tmpBookmark;
              _tmpBookmark = _cursor.getShort(_cursorIndexOfBookmark);
              _tmpDoaSupport = new DoaSupport(_tmpDoaId,_tmpArabic,_tmpLatin,_tmpBookmark);
            }  else  {
              _tmpDoaSupport = null;
            }
            _item = new DoaSupportJoin();
            _item.doa = _tmpDoa;
            _item.doaSupport = _tmpDoaSupport;
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public DoaSupportJoin getAllDoaDetail(final int rowId) {
    final String _sql = "SELECT * FROM Doa INNER JOIN DoaSupport ON DoaSupport.doaId = Doa.`rowid` WHERE doa.`rowid` = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rowId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfTranslate = CursorUtil.getColumnIndexOrThrow(_cursor, "translate");
      final int _cursorIndexOfFootnote = CursorUtil.getColumnIndexOrThrow(_cursor, "footnote");
      final int _cursorIndexOfDoaId = CursorUtil.getColumnIndexOrThrow(_cursor, "doaId");
      final int _cursorIndexOfArabic = CursorUtil.getColumnIndexOrThrow(_cursor, "arabic");
      final int _cursorIndexOfLatin = CursorUtil.getColumnIndexOrThrow(_cursor, "latin");
      final int _cursorIndexOfBookmark = CursorUtil.getColumnIndexOrThrow(_cursor, "bookmark");
      final DoaSupportJoin _result;
      if(_cursor.moveToFirst()) {
        final Doa _tmpDoa;
        if (! (_cursor.isNull(_cursorIndexOfTitle) && _cursor.isNull(_cursorIndexOfCategory) && _cursor.isNull(_cursorIndexOfTranslate) && _cursor.isNull(_cursorIndexOfFootnote))) {
          _tmpDoa = new Doa();
          if (_cursor.isNull(_cursorIndexOfTitle)) {
            _tmpDoa.title = null;
          } else {
            _tmpDoa.title = _cursor.getString(_cursorIndexOfTitle);
          }
          if (_cursor.isNull(_cursorIndexOfCategory)) {
            _tmpDoa.category = null;
          } else {
            _tmpDoa.category = _cursor.getString(_cursorIndexOfCategory);
          }
          if (_cursor.isNull(_cursorIndexOfTranslate)) {
            _tmpDoa.translate = null;
          } else {
            _tmpDoa.translate = _cursor.getString(_cursorIndexOfTranslate);
          }
          if (_cursor.isNull(_cursorIndexOfFootnote)) {
            _tmpDoa.footnote = null;
          } else {
            _tmpDoa.footnote = _cursor.getString(_cursorIndexOfFootnote);
          }
        }  else  {
          _tmpDoa = null;
        }
        final DoaSupport _tmpDoaSupport;
        if (! (_cursor.isNull(_cursorIndexOfDoaId) && _cursor.isNull(_cursorIndexOfArabic) && _cursor.isNull(_cursorIndexOfLatin) && _cursor.isNull(_cursorIndexOfBookmark))) {
          final short _tmpDoaId;
          _tmpDoaId = _cursor.getShort(_cursorIndexOfDoaId);
          final String _tmpArabic;
          if (_cursor.isNull(_cursorIndexOfArabic)) {
            _tmpArabic = null;
          } else {
            _tmpArabic = _cursor.getString(_cursorIndexOfArabic);
          }
          final String _tmpLatin;
          if (_cursor.isNull(_cursorIndexOfLatin)) {
            _tmpLatin = null;
          } else {
            _tmpLatin = _cursor.getString(_cursorIndexOfLatin);
          }
          final short _tmpBookmark;
          _tmpBookmark = _cursor.getShort(_cursorIndexOfBookmark);
          _tmpDoaSupport = new DoaSupport(_tmpDoaId,_tmpArabic,_tmpLatin,_tmpBookmark);
        }  else  {
          _tmpDoaSupport = null;
        }
        _result = new DoaSupportJoin();
        _result.doa = _tmpDoa;
        _result.doaSupport = _tmpDoaSupport;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<DoaSupportJoin>> getSearchDoaList(final String query) {
    final String _sql = "SELECT * FROM Doa INNER JOIN DoaSupport ON DoaSupport.doaId = Doa.`rowid` WHERE Doa MATCH ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"Doa","DoaSupport"}, false, new Callable<List<DoaSupportJoin>>() {
      @Override
      public List<DoaSupportJoin> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfTranslate = CursorUtil.getColumnIndexOrThrow(_cursor, "translate");
          final int _cursorIndexOfFootnote = CursorUtil.getColumnIndexOrThrow(_cursor, "footnote");
          final int _cursorIndexOfDoaId = CursorUtil.getColumnIndexOrThrow(_cursor, "doaId");
          final int _cursorIndexOfArabic = CursorUtil.getColumnIndexOrThrow(_cursor, "arabic");
          final int _cursorIndexOfLatin = CursorUtil.getColumnIndexOrThrow(_cursor, "latin");
          final int _cursorIndexOfBookmark = CursorUtil.getColumnIndexOrThrow(_cursor, "bookmark");
          final List<DoaSupportJoin> _result = new ArrayList<DoaSupportJoin>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DoaSupportJoin _item;
            final Doa _tmpDoa;
            if (! (_cursor.isNull(_cursorIndexOfTitle) && _cursor.isNull(_cursorIndexOfCategory) && _cursor.isNull(_cursorIndexOfTranslate) && _cursor.isNull(_cursorIndexOfFootnote))) {
              _tmpDoa = new Doa();
              if (_cursor.isNull(_cursorIndexOfTitle)) {
                _tmpDoa.title = null;
              } else {
                _tmpDoa.title = _cursor.getString(_cursorIndexOfTitle);
              }
              if (_cursor.isNull(_cursorIndexOfCategory)) {
                _tmpDoa.category = null;
              } else {
                _tmpDoa.category = _cursor.getString(_cursorIndexOfCategory);
              }
              if (_cursor.isNull(_cursorIndexOfTranslate)) {
                _tmpDoa.translate = null;
              } else {
                _tmpDoa.translate = _cursor.getString(_cursorIndexOfTranslate);
              }
              if (_cursor.isNull(_cursorIndexOfFootnote)) {
                _tmpDoa.footnote = null;
              } else {
                _tmpDoa.footnote = _cursor.getString(_cursorIndexOfFootnote);
              }
            }  else  {
              _tmpDoa = null;
            }
            final DoaSupport _tmpDoaSupport;
            if (! (_cursor.isNull(_cursorIndexOfDoaId) && _cursor.isNull(_cursorIndexOfArabic) && _cursor.isNull(_cursorIndexOfLatin) && _cursor.isNull(_cursorIndexOfBookmark))) {
              final short _tmpDoaId;
              _tmpDoaId = _cursor.getShort(_cursorIndexOfDoaId);
              final String _tmpArabic;
              if (_cursor.isNull(_cursorIndexOfArabic)) {
                _tmpArabic = null;
              } else {
                _tmpArabic = _cursor.getString(_cursorIndexOfArabic);
              }
              final String _tmpLatin;
              if (_cursor.isNull(_cursorIndexOfLatin)) {
                _tmpLatin = null;
              } else {
                _tmpLatin = _cursor.getString(_cursorIndexOfLatin);
              }
              final short _tmpBookmark;
              _tmpBookmark = _cursor.getShort(_cursorIndexOfBookmark);
              _tmpDoaSupport = new DoaSupport(_tmpDoaId,_tmpArabic,_tmpLatin,_tmpBookmark);
            }  else  {
              _tmpDoaSupport = null;
            }
            _item = new DoaSupportJoin();
            _item.doa = _tmpDoa;
            _item.doaSupport = _tmpDoaSupport;
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
