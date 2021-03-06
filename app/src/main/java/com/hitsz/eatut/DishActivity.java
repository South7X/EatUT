package com.hitsz.eatut;

import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hitsz.eatut.adapter.DishAdapter;
import com.hitsz.eatut.adapter.dish;
import com.hitsz.eatut.database.DishInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DishActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<dish> foodsList=new ArrayList<>();
    private FloatingActionButton fab;
    private String windowName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        Intent intent = getIntent();
        windowName = intent.getStringExtra("extra_data");
        findView();
        initfoods(windowName);
        initRecycle();
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.fab:
                Intent intent=new Intent(DishActivity.this,CheckActivity.class);
                startActivity(intent);
                break;

        }
    }

    protected void findView(){
        recyclerView=(RecyclerView)findViewById(R.id.dish_recycle);
        fab=(FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(this);
    }
    //初始化菜品信息
    private void initfoods(String windowName){
        List<DishInfo> dishInfos = LitePal.where("belongToWindow like ?", windowName).find(DishInfo.class);
        for (DishInfo dishInfo:dishInfos){
            String name = dishInfo.getDishName();
            int image = dishInfo.getImageID();
            float price = dishInfo.getDishPrice();
            float score = dishInfo.getDishScore();
            int dishID=dishInfo.getId();
            dish addDish = new dish(name, image, price, score,dishID);
            foodsList.add(addDish);
        }
    }

    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DishAdapter adapter=new DishAdapter(foodsList, new DishAdapter.daListener() {
            @Override
            public void da() {
                foodsList.clear();
                initfoods(windowName);
                initRecycle();
            }
        });
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
