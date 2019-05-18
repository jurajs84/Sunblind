package com.example.sunblide;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class SunblideRepository {
    private LiveData<List<Sunblide>> sunblideList;
    private SunblideDao sunblideDao;

    public SunblideRepository(Application application) {
        SunblideDatabase db = SunblideDatabase.getDatabase(application);
        this.sunblideDao = db.sunblideDao();
        this.sunblideList = sunblideDao.getAllSunblides();
    }

    public void insert(Sunblide sunblide){
        new InsertAsyncTask(sunblideDao).execute(sunblide);
    }

    public void update(Sunblide sunblide){
        new UpdateAsyncTask(sunblideDao).execute(sunblide);
    }

    public void delete(Sunblide sunblide){
        new DeleteAsyncTask(sunblideDao).execute(sunblide);
    }

    public LiveData<List<Sunblide>> getSunblideList() {
        return sunblideList;
    }

    private static class InsertAsyncTask extends AsyncTask<Sunblide, Void, Void> {

        private SunblideDao sunblideDao;

        public InsertAsyncTask(SunblideDao sunblideDao) {
            this.sunblideDao = sunblideDao;
        }

        @Override
        protected Void doInBackground(Sunblide... sunblides) {
            sunblideDao.insert(sunblides[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Sunblide, Void, Void>{

        private SunblideDao sunblideDao;

        public UpdateAsyncTask(SunblideDao sunblideDao) {
            this.sunblideDao = sunblideDao;
        }

        @Override
        protected Void doInBackground(Sunblide... sunblides) {
            sunblideDao.update(sunblides[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Sunblide, Void, Void> {

        private SunblideDao sunblideDao;

        public DeleteAsyncTask (SunblideDao sunblideDao) {
            this.sunblideDao = sunblideDao;
        }

        @Override
        protected Void doInBackground(Sunblide... sunblides) {
            sunblideDao.delete(sunblides[0]);
            return null;
        }
    }
}
