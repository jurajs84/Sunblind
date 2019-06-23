package com.example.sunblind;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class SunblindRepository {
    private LiveData<List<Sunblind>> sunblindList;
    private SunblindDao sunblindDao;

    public SunblindRepository(Application application) {
        SunblindDatabase db = SunblindDatabase.getDatabase(application);
        this.sunblindDao = db.sunblindDao();
        this.sunblindList = sunblindDao.getAllSunblinds();
    }

    public void insert(Sunblind sunblind){
        new InsertAsyncTask(sunblindDao).execute(sunblind);
    }

    public void update(Sunblind sunblind){
        new UpdateAsyncTask(sunblindDao).execute(sunblind);
    }

    public void delete(Sunblind sunblind){
        new DeleteAsyncTask(sunblindDao).execute(sunblind);
    }

    public LiveData<List<Sunblind>> getSunblindList() {
        return sunblindList;
    }

    private static class InsertAsyncTask extends AsyncTask<Sunblind, Void, Void> {

        private SunblindDao sunblindDao;

        public InsertAsyncTask(SunblindDao sunblindDao) {
            this.sunblindDao = sunblindDao;
        }

        @Override
        protected Void doInBackground(Sunblind... sunblinds) {
            sunblindDao.insert(sunblinds[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Sunblind, Void, Void>{

        private SunblindDao sunblindDao;

        public UpdateAsyncTask(SunblindDao sunblindDao) {
            this.sunblindDao = sunblindDao;
        }

        @Override
        protected Void doInBackground(Sunblind... sunblinds) {
            sunblindDao.update(sunblinds[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Sunblind, Void, Void> {

        private SunblindDao sunblindDao;

        public DeleteAsyncTask (SunblindDao sunblindDao) {
            this.sunblindDao = sunblindDao;
        }

        @Override
        protected Void doInBackground(Sunblind... sunblinds) {
            sunblindDao.delete(sunblinds[0]);
            return null;
        }
    }
}
