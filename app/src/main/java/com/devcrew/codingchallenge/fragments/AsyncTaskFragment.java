package com.devcrew.codingchallenge.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by Qamar on 9/16/2016.
 */
public class AsyncTaskFragment extends Fragment {

    /**
     * Callback through which is used to report
     * task's progress and results back to the Activity.
     */
    public interface TaskCallbacks {
        void onPreExecute();

        void onCancelled();

        void onPostExecute(ArrayList<String> dataset);
    }

    private TaskCallbacks mCallbacks;
    private DummyTask mTask;


    /**
     * Keep the reference to actvity so that
     * when activity is recreated we release the
     * reference and hold it again
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (TaskCallbacks) context;

    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created. After that it will not be called
     * as its retained
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across config changes.
        setRetainInstance(true);

        // execute the background task.
        mTask = new DummyTask();
        mTask.execute();
    }

    private ArrayList<String> getDataFromWebService() {
        ArrayList<String> dataSet = new ArrayList<>();
        dataSet.add("M, Afzal");
        dataSet.add("Mr, Qamar");
        dataSet.add("Mr, Amjad");
        return dataSet;
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

    }

    /**
     * A dummy task that performs some background work and
     * proxies progress updates and results back to the Activity.
     * <p/>
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    private class DummyTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }

        }

        @Override
        protected ArrayList<String> doInBackground(Void... ignore) {
            for (int i = 0; !isCancelled() && i < 100; i++) {
                SystemClock.sleep(100);
            }

            return getDataFromWebService();
        }

        @Override
        protected void onCancelled() {
            // report that task is canceled
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> data) {
            //report that task execution completed
            if (mCallbacks != null) {
                mCallbacks.onPostExecute(data);
            }
        }
    }
}
