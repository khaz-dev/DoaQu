package com.khazasid.android.doaqu.Database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.FtsTableInfo;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DoasDatabase_Impl extends DoasDatabase {
  private volatile DoaDao _doaDao;

  private volatile DoaSupportDao _doaSupportDao;

  private volatile DoaSupportJoinDao _doaSupportJoinDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `Doa` USING FTS3(`title` TEXT, `category` TEXT, `translate` TEXT, `footnote` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `DoaSupport` (`doaId` INTEGER NOT NULL, `arabic` TEXT, `latin` TEXT, `bookmark` INTEGER NOT NULL, PRIMARY KEY(`doaId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '88df49aae46272fde1a5c3ece231ff06')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Doa`");
        _db.execSQL("DROP TABLE IF EXISTS `DoaSupport`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashSet<String> _columnsDoa = new HashSet<String>(5);
        _columnsDoa.add("title");
        _columnsDoa.add("category");
        _columnsDoa.add("translate");
        _columnsDoa.add("footnote");
        final FtsTableInfo _infoDoa = new FtsTableInfo("Doa", _columnsDoa, "CREATE VIRTUAL TABLE IF NOT EXISTS `Doa` USING FTS3(`title` TEXT, `category` TEXT, `translate` TEXT, `footnote` TEXT)");
        final FtsTableInfo _existingDoa = FtsTableInfo.read(_db, "Doa");
        if (!_infoDoa.equals(_existingDoa)) {
          return new RoomOpenHelper.ValidationResult(false, "Doa(com.khazasid.android.doaqu.Database.Doa).\n"
                  + " Expected:\n" + _infoDoa + "\n"
                  + " Found:\n" + _existingDoa);
        }
        final HashMap<String, TableInfo.Column> _columnsDoaSupport = new HashMap<String, TableInfo.Column>(4);
        _columnsDoaSupport.put("doaId", new TableInfo.Column("doaId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoaSupport.put("arabic", new TableInfo.Column("arabic", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoaSupport.put("latin", new TableInfo.Column("latin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoaSupport.put("bookmark", new TableInfo.Column("bookmark", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDoaSupport = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDoaSupport = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDoaSupport = new TableInfo("DoaSupport", _columnsDoaSupport, _foreignKeysDoaSupport, _indicesDoaSupport);
        final TableInfo _existingDoaSupport = TableInfo.read(_db, "DoaSupport");
        if (! _infoDoaSupport.equals(_existingDoaSupport)) {
          return new RoomOpenHelper.ValidationResult(false, "DoaSupport(com.khazasid.android.doaqu.Database.DoaSupport).\n"
                  + " Expected:\n" + _infoDoaSupport + "\n"
                  + " Found:\n" + _existingDoaSupport);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "88df49aae46272fde1a5c3ece231ff06", "e35ee0f779d10fc9394115ef10521b59");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(1);
    _shadowTablesMap.put("Doa", "Doa_content");
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Doa","DoaSupport");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Doa`");
      _db.execSQL("DELETE FROM `DoaSupport`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DoaDao.class, DoaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DoaSupportDao.class, DoaSupportDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DoaSupportJoinDao.class, DoaSupportJoinDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public DoaDao doaDao() {
    if (_doaDao != null) {
      return _doaDao;
    } else {
      synchronized(this) {
        if(_doaDao == null) {
          _doaDao = new DoaDao_Impl(this);
        }
        return _doaDao;
      }
    }
  }

  @Override
  public DoaSupportDao doaSupportDao() {
    if (_doaSupportDao != null) {
      return _doaSupportDao;
    } else {
      synchronized(this) {
        if(_doaSupportDao == null) {
          _doaSupportDao = new DoaSupportDao_Impl(this);
        }
        return _doaSupportDao;
      }
    }
  }

  @Override
  public DoaSupportJoinDao doaSupportJoinDao() {
    if (_doaSupportJoinDao != null) {
      return _doaSupportJoinDao;
    } else {
      synchronized(this) {
        if(_doaSupportJoinDao == null) {
          _doaSupportJoinDao = new DoaSupportJoinDao_Impl(this);
        }
        return _doaSupportJoinDao;
      }
    }
  }
}
