package com.locus.locusdemo.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.locus.locusdemo.PassDataListener;
import com.locus.locusdemo.R;
import com.locus.locusdemo.db.ValueDAO;
import com.locus.locusdemo.model.DataModel;
import com.locus.locusdemo.util.PrefUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PassDataListener {

    @BindView(R.id.rv_data)
    RecyclerView rvData;
    Gson gson = new Gson();
    DataAdapter adapter;
    ArrayList
            <DataModel> dataModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dataModels = ValueDAO.getInstance().getData();

        adapter = new DataAdapter(this, dataModels, this);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        rvData.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_action, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit:
                ValueDAO.getInstance().updateData(dataModels);
                PrefUtils.saveHomeTabData(this,true);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void passData(ArrayList<DataModel> modelArrayList) {
        this.dataModels = modelArrayList;
    }
}
