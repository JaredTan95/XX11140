package cn.tanjianff.simpleemailapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private letterList letterlistview;

    private ArrayList<String> letterlistData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "欢迎使用!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        letterlistview= (letterList) findViewById(R.id.letterlist);
        letterlistData= new ArrayList<>();
        for(int i=0;i<50;i++) {
            letterlistData.add("xxx发来邮件" + i);
        }

        letterlistview.setAdapter(new letterListItemAdapter());
        letterlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(letterlistview.canClick()) {
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,ScrollingActivity.class);
                    MainActivity.this.startActivity(intent);
                    Toast.makeText(MainActivity.this, letterlistData.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addAccount) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AddAccountActivity.class);
            MainActivity.this.startActivity(intent);
        }  else if (id == R.id.nav_Create) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WriteActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (id == R.id.nav_settings) {
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,SettingsActivity.class);
            MainActivity.this.startActivity(intent);
           // Toast.makeText(this,"设置",Toast.LENGTH_SHORT);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class letterListItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return letterlistData.size();
        }

        @Override
        public Object getItem(int position) {
            return letterlistData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView) {
                convertView = View.inflate(MainActivity.this, R.layout.list_item, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.mailTitle);
            TextView delete = (TextView) convertView.findViewById(R.id.maildelete);

            tv.setText(letterlistData.get(position));

            final int pos = position;
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    letterlistData.remove(pos);
                    notifyDataSetChanged();
                    letterlistview.turnToNormal();
                }
            });
            return convertView;
        }
    }
}
