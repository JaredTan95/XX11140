package cn.tanjianff.simpleemailapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import cn.tanjianff.simpleemailapp.dbEntity.MailBean;
import cn.tanjianff.simpleemailapp.dbEntity.accountRec;
import cn.tanjianff.simpleemailapp.dbEntity.receiveMailBean;
import cn.tanjianff.simpleemailapp.util.CURDutil;
import cn.tanjianff.simpleemailapp.util.javaMailUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private letterList letterlistview;

    private static ArrayList<receiveMailBean> letterlistData=new ArrayList<>();

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

        //接受数据,绑定listview展示邮件列表
        letterlistview= (letterList) findViewById(R.id.letterlist);

        new Thread(new networkTask()).start();

        letterlistview.setAdapter(new letterListItemAdapter());
        letterlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(letterlistview.canClick()) {
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,ScrollingActivity.class);
                    MainActivity.this.startActivity(intent);
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class letterListItemAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(letterlistData!=null){
                return letterlistData.size();
            }
            else {
                return 0;
            }
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

            tv.setText(letterlistData.get(position).getSubject());

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

    class networkTask implements Runnable {
        @Override
        public void run() {
            String[] userProps = getIntent().getStringArrayExtra("userProps");
            MailBean mailBean = new MailBean();
            if (userProps != null) {
                try {
                    mailBean.setUsername(userProps[0]);
                    mailBean.setPassword(userProps[1]);
                    letterlistData.addAll(javaMailUtil.receive(mailBean));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
