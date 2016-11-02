package cn.tanjianff.sheetsmana;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.tanjianff.sheetsmana.util.jExcel;
import jxl.read.biff.BiffException;

public class IOExcelActivity extends AppCompatActivity {

    private Button importexcel;
    private Button out2Excel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ioexcel);
        importexcel= (Button) findViewById(R.id.importexcel);
        out2Excel= (Button) findViewById(R.id.out2Excel);
        importexcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                IOExcelActivity.this.startActivityForResult(intent,1);
            }
        });

        out2Excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String[] proj = {MediaStore.Images.Media.DATA};

            try {
                String rel=jExcel.readExcelTable(uri.toString());
                Toast.makeText(IOExcelActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }
        }
    }
}
