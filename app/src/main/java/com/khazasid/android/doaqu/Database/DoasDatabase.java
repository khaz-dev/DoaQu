package com.khazasid.android.doaqu.Database;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.khazasid.android.doaqu.R;

@Database(entities = {Doa.class, DoaSupport.class}, version = 1, exportSchema = false)
public abstract class DoasDatabase extends RoomDatabase {

    private static DoasDatabase INSTANCE;

    public static List<Doa> DOAS = new ArrayList<>();
    public static List<DoaSupport> DOA_SUPPORTS = new ArrayList<>();

    public abstract DoaDao doaDao();
    public abstract DoaSupportDao doaSupportDao();
    public abstract DoaSupportJoinDao doaSupportJoinDao();

    private static final Object sLock = new Object();

    public static DoasDatabase getInstance(Context context) {

        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        DoasDatabase.class, context.getString(R.string.database_name))
                        .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);

                                Executors.newSingleThreadExecutor().execute(
                                        () -> getInstance(context).doaDao().saveAll(DOAS));

                                Executors.newSingleThreadExecutor().execute(
                                        () -> getInstance(context).doaSupportDao()
                                                .saveAll(DOA_SUPPORTS));

                            }
                        })
                        .build();
            }
            return INSTANCE;
        }
    }
}
