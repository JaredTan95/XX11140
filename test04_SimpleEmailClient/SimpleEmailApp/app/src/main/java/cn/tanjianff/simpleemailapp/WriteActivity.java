package cn.tanjianff.simpleemailapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WriteActivity extends AppCompatActivity {

    private Button Btn_cancelSend;
    private Button Btn_send;
    private AutoCompleteTextView  V_receiver;
    private AutoCompleteTextView V_subject;
    private EditText V_contentInfo;

    private String Strreceiver;
    private String Strsubject;
    private String StrcontentInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        Btn_cancelSend= (Button) findViewById(R.id.cancelSend);
        Btn_send= (Button) findViewById(R.id.send);
        Btn_send.setEnabled(false);
        V_receiver= (AutoCompleteTextView) findViewById(R.id.receiver);
        V_subject= (AutoCompleteTextView) findViewById(R.id.subject);
        V_contentInfo= (EditText) findViewById(R.id.contentInfo);

        new IsNullThread().start();

        Btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Strreceiver=V_receiver.getText().toString().trim();
                Strsubject=V_subject.getText().toString();
                StrcontentInfo=V_contentInfo.getText().toString();

                //TODO:绑定事件失效,待检查!
            }
        });

        Btn_cancelSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteActivity.this.finish();//通过通知程序此Activity已经完成,达到返回上一个Activity.
            }
        });
    }

    class IsNullThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                if(Strreceiver!=null){
                    Btn_send.setEnabled(true);
                }
            }
        }
    }
}
