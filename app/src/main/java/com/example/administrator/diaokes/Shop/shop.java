package com.example.administrator.diaokes.Shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.Rank.Fragment1;
import com.example.administrator.diaokes.Rank.Fragment2;
import com.example.administrator.diaokes.recyclerView.shopAdapter;
import com.example.administrator.diaokes.recyclerView.viewpageAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/27.
 */

public class shop extends AppCompatActivity {
    private Toolbar toolbar;
    private SearchView searchView;
    private String[] titles = {"推荐", "鱼竿","鱼线","浮漂","鱼钩","饵料","配件"};
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> list1;
    private String data = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_layout);
        toolbar = findViewById(R.id.shoptoolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.shop_tab);
        viewPager = findViewById(R.id.shop_page);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.floatcolor));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("我的商城");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        data = intent.getStringExtra("data");

        list1 = new ArrayList<>();
        list1.add(new recommend());
        list1.add(new yugan());
        list1.add(new yuxian());
        list1.add(new fupiao());
        list1.add(new yugou());
        list1.add(new erliao());
        list1.add(new others());

        viewPager.setAdapter(new shopAdapter(getSupportFragmentManager(),list1,titles));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        //通过MenuItem得到SearchView
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.shop_car_button:
                Intent intent = new Intent(this,shopCar.class);
                intent.putExtra("data",data);
                startActivity(intent);
                break;
        }
        return true;
    }
}
