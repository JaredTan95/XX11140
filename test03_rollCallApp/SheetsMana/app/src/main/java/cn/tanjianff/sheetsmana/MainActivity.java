package cn.tanjianff.sheetsmana;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.tanjianff.sheetsmana.entity.stuSheet;
import cn.tanjianff.sheetsmana.util.CURDutil;
import cn.tanjianff.sheetsmana.util.ImagBiStorage;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private CURDutil curdUtil;
    private ListView listview;
    private static SimpleAdapter simpleAdapter;
    private static boolean isEmpty=true;
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
                Snackbar.make(view, "你可以导入Excel文件.", Snackbar.LENGTH_LONG)
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



        //testAddData();//添加演示数据
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
            Intent intent=new Intent(MainActivity.this,AddItemActivity.class);
            MainActivity.this.startActivity(intent);
            //return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Adapter adapter=parent.getAdapter();
        //Map<String,String> map=(Map<String, String>) adapter.getItem(position);

        //Toast.makeText(getApplicationContext(),map.get("std_name"),Toast.LENGTH_LONG).show();
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,itemDetailsActivity.class);
        String pos=String.valueOf(position+1);
        intent.putExtra("clickItemOrder",pos);
        MainActivity.this.startActivity(intent);
        //Toast.makeText(MainActivity.this,"你点击了第" + (position+1) + "项",Toast.LENGTH_SHORT).show();
    }

    public void query() {
        List<stuSheet> students = curdUtil.query();
        if(students!=null){
            isEmpty=false;
        }
        List<Map<String, Object>> listItems = new ArrayList<>();
        ImagBiStorage imagBiStorage=new ImagBiStorage(this);
        for (stuSheet std : students) {
            Map<String,Object> showItems= new HashMap<>();
            showItems.put("orderNum",std.getID());
            //将位图转换成资源文件
            showItems.put("head_icon",Bitmap2drawable(imagBiStorage.getBitmap(std.getIcon())));
            showItems.put("sid",std.getStd_id());
            showItems.put("list_items_names",std.getStd_name());
            showItems.put("className",std.getStd_className());
            listItems.add(showItems);
        }
        //创建一个SimpleAdapter
        simpleAdapter=new SimpleAdapter(getApplicationContext(),listItems,
                R.layout.list_item,
                new String[]{"orderNum","head_icon","sid","list_items_names","className"},
                new int[]{R.id.orderNum,R.id.ic_std_head,R.id.std_id,R.id.std_name,R.id.className});
        listview.setAdapter(simpleAdapter);
    }

    public void testAddData(){
        ArrayList<stuSheet> students = new ArrayList<stuSheet>();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_icon)).getBitmap();
        byte[] bytes=new ImagBiStorage(getApplicationContext()).Img2Byte(bitmap);
        stuSheet std1 = new stuSheet("",bytes, "631406010122", "Jenny", "计科一班","11100");
        stuSheet std2 = new stuSheet("",bytes, "631406010123", "Jessica", "计科一班","11000");
        stuSheet std3 = new stuSheet("",bytes, "631406010124", "sexy girl", "计科一班","00000");
        stuSheet std4 = new stuSheet("",bytes, "631406010125", "Kelly", "计科一班","01011");
        stuSheet std5 = new stuSheet("",bytes, "631406010126", "Jane", "计科一班","11111");
        students.add(std1);
        students.add(std2);
        students.add(std3);
        students.add(std4);
        students.add(std5);
        curdUtil.add(students);
    }


    public Drawable Bitmap2drawable(Bitmap bp) {
        //因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        Bitmap bm = bp;
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        return bd;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        curdUtil.closeDB();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(),"Reloading...",Toast.LENGTH_SHORT).show();
        //重新加载数据
        simpleAdapter.notifyDataSetChanged();
        query();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //如果resultCode是RESULT_OK的话，就把内容重新加载显示出来。
            onRestart();
        }


    }

    /*重写SimpleAdapter,以此实现图片适配的问题,因为SimpleAdapter不能对图片进行适配,重写即可*/
    class SimpleAdapter extends android.widget.SimpleAdapter {
        private int[] appTo;
        private String[] appFrom;
        private ViewBinder appViewBinder;
        private List<? extends Map<String, ?>> appData;
        private int appResource;
        private LayoutInflater appInflater;

        public SimpleAdapter(Context context, List<? extends Map<String, ?>> data,
                             int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            appData = data;
            appResource = resource;
            appFrom = from;
            appTo = to;
            appInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return createViewFromResource(position, convertView, parent, appResource);
        }

        private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
            View v;

            if (convertView == null) {
                v = appInflater.inflate(resource, parent, false);
                final int[] to = appTo;
                final int count = to.length;
                final View[] holder = new View[count];

                for (int i = 0; i < count; i++) {
                    holder[i] = v.findViewById(to[i]);
                }

                v.setTag(holder);
            } else {
                v = convertView;
            }
            bindView(position, v);
            return v;
        }

        private void bindView(int position, View view) {
            final Map<String, ?> dataSet = appData.get(position);
            if (dataSet == null) {
                return;
            }

            final ViewBinder binder = appViewBinder;
            final View[] holder = (View[]) view.getTag();
            final String[] from = appFrom;
            final int[] to = appTo;
            final int count = to.length;

            for (int i = 0; i < count; i++) {
                final View v = holder[i];

                if (v != null) {
                    final Object data = dataSet.get(from[i]);
                    String text = data == null ? "" : data.toString();

                    if (text == null) {
                        text = "";
                    }

                    boolean bound = false;

                    if (binder != null) {
                        bound = binder.setViewValue(v, data, text);
                    }

                    if (!bound) {
                        if (v instanceof TextView) {
                            setViewText((TextView) v, text);
                        } else if (v instanceof ImageView) {
                            setViewImage((ImageView) v, (Drawable) data);
                        } else {
                            throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                    "view that can be bounds by this SimpleAdapter");
                        }
                    }
                }
            }
        }

        public void setViewImage(ImageView v, Drawable value) {
            v.setImageDrawable(value);
        }
    }

}
