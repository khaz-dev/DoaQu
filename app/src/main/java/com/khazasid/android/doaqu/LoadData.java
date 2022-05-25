package com.khazasid.android.doaqu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;
import com.khazasid.android.doaqu.Database.Doa;
import com.khazasid.android.doaqu.Database.DoaSupport;
import com.khazasid.android.doaqu.Database.DoasDatabase;
import com.khazasid.android.doaqu.Tools.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class LoadData extends AppCompatActivity {

    private  static LoadData instance;
    private LoadDoaAsyncTask asyncTask;

    public LoadData(){
        super();
        instance = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        asyncTask = new LoadDoaAsyncTask();
        asyncTask.setListener(() -> {
            Intent startApp = new Intent(LoadData.this, MainActivity.class);
            startActivity(startApp);
            finish();
        });

        // Check first launch
        if(Tools.getDoaQuPrefs(getApplicationContext()).getBoolean(getString(R.string.first_time_pref), true)){
            SharedPreferences.Editor editor = Tools.getDoaQuPrefs(getApplicationContext()).edit();
            Date currentDate = new Date();

            editor.putLong(getString(R.string.acces_time_preferences), currentDate.getTime());

            FirebaseMessaging. getInstance().subscribeToTopic("doaqu");

            startLoadingDoa();

            editor.putBoolean(getString(R.string.first_time_pref), false).apply();
        } else {

            Intent startApp = new Intent(LoadData.this, MainActivity.class);
            startActivity(startApp);
            finish();
        }

    }

    private void startLoadingDoa(){

        asyncTask.execute();

    }

    private static class LoadDoaAsyncTask extends AsyncTask<Void, Void, Void >{
        private LoadDoaAsyncTaskListener listener;

        @Override
        protected Void doInBackground(Void... voids) {

            loadDoa();
            DoasDatabase.getInstance(getInstance());

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            if (listener != null) {
                listener.onLoadDoaAsyncTaskFinished();
            }
        }

        void setListener(LoadDoaAsyncTaskListener listener) {
            this.listener = listener;
        }

        public interface LoadDoaAsyncTaskListener {
            void onLoadDoaAsyncTaskFinished();
        }

        private void loadDoa(){
            try{
                loadWords();
            } catch(IOException e){
                throw new RuntimeException(e);
            }
        }

        private void loadWords() throws IOException {

            final Resources resources = getInstance().getResources();

            InputStream inputStreamArabic = resources.openRawResource(R.raw.doas_arabic);
            InputStream inputStreamDoa = resources.openRawResource(R.raw.doas);
            InputStream inputStreamLatin = resources.openRawResource(R.raw.doas_latin);

            BufferedReader readerArabic = new BufferedReader(new InputStreamReader(inputStreamArabic));
            BufferedReader readerDoa = new BufferedReader(new InputStreamReader(inputStreamDoa));
            BufferedReader readerLatin = new BufferedReader(new InputStreamReader(inputStreamLatin));

            short rowId = 1;
            String lineArabic;
            String lineDoa;
            String lineLatin;
            while( ((lineArabic = readerArabic.readLine()) != null) &&
                    ((lineDoa = readerDoa.readLine()) != null) &&
                    ((lineLatin = readerLatin.readLine()) != null)){

                String[] stringsDoa = lineDoa.split("\\|");
                if(stringsDoa.length < 4 && lineArabic.length()<1 &&
                        lineLatin.length()<1) continue;

                Doa doaList = new Doa(stringsDoa[0].trim(),stringsDoa[1].trim(),
                        stringsDoa[2].trim(), stringsDoa[3].trim());

                DoasDatabase.DOAS.add(doaList);

                DoasDatabase.DOA_SUPPORTS.add(new DoaSupport(rowId, lineArabic.trim(), lineLatin.trim(), (short)0));

                rowId++;
            }
            readerArabic.close();
            readerDoa.close();
            readerLatin.close();
        }
    }

    static LoadData getInstance(){
        return instance;
    }

    @Override
    protected void onDestroy() {
        asyncTask.setListener(null);
        super.onDestroy();
    }
}
