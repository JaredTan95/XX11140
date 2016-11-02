package cn.tanjianff.sheetsmana;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.tanjianff.sheetsmana.entity.stuSheet;
import cn.tanjianff.sheetsmana.util.CURDutil;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private CURDutil curdUtil;
    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        curdUtil=new CURDutil(getApplicationContext());
        listview= (ListView) findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:添加导入数据的操作
                Snackbar.make(view, "Snack Bar Text", Snackbar.LENGTH_LONG)
                        .setAction("I/OExcel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, IOExcelActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setActionTextColor(Color.WHITE).show();
            }
        });

 /*

        int [] orderNum=new int[]{1,2,3};
        int[] head_icon = new int[]{R.mipmap.wgp, R.mipmap.wgp, R.mipmap.wgp};
        String[] sid=new String[]{"631406010102","631406010103","631406010104"};
        String[] list_items_names = {"莫天金", "吴国平", "孙文斌"};
        *//*, "潘俊旭", "石佳磊", "赵权",
                "马鹏", "郭文浩", "李季", "陈仕豪", "杜菲", "李红兵", "蔡佳辰", "肖洒益",
                "伍凯荣", "张林", "王斌", "廖宇峰", "谭建", "左永和", "王增辉", "任中豪",
                "何泳桦", "张力", "任达"*//*
        String[] className = new String[]{"计科1401班", "计科1401班", "计科1401班"};

        List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
        for(int i=0;i<list_items_names.length;i++){
            Map<String,Object> showItems=new HashMap<String,Object>();
            showItems.put("orderNum",orderNum[i]);
            showItems.put("head_icon",head_icon[i]);
            showItems.put("sid",sid[i]);
            showItems.put("list_items_names",list_items_names[i]);
            showItems.put("className",className[i]);
            listItems.add(showItems);
        }

        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter=new SimpleAdapter(getApplicationContext(),listItems,
                R.layout.list_item,
                new String[]{"orderNum","head_icon","sid","list_items_names","className"},
                new int[]{R.id.orderNum,R.id.ic_std_head,R.id.std_id,R.id.std_name,R.id.className});



        listview.setAdapter(simpleAdapter);
        */

        //testAddData();
        /*此处数据将从SQLite数据库中获取*/
        //查询数据库里的数据
        query();
        //为每一个item绑定点击事件
        listview.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_add){
          return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,itemDetailsActivity.class);
        intent.putExtra("clickItemOrder",(position+1));
        MainActivity.this.startActivity(intent);
        Toast.makeText(MainActivity.this,"你点击了第" + (position+1) + "项",Toast.LENGTH_SHORT).show();
    }

    public void query() {
        List<stuSheet> students = curdUtil.query();
        List<Map<String, Object>> listItems = new ArrayList<>();
        int i=0;
        for (stuSheet std : students) {
            Map<String,Object> showItems= new HashMap<>();
            showItems.put("orderNum",++i);
            showItems.put("head_icon",std.getIcon());
            showItems.put("sid",std.getStd_id());
            showItems.put("list_items_names",std.getStd_name());
            showItems.put("className",std.getStd_className());
            listItems.add(showItems);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter=new SimpleAdapter(getApplicationContext(),listItems,
                R.layout.list_item,
                new String[]{"orderNum","head_icon","sid","list_items_names","className"},
                new int[]{R.id.orderNum,R.id.ic_std_head,R.id.std_id,R.id.std_name,R.id.className});
        listview.setAdapter(simpleAdapter);
    }

    public void testAddData(){
        ArrayList<stuSheet> students = new ArrayList<stuSheet>();
        stuSheet std1 = new stuSheet("wpg", "631406010122", "Jenny", "计科一班","11100");
        stuSheet std2 = new stuSheet("sdd", "631406010123", "Jessica", "计科一班","11000");
        stuSheet std3 = new stuSheet("dsfs", "631406010124", "sexy girl", "计科一班","00000");
        stuSheet std4 = new stuSheet("dfswrt", "631406010125", "Kelly", "计科一班","01011");
        stuSheet std5 = new stuSheet("fhjtr", "631406010126", "Jane", "计科一班","11111");
        students.add(std1);
        students.add(std2);
        students.add(std3);
        students.add(std4);
        students.add(std5);
        curdUtil.add(students);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        curdUtil.closeDB();
    }
}
