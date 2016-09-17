package com.devcrew.codingchallenge;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.devcrew.codingchallenge.adapters.NamesAdapter;
import com.devcrew.codingchallenge.common.Utils;
import com.devcrew.codingchallenge.fragments.AsyncTaskFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AsyncTaskFragment.TaskCallbacks {

    private static final String KEY_NAME = "name";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATASET = "dataset";
    private static final String KEY_IS_LOADING = "isloading";
    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mDataset;
    private ProgressDialog mProgressDialog;
    private boolean isLoadingData;

    private AsyncTaskFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.message_loading));

        if (savedInstanceState != null) {
            mDataset = savedInstanceState.getStringArrayList(KEY_DATASET);
            isLoadingData = savedInstanceState.getBoolean(KEY_IS_LOADING);
            if (isLoadingData) {
                mProgressDialog.show();
            }
        } else {
            mDataset = new ArrayList<>();
        }
        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (AsyncTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new AsyncTaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
        // specify an adapter (see also next example)
        mAdapter = new NamesAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(KEY_DATASET, mDataset);
        outState.putBoolean(KEY_IS_LOADING, isLoadingData);
        super.onSaveInstanceState(outState);
    }

    void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(
                R.string.dialog_title);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick(String text) {

        if (Utils.isValidCommaSepratedName(text)) {
            mDataset.add(Utils.toTitleCase(text));
            mAdapter.notifyDataSetChanged();
            Snackbar.make(fab, R.string.message_name_added, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(fab, R.string.message_error_invalid_input, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void doNegativeClick() {
    }

    @Override
    public void onPreExecute() {
        isLoadingData = true;
        mProgressDialog.show();
    }

    @Override
    public void onCancelled() {
        isLoadingData = false;
        mProgressDialog.dismiss();
    }

    @Override
    public void onPostExecute(ArrayList<String> dataset) {
        isLoadingData = false;
        mProgressDialog.dismiss();
        mDataset.addAll(dataset);
        mAdapter.notifyDataSetChanged();
    }

    public static class MyAlertDialogFragment extends DialogFragment {
        TextInputEditText inputEditText;

        public static MyAlertDialogFragment newInstance(int title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt(KEY_TITLE, title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putString(KEY_NAME, inputEditText.getText().toString());
            super.onSaveInstanceState(outState);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt(KEY_TITLE);
            inputEditText = new TextInputEditText(getActivity());
            if (savedInstanceState != null) {
                inputEditText.setText(savedInstanceState.getString(KEY_NAME));
            }
            return new AlertDialog.Builder(getActivity())
                    .setView(inputEditText)
                    .setTitle(title)
                    .setPositiveButton(R.string.dialog_button_add,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((MainActivity) getActivity()).doPositiveClick(inputEditText.getText().toString());
                                }
                            }
                    )
                    .setNegativeButton(R.string.dialog_button_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((MainActivity) getActivity()).doNegativeClick();
                                }
                            }
                    )
                    .create();
        }
    }

}
