package cn.tanjianff.sampleemail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.tanjianff.sampleemail.InheritanceView.letterList;
import cn.tanjianff.sampleemail.dbEntity.MailBean;
import cn.tanjianff.sampleemail.dbEntity.receiveMailBean;
import cn.tanjianff.sampleemail.util.javaMailUtil;

public class MailBoxPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private letterList letterlistview;
    private static ArrayList<receiveMailBean> letterlistData= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /***********************************************/

        //接受数据,绑定listview展示邮件列表
        letterlistview= (letterList) findViewById(R.id.letterlist);

        //new Thread(new networkTask()).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] userProps = getIntent().getStringArrayExtra("userProps");
                MailBean mailBean = new MailBean();
                if (userProps != null) {
                    try {
                        mailBean.setUsername(userProps[0]);
                        mailBean.setPassword(userProps[1]);
                        System.err.println(userProps[0] + " " + userProps[1]);
                        letterlistData.addAll(javaMailUtil.receive(mailBean));
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),userProps[0]+userProps[1]+"\n"+letterlistData.toString(),Toast.LENGTH_LONG);
                    }
                }
            }
        }).start();

        letterlistview.setAdapter(new letterListItemAdapter());
        letterlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(letterlistview.canClick()) {
                    Intent intent=new Intent();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mail_box_page, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.menu_write:
                //TODO:Handle the write letter action
            break;
            case R.id.menu_mailbox:
                //TODO:Handle the write letter action
                break;
            case R.id.menu_sended:
                //TODO:Handle the write letter action
                break;
            case R.id.menu_addAccount:
                //TODO:Handle the write letter action
                break;
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
                convertView = View.inflate(MailBoxPageActivity.this, R.layout.list_item, null);
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
                    System.err.println(userProps[0]+" "+userProps[1]);
                    letterlistData.addAll(javaMailUtil.receive(mailBean));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
