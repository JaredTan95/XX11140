package cn.tanjianff.sheetsmana;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    private PopupWindow popupWindow;
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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                showPopMenu();
                //dispatchTakePictureIntent();
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


    class AddBtnSaveOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Context context = getApplicationContext();
            String[] toAddVal = new String[]{v_Edname.getText().toString(), v_EdStdId.getText().toString()
                    , v_stdclass.getText().toString(), "0,0,0,0,0"};
            CURDutil curDutil = new CURDutil(context);
            //stuSheet(String icon, String std_id, String std_name, String std_className, String caseSelection)
            stuSheet student = new stuSheet("", imgbytes, toAddVal[1], toAddVal[0], toAddVal[2], toAddVal[3]);

            //尝试添加数据项
            if (curDutil.addOneItem(student)) {
                Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                Toast.makeText(context, "ok", Toast.LENGTH_LONG).show();
                AddItemActivity.this.setResult(Activity.RESULT_OK, intent);
                AddItemActivity.this.finish();
            } else {
                Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            iconBitmap = (Bitmap) extras.get("data");
            imgbytes= new ImagBiStorage(getApplicationContext()).Img2Byte(iconBitmap);
            v_icon.setImageBitmap(iconBitmap);
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
                        v_icon.setImageBitmap(image);
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
                        v_icon.setImageBitmap(image);
                    }
                }
            }
        }
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

    //打开相机功能
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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


    public void BindViewId() {
        ids = new int[]{R.id.add_icon, R.id.add_name, R.id.add_idNum, R.id.add_className, R.id.add_cancel, R.id.add_save};
        v_icon = (ImageView) findViewById(R.id.add_icon);
        v_Edname = (EditText) findViewById(R.id.add_name);
        v_EdStdId = (EditText) findViewById(R.id.add_idNum);
        v_stdclass = (EditText) findViewById(R.id.add_className);
        btn_cancel = (Button) findViewById(R.id.add_cancel);
        btn_save = (Button) findViewById(R.id.add_save);
        //popupWindow=findViewById(r.)
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
            popupWindow.setWidth(LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }

        popupWindow.setContentView(view);
        popupWindow.showAtLocation(bt_select_cancle, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }
}
