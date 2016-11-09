package cn.tanjianff.sheetsmana;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Arrays;

import cn.tanjianff.sheetsmana.entity.stuSheet;
import cn.tanjianff.sheetsmana.util.CURDutil;
import cn.tanjianff.sheetsmana.util.ImagBiStorage;

public class itemDetailsActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private PopupWindow popupWindow;
    private static String posStr;
    private static Bitmap iconBitmap;
    private static byte[] imgbytes;
    private EditText editText_name;
    private EditText editText_id;
    private ImageView icon;
    private CheckBox[] checkBoxes = new CheckBox[5];
    private Button cancel;
    private Button save;
    private Button delete;
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
            ImagBiStorage imagBiStorage=new ImagBiStorage(getApplicationContext());
            iconBitmap=imagBiStorage.getBitmap(student.getIcon());
            if(iconBitmap!=null){
                imgbytes=imagBiStorage.Img2Byte(iconBitmap);
                icon.setImageBitmap(iconBitmap);
            }
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
            e.printStackTrace();
            Toast.makeText(this, "查询数据失败!", Toast.LENGTH_SHORT).show();
        }

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dispatchTakePictureIntent();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                showPopMenu();
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
               SubmitSaveEvent();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new CURDutil(getApplicationContext()).delateById(posStr)){
                    Snackbar.make(v, "你确定删除吗?", Snackbar.LENGTH_LONG)
                            .setAction("确认删除", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(new CURDutil(getApplicationContext()).delateById(posStr)){
                                        Snackbar.make(v,"Delete OK!",Snackbar.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Snackbar.make(v,"Delete Failed!",Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setActionTextColor(Color.WHITE).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            iconBitmap = (Bitmap) extras.get("data");
            imgbytes = new ImagBiStorage(getApplicationContext()).Img2Byte(iconBitmap);
            icon.setImageBitmap(iconBitmap);
        }*/
        //关闭拍照、选择照片弹窗
        closePopupWindow();

        if (data != null) {
            //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
            Uri mImageCaptureUri = data.getData();
            //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
            if (mImageCaptureUri != null) {
                Bitmap image;
                try {
                    //这个方法是根据Uri获取Bitmap图片的静态方法
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                    if (image != null) {
                        imgbytes= new ImagBiStorage(getApplicationContext()).Img2Byte(image);
                        icon.setImageBitmap(image);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"选取失败!",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                    Bitmap image = extras.getParcelable("data");
                    if (image != null) {
                        imgbytes= new ImagBiStorage(getApplicationContext()).Img2Byte(image);
                        icon.setImageBitmap(image);
                    }
                }
            }
        }
    }


    //打开相机功能
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //当点击'保存'按钮时,OnClickListener将调用此函数
    public void SubmitSaveEvent() {
        try {
            //stuSheet(String id, byte[] icon, String std_id, String std_name, String std_className, String caseSelection
            stuSheet stu = new stuSheet();
            stu.setID(posStr);
            stu.setIcon(imgbytes);
            stu.setStd_name(editText_name.getText().toString());
            stu.setStd_id(editText_id.getText().toString());
            StringBuilder toAddCase = new StringBuilder();

            //字符串拼接,将修改后的考勤状态存入数据库
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isChecked()) {
                    if (i == 0) {
                        toAddCase.append("1");
                    } else {
                        toAddCase.append(",1");
                    }
                }
                if (!checkBoxes[i].isChecked()) {
                    if (i == 0) {
                        toAddCase.append("0");
                    } else {
                        toAddCase.append(",0");
                    }
                }
            }
            stu.setCaseSelection(toAddCase.toString());
            if (new CURDutil(getApplicationContext()).updateById(stu)) {
                finish();
            } else{
                Toast.makeText(getApplicationContext(),"修改失败,操作!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "修改失败,异常!", Toast.LENGTH_SHORT).show();
        }
    }

    public void BindViewId() {
        editText_name = (EditText) findViewById(R.id.setName);
        editText_id = (EditText) findViewById(R.id.set_StdId);
        cancel = (Button) findViewById(R.id.cancelEdit);
        save = (Button) findViewById(R.id.saveEdit);
        delete= (Button) findViewById(R.id.btn_delete);
        icon = (ImageView) findViewById(R.id.setIcon);
        for (int i = 0; i < checkboxIds.length; i++) {
            //对单选框视图进行绑定Id
            checkBoxes[i] = (CheckBox) findViewById(checkboxIds[i]);
        }
    }

    private void showPopMenu() {
        View view = View.inflate(getApplicationContext(), R.layout.select_pic_popup_menu, null);
        RelativeLayout from_camera = (RelativeLayout) view.findViewById(R.id.from_camera);
        RelativeLayout from_gallery = (RelativeLayout) view.findViewById(R.id.from_gallery);
        Button bt_select_cancle = (Button) view.findViewById(R.id.bt_select_cancle);

        from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPicFromGallery();
            }
        });
        bt_select_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_bottom_in));

        if (popupWindow == null) {
            popupWindow = new PopupWindow(this);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }

        popupWindow.setContentView(view);
        popupWindow.showAtLocation(bt_select_cancle, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    public void dispatchPicFromGallery() {
        Intent intent = new Intent();
         /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
         /* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
    }

    /**
     * 关闭弹出窗口
     */
    private void closePopupWindow() {

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            WindowManager.LayoutParams params =getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);
        }
    }

}
