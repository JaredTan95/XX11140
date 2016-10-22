package cn.tanjianff.test2;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import caculate.Counts;

public class MainActivity extends AppCompatActivity {

    private TextView print;

    private static String firstNumber = "0";
    private static String secondNumber = "0";
    private static String sum = "0";
    private static int flag = 0;

    public Counts take = null;

    private int[] btnidTake = {R.id.btn_div, R.id.btn_mult, R.id.btn_sub,
            R.id.btn_add, R.id.btn_sum};

    private int[] btn_octto = {R.id.binary, R.id.otc, R.id.oct, R.id.hex};
    private Button[] btn_oct2orthers = new Button[btn_octto.length];

    private Button[] buttonTake = new Button[btnidTake.length];

    private int[] btnidNum = {R.id.btn_zero, R.id.btn_one, R.id.btn_two, R.id.btn_three,
            R.id.btn_four, R.id.btn_five, R.id.btn_six, R.id.btn_seven, R.id.btn_eight, R.id.btn_nine,
            R.id.btn_dot};
    private Button[] buttons = new Button[btnidNum.length];

    private int[] btncl = {R.id.btn_sq, R.id.btn_cleanone};
    private Button[] btcls = new Button[btncl.length];
    private GridLayout gly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mCurrentOrientation = getResources().getConfiguration().orientation;
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);

        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            //If current screen is landscape
            setContentView(R.layout.activity_main);

            /*由于横竖屏切换后,会渲染进制转换的button,因此,要考虑分情况绑定,如果在竖屏情况下绑定竖屏里面没有的
            * 视图,会出现空指针的情况,要注意*/
            Oct2others to = new Oct2others();
            for (int j = 0; j < btn_octto.length; j++) {
                btn_oct2orthers[j] = (Button) findViewById(btn_octto[j]);
                btn_oct2orthers[j].setOnClickListener(to);
            }
        }

        gly = (GridLayout) findViewById(R.id.gly);
        print = (TextView) findViewById(R.id.showWindow);
        GetNumber get = new GetNumber();
        Compute cm = new Compute();


        for (int i = 0; i < btnidNum.length; i++) {
            buttons[i] = (Button) findViewById(btnidNum[i]);
            buttons[i].setOnClickListener(get);
        }

        for (int i = 0; i < btnidTake.length; i++) {
            buttonTake[i] = (Button) findViewById(btnidTake[i]);
            buttonTake[i].setOnClickListener(cm);
        }

        Button eq = (Button) findViewById(R.id.btn_sum);


        eq.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    secondNumber = print.getText().toString();
                    if (take == Counts.DIVIDE && secondNumber.equals("0")) {
                        print.setText("error");
                    } else {
                        sum = take.Values(firstNumber, secondNumber);
                        firstNumber = sum;
                        secondNumber = "0";
                        print.setText(sum);
                        flag = 1;
                    }
                }
            }
        });

        Button clear = (Button) findViewById(R.id.clearAll);
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sum = "0";
                firstNumber = secondNumber = sum;
                print.setText(sum);
                flag = 0;
            }
        });
        for (int i = 0; i < btncl.length; i++) {
            btcls[i] = (Button) findViewById(btncl[i]);
            btcls[i].setOnClickListener(new OnTake());
        }
    }

    // 给 TextView赋值
    class GetNumber implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (flag == 1)
                sum = "0";
            if (sum.equals("0")) {
                print.setText("");
                sum = (v.getId() == R.id.btn_dot) ? "0" : "";
            }
            String txt = ((Button) v).getText().toString();
            boolean s = Pattern.matches("-*(\\d+).?(\\d)*", sum + txt);
            sum = s ? (sum + txt) : sum;
            print.setText(sum);
        }
    }

    // 根据条件计算
    class Compute implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            firstNumber = print.getText().toString();
            switch (arg0.getId()) {
                case R.id.btn_add:
                    take = Counts.ADD;
                    break;
                case R.id.btn_sub:
                    take = Counts.SUB;
                    break;
                case R.id.btn_mult:
                    take = Counts.MULTIPLY;
                    break;
                case R.id.btn_div:
                    take = Counts.DIVIDE;
                    break;
            }
            sum = "0";
            flag = 0;
        }
    }

    class OnTake implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sq:
                    Double numss = Math.sqrt(new BigDecimal(sum).doubleValue());
                    int stratindex = numss.toString().contains(".") ? numss.toString().indexOf(".") : 0;
                    sum = numss.toString().length() > 13 ? numss.toString().substring(0, 12 + stratindex) : numss.toString();
                    break;
                case R.id.btn_cleanone: {
                    if (sum.length() > 1) {
                        sum = sum.substring(0, sum.length() - 1);
                    } else {
                        sum = "0";
                    }
                }
                break;
            }
            print.setText(sum);
        }
    }

    class Oct2others implements OnClickListener {
        @Override
        public void onClick(View v) {
            String numstr=print.getText().toString();
            switch (v.getId()) {
                case R.id.binary:
                    Integer integer = Integer.valueOf(numstr);
                    sum = Integer.toBinaryString(integer);
                    break;
                case R.id.otc:
                    Integer integer1 = Integer.valueOf(numstr);
                    sum = Integer.toOctalString(integer1);
                    break;
                case R.id.oct:
                    sum = Integer.valueOf(numstr).toString();
                    break;
                case R.id.hex:
                    Integer integer2 = Integer.valueOf(numstr);
                    sum = Integer.toHexString(integer2);
                    break;
            }
            print.setText(sum);
        }
    }
}