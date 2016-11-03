package cn.tanjianff.sheetsmana;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import cn.tanjianff.sheetsmana.entity.stuSheet;
import cn.tanjianff.sheetsmana.util.CURDutil;
import cn.tanjianff.sheetsmana.util.ImagBiStorage;

public class itemDetailsActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static Bitmap iconBitmap;
    private byte[] imgbytes;
    private EditText editText_name;
    private EditText editText_id;
    private ImageView icon;
    private CheckBox[] checkBoxes = new CheckBox[5];
    private Button cancel;
    private Button save;
    private static int[] checkboxIds = new int[]{R.id.case1, R.id.case2, R.id.case3, R.id.case4, R.id.case5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        editText_name = (EditText) findViewById(R.id.setName);
        editText_id = (EditText) findViewById(R.id.set_StdId);
        cancel = (Button) findViewById(R.id.cancelEdit);
        icon = (ImageView) findViewById(R.id.setIcon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通知完成Activity从而返回上一Activity
                itemDetailsActivity.this.finish();
            }
        });
        save = (Button) findViewById(R.id.saveEdit);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stu_id = editText_id.getText().toString();
                stuSheet stu = new stuSheet();
                stu.setStd_id(stu_id);
                Toast.makeText(itemDetailsActivity.this, "正在保存更改...", Toast.LENGTH_SHORT);
            }
        });
        icon = (ImageView) findViewById(R.id.setIcon);
        Intent intent = getIntent();
        int position = 0;
        intent.getIntExtra("clickItemOrder", position);
        CURDutil queryCollection = new CURDutil(getApplicationContext());
        ArrayList<stuSheet> collections = new ArrayList<>();
        //collections.addAll(queryCollection.queryComment(position));
        //editText_id.setText(intent.getIntExtra("clickItemOrder",position));
       /* if(!collections.isEmpty()){
            stuSheet current=collections.get(0);
            icon.setImageBitmap(new ImagBiStorage(getApplicationContext()).getBitmap(current.getIcon()));
            editText_name.setText(current.getStd_name());
            editText_id.setText(current.getStd_id());}*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            iconBitmap = (Bitmap) extras.get("data");
            imgbytes = new ImagBiStorage(getApplicationContext()).Img2Byte(iconBitmap);
            icon.setImageBitmap(iconBitmap);
        }
    }


    //打开相机功能
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
