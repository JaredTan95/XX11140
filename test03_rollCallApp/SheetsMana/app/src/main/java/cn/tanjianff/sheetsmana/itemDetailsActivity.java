package cn.tanjianff.sheetsmana;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.tanjianff.sheetsmana.entity.stuSheet;
import cn.tanjianff.sheetsmana.util.CURDutil;
import cn.tanjianff.sheetsmana.util.ImagBiStorage;

public class itemDetailsActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static String posStr;
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
        //绑定视图
        BindViewId();

        //用于接受上一个Activity向此Activity传入的item的id
        posStr = getIntent().getStringExtra("clickItemOrder");
        CURDutil queryItem = new CURDutil(getApplicationContext());
        stuSheet student = queryItem.queryById(posStr);
        try {
            editText_id.setText(student.getStd_id());
            editText_name.setText(student.getStd_name());
            icon.setImageBitmap(new ImagBiStorage(this).getBitmap(student.getIcon()));
            String[] caseStrArray = student.getCaseSelection().split(",");
            for (int i = 0; i < checkboxIds.length; i++) {
                //此处界限应该以checkBox[]的长度为准,避免异常
                if (!caseStrArray[i].equals("0")) {
                    checkBoxes[i].setChecked(true);
                } else {
                    checkBoxes[i].setChecked(false);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "查询数据失败!", Toast.LENGTH_SHORT).show();
        }

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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //stuSheet(String id, byte[] icon, String std_id, String std_name, String std_className, String caseSelection
                    stuSheet stu = new stuSheet();
                    stu.setID(posStr);

                    ImagBiStorage imagBiStorage=new ImagBiStorage(getApplicationContext());
                    Bitmap bitmap=imagBiStorage.drawable2Bitmap(icon.getDrawable());
                    stu.setIcon(imagBiStorage.Img2Byte(bitmap));

                    stu.setStd_name(editText_name.getText().toString());
                    stu.setStd_id(editText_id.getText().toString());
                    StringBuilder toAddCase=new StringBuilder();
                    for(int i=0;i<checkBoxes.length;i++){
                        if(checkBoxes[i].isChecked()){
                            if(i==0){
                                toAddCase.append("1");
                            }else{
                                toAddCase.append(",1");
                            }
                        }
                        if(!checkBoxes[i].isChecked()) {
                            if(i==0){
                                toAddCase.append("0");
                            }else{
                                toAddCase.append(",0");
                            }
                        }
                    }
                    stu.setCaseSelection(toAddCase.toString());
                    if(new CURDutil(getApplicationContext()).updateStuSheetItem(stu)){
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "修改失败!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "修改失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public void BindViewId() {
        editText_name = (EditText) findViewById(R.id.setName);
        editText_id = (EditText) findViewById(R.id.set_StdId);
        cancel = (Button) findViewById(R.id.cancelEdit);
        save = (Button) findViewById(R.id.saveEdit);
        icon = (ImageView) findViewById(R.id.setIcon);
        for (int i = 0; i < checkboxIds.length; i++) {
            //对单选框视图进行绑定Id
            checkBoxes[i] = (CheckBox) findViewById(checkboxIds[i]);
        }
    }
}
