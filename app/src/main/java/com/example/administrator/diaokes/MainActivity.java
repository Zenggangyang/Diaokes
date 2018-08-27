package com.example.administrator.diaokes;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.preference.PreferenceManager;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Window;

        import com.ashokvarma.bottomnavigation.BadgeItem;
        import com.ashokvarma.bottomnavigation.BottomNavigationBar;
        import com.ashokvarma.bottomnavigation.BottomNavigationItem;
        import com.example.administrator.diaokes.Fragment.Found;
        import com.example.administrator.diaokes.Fragment.Main;
        import com.example.administrator.diaokes.Fragment.Personal;
        import com.example.administrator.diaokes.Fragment.Topic;
        import com.example.administrator.diaokes.services.AutoUpdateService;

        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar bottomNavigationBar;
    private BadgeItem badgeItem;
    private Main _main;
    private Found _found;
    private Topic _topic;
    private Personal _personal;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra("name");


        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bar);
        badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("99");
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.background);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.main,"首页").setActiveColorResource(R.color.active).setInActiveColor(R.color.black))
                .addItem(new BottomNavigationItem(R.drawable.found,"发现").setActiveColorResource(R.color.active).setInActiveColor(R.color.black))
                .addItem(new BottomNavigationItem(R.drawable.topic,"话题").setActiveColorResource(R.color.active).setInActiveColor(R.color.black).setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(R.drawable.personal,"个人").setActiveColorResource(R.color.active).setInActiveColor(R.color.black))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultDisplay();
    }
    public void setDefaultDisplay(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment,Main.newInstance(username)).commit();
    }
/*
    private ArrayList<Fragment> getFragments(){
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(Main.newInstance());
        list.add(Found.newInstance());
        list.add(Topic.newInstance());
        list.add(Personal.newInstance());
        return list;
    }
*/
    @Override
    public void onTabSelected(int position) {
        /*
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //当前的Fragment
                Fragment fragment = fm.findFragmentById(R.id.fragment);
                if(fragment instanceof Main){
                    ft.remove(fragment);
                }
                if(fragment instanceof Found){
                    ft.remove(fragment);
                }
                //点击即将跳转的Fragment
                Fragment to = fragments.get(position);
                /*
                if (position == 0) {
                    ft.replace(R.id.fragment, to);
                } else {
                    if (to.isAdded()) {
                        //隐藏当前的Fragment,显示下一个
                        ft.hide(fragment).show(to);
                    } else {
                        ft.add(R.id.fragment, to).hide(fragment);
                    }
                }*/
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (position) {
            case 0:
                /*
                if (_main == null) {
                    _main = (Main) _main.newInstance();
                }
                ft.add(R.id.fragment, _main);
                _main.onStart();*/
                if (_main == null) {
                    _main = (Main) _main.newInstance(username);
                }
                ft.replace(R.id.fragment, _main);
                break;
            case 1:
                if (_found == null) {
                    _found = (Found) _found.newInstance(username);
                }
                ft.replace(R.id.fragment, _found);
                        /*
                        if (to.isAdded()) {
                            ft.hide(fragment).show(to);
                        } else {
                            ft.add(R.id.fragment, to).hide(fragment);
                        }*/
                break;
            case 2:
                /*
                if (to.isAdded()) {
                    ft.hide(fragment).show(to);
                } else {
                    ft.add(R.id.fragment, to).hide(fragment);
                }
                */
                break;
            case 3:
                if (_personal == null) {
                    _personal = (Personal) _personal.newInstance(username);
                }
                ft.replace(R.id.fragment, _personal);
                break;
            default:
                break;
        }
        ft.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
