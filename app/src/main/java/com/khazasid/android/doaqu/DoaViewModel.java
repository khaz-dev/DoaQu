package com.khazasid.android.doaqu;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.khazasid.android.doaqu.Database.DoaSupportDao;
import com.khazasid.android.doaqu.Database.DoaSupportJoin;
import com.khazasid.android.doaqu.Database.DoaSupportJoinDao;
import com.khazasid.android.doaqu.Database.DoasDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DoaViewModel extends AndroidViewModel {

    private final DoaSupportJoinDao doaSupportJoinDao;
    private final DoaSupportDao doaSupportDao;
    private final ExecutorService executorService;

    public DoaViewModel(@NonNull Application application) {
        super(application);

        doaSupportJoinDao = DoasDatabase.getInstance(application).doaSupportJoinDao();
        doaSupportDao = DoasDatabase.getInstance(application).doaSupportDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    LiveData<List<DoaSupportJoin>> getAllDoas(){
        return doaSupportJoinDao.getAllDoaList();
    }

    LiveData<List<DoaSupportJoin>> getAllBookmarkDoas(){
        return doaSupportJoinDao.getAllBookmarkDoaList();
    }

    LiveData<List<DoaSupportJoin>> getSearchDoas(String query){
        return doaSupportJoinDao.getSearchDoaList(query);
    }

    DoaSupportJoin getAllDoaDetail(short rowId){
        return doaSupportJoinDao.getAllDoaDetail(rowId);
    }

    void updateDoaSupportBookmark(short rowId, short bookmark){
        executorService.execute(() -> doaSupportDao.updateBookmarkByRowId(rowId, bookmark));
    }
}
