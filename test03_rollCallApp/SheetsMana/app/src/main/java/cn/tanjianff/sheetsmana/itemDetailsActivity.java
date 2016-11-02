package cn.tanjianff.sheetsmana;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

public class itemDetailsActivity extends AppCompatActivity {

    private EditText editText_name;
    private EditText editText_id;
    private ImageView icon;
    private CheckBox [] checkBoxes=new CheckBox[5];
    private Button cancel;
    private Button save;
    private static int [] checkboxIds=new int[]{R.id.case1,R.id.case2,R.id.case3,R.id.case4,R.id.case5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        editText_name= (EditText) findViewById(R.id.setName);
        editText_id= (EditText) findViewById(R.id.set_StdId);
        cancel=(Button)findViewById(R.id.cancelEdit);
        icon= (ImageView) findViewById(R.id.setIcon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                itemDetailsActivity.this.startActivityForResult(intent,1);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通知完成Activity从而返回上一Activity
                itemDetailsActivity.this.finish();
            }
        });
        save= (Button) findViewById(R.id.saveEdit);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stu_id=editText_id.getText().toString();
                stuSheet stu=new stuSheet();
                stu.setStd_id(stu_id);
                Toast.makeText(itemDetailsActivity.this,"正在保存更改...",Toast.LENGTH_SHORT);
            }
        });
        icon= (ImageView) findViewById(R.id.setIcon);
        for (int i=0;i<checkBoxes.length;i++){
            checkBoxes[i]= (CheckBox) findViewById(checkboxIds[i]);
        }
        Intent intent=getIntent();
        int position=0;
        intent.getIntExtra("clickItemOrder",position);
        CURDutil queryCollection=new CURDutil(getApplicationContext());
        ArrayList<stuSheet> collections=new ArrayList<> ();
        String strpos=String.valueOf(position);
        collections.addAll(queryCollection.queryComment(strpos));
        editText_id.setText(intent.getIntExtra("clickItemOrder",position)+""+queryCollection.queryComment(strpos).toString());

        if(!collections.isEmpty()){
            stuSheet current=collections.get(0);
            String state[]=new String[5];
            state[0]=current.getCase1();
            state[1]=current.getCase2();
            state[2]=current.getCase3();
            state[3]=current.getCase4();
            state[4]=current.getCase5();
            for(int i=0;i<state.length;i++){
                if(state[i]!=null){
                    checkBoxes[i].setEnabled(true);
                }
                else {
                    checkBoxes[i].setEnabled(false);
                }
            }
            icon.setImageURI(Uri.parse(current.getIcon()));
            editText_name.setText(current.getStd_name());
            editText_id.setText(current.getStd_id());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            File file = new File(img_path);
            Toast.makeText(itemDetailsActivity.this, file.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
