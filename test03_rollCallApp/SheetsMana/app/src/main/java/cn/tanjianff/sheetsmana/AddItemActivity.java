package cn.tanjianff.sheetsmana;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.tanjianff.sheetsmana.entity.stuSheet;
import cn.tanjianff.sheetsmana.util.CURDutil;
import cn.tanjianff.sheetsmana.util.ImagBiStorage;

import static cn.tanjianff.sheetsmana.itemDetailsActivity.REQUEST_IMAGE_CAPTURE;

public class AddItemActivity extends AppCompatActivity {

     static int[] ids;
     static ImageView v_icon;
     static EditText v_Edname;
     static EditText v_EdStdId;
     static EditText v_stdclass;
     static Button btn_cancel;
     static Button btn_save;
    private ProgressBar progressBar;
    private Bitmap iconBitmap;
    private byte[] imgbytes;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        //绑定视图id
        BindViewId();


        v_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        //为"取消"、"确定"按钮绑定点击监听事件
        btn_save.setOnClickListener(new AddBtnSaveOnClickListener());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    class AddBtnSaveOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Context context=getApplicationContext();
            String[] toAddVal=new String[]{v_Edname.getText().toString(),v_EdStdId.getText().toString()
                    ,v_stdclass.getText().toString(),"0,0,0,0,0"};
           // Toast.makeText(context,toAddVal[1].toString(),Toast.LENGTH_LONG).show();
            CURDutil curDutil=new CURDutil(context);
            //stuSheet(String icon, String std_id, String std_name, String std_className, String caseSelection)
            stuSheet student=new stuSheet(imgbytes,toAddVal[1],toAddVal[0],toAddVal[2],toAddVal[3]);

          /*  List<stuSheet> toaddList=new ArrayList<>();
            toaddList.add(student);*/
                    //创建进度条,提示用户后台正在操作
            progressBar=new ProgressBar(getApplicationContext());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(50);
                        int i=0;
                        progressBar.setProgress(++i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            //curDutil.add(toaddList);
            if(curDutil.addOneItem(student)){
                progressBar.setProgress(100);
                progressBar.setBackgroundColor(Color.WHITE);
                Toast.makeText(context,"ok",Toast.LENGTH_LONG).show();
            }else {
                progressBar.setProgress(50);
                progressBar.setBackgroundColor(Color.RED);
                Toast.makeText(context,"Fail",Toast.LENGTH_LONG).show();
            }
            Intent intent=new Intent(AddItemActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            iconBitmap = (Bitmap) extras.get("data");
            imgbytes= new ImagBiStorage(getApplicationContext()).Img2Byte(iconBitmap);
            v_icon.setImageBitmap(iconBitmap);
        }
    }


    //打开相机功能
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    public void BindViewId(){
        ids=new int[]{R.id.add_icon,R.id.add_name,R.id.add_idNum,R.id.add_className,R.id.add_cancel,R.id.add_save};
        v_icon= (ImageView) findViewById(R.id.add_icon);
        v_Edname= (EditText) findViewById(R.id.add_name);
        v_EdStdId= (EditText) findViewById(R.id.add_idNum);
        v_stdclass= (EditText) findViewById(R.id.add_className);
        btn_cancel= (Button) findViewById(R.id.add_cancel);
        btn_save= (Button) findViewById(R.id.add_save);
        progressBar= (ProgressBar) findViewById(R.id.progressBar_horizontal);
    }
}
