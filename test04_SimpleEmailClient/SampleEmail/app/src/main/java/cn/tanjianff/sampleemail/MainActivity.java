package cn.tanjianff.sampleemail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private ListView lv_options;
    private TextView otherEmails;
    public static String[] emailNames;
    private int [] icon_email;
    private Context thisContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisContext=getApplicationContext();
        BindView();
        InitialData();
        BindData();
    }

    public void BindData(){
       List<Map<String, Object>> listitem = new ArrayList<>();
        for (int i = 0; i < emailNames.length; i++) {
            Map<String, Object> showitem = new HashMap<>();
            showitem.put("nameemail", emailNames[i]);
            showitem.put("iconemail", icon_email[i]);
            listitem.add(showitem);
        }

        SimpleAdapter simpleAdapter=new SimpleAdapter(thisContext,listitem,R.layout.list_options
                ,new String[]{"iconemail","nameemail"},new int[]{R.id.icon_email,R.id.name_email});

        lv_options.setAdapter(simpleAdapter);
        lv_options.setOnItemClickListener(this);


        otherEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ValidationActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    public void BindView(){
        lv_options= (ListView) findViewById(R.id.list_options);
        otherEmails= (TextView) findViewById(R.id.otherEmails);
    }

    public void InitialData(){
        emailNames=new String[]{"Icloud","網易163","Google","Outlook.com"};
        icon_email=new int[]{R.mipmap.icloud,R.mipmap.netease,
              R.mipmap.google,R.mipmap.outlook};
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,ValidationActivity.class);
        intent.putExtra("toptitle",emailNames[position]);
        MainActivity.this.startActivity(intent);
    }
}
