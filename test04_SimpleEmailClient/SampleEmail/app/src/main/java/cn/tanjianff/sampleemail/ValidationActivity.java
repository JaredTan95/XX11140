package cn.tanjianff.sampleemail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ValidationActivity extends AppCompatActivity {

    private String headtitle;
    private TextView Vali_title;
    private EditText edx_emailAccount;
    private EditText edx_emailPwd;
    private Button cancel;
    private Button next;
    private ProgressBar progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        BindView();
        InitialData();
        BindOnClickListener();
    }

    public void BindView() {
        Vali_title = (TextView) findViewById(R.id.Vali_title);
        edx_emailAccount = (EditText) findViewById(R.id.edx_emailAccount);
        edx_emailPwd = (EditText) findViewById(R.id.edx_emailPwd);
        cancel= (Button) findViewById(R.id.btn_cancel);
        next= (Button) findViewById(R.id.btn_next);
        progress_bar= (ProgressBar) findViewById(R.id.progress_bar);
    }

    public void InitialData() {
        headtitle = getIntent().getStringExtra("toptitle");
        try{
            Vali_title.setText(headtitle);
            switch (headtitle) {
                case "Icloud":
                    edx_emailAccount.setHint("email@icloud.com");
                    break;
                case "網易163":
                    edx_emailAccount.setHint("email@163.com");
                    break;
                case "Google":
                    edx_emailAccount.setHint("email@google.com");
                    break;
                case "Outlook.com":
                    edx_emailAccount.setHint("email@outlook.com");
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            edx_emailAccount.setHint("email@hostname.com");
            Vali_title.setText("新建账户");
        }
    }

    public void BindOnClickListener(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    /*更改样式提示用户后台正在处理*/
                    progress_bar.setVisibility(View.VISIBLE);
                    Vali_title.setText("正在验证...");
                    next.setEnabled(false);

                    //TODO:添加邮箱验证操作


                    /*如果一切Ok则进入邮箱*/
                    Intent intent=new Intent(ValidationActivity.this,MailBoxPageActivity.class);
                    ValidationActivity.this.startActivity(intent);
                    finish();
                }catch (Exception e){
                    finish();//出现异常则直接完成此Activity
                }
            }
        });
    }
}
