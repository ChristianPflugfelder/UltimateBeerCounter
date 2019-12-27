package de.haepfl.ultimatebeercounter;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import de.haepfl.ultimatebeercounter.Room.Day;
import de.haepfl.ultimatebeercounter.Room.DayDao;
import de.haepfl.ultimatebeercounter.Room.DayDatabase;

public class DayRepository {

    private DayDao dayDao;
    private List<Day> allDays;

    public DayRepository(Context application) {
        DayDatabase database = DayDatabase.getInstance(application);
        dayDao = database.dayDao();
        allDays = dayDao.getAllDays();
    }

    public void insert(Day day) {
        new InsertDayAsyncTask(dayDao).execute(day);
    }

    public void delete(Day day) {
        new DeleteDayAsyncTask(dayDao).execute(day);
    }

    public void deleteAllDays() {
        new DeleteAllDaysNoteAsyncTask(dayDao).execute();
    }

    public List<Day> getAllDays() {
        return allDays;
    }


    //Can fit all classes in to one

    private static class InsertDayAsyncTask extends AsyncTask<Day, Void, Void> {
        private DayDao dayDao;

        private InsertDayAsyncTask(DayDao dayDao) {
            this.dayDao = dayDao;
        }

        @Override
        protected Void doInBackground(Day... days) {
            dayDao.insert((days[0]));
            return null;
        }
    }

    private static class DeleteDayAsyncTask extends AsyncTask<Day, Void, Void> {
        private DayDao dayDao;

        private DeleteDayAsyncTask(DayDao dayDao) {
            this.dayDao = dayDao;
        }

        @Override
        protected Void doInBackground(Day... days) {
            dayDao.delete((days[0]));
            return null;
        }
    }

    private static class DeleteAllDaysNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private DayDao dayDao;

        private DeleteAllDaysNoteAsyncTask(DayDao dayDao) {
            this.dayDao = dayDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dayDao.deleteAllDays();
            return null;
        }
    }
}
