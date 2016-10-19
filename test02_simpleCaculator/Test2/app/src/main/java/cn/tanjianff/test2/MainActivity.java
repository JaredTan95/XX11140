package cn.tanjianff.test2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            R.id.btn_add,R.id.btn_sum};

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
        setContentView(R.layout.activity_main);
        gly = (GridLayout) findViewById(R.id.gly);
        print = (TextView) findViewById(R.id.showWindow);
        GetNumber get = new GetNumber();

        for (int i = 0; i < btnidNum.length; i++) {
            buttons[i] = (Button) findViewById(btnidNum[i]);
            buttons[i].setOnClickListener(get);
        }

        Compute cm = new Compute();

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

        Button cleargo = (Button) findViewById(R.id.btn_cleanone);
        cleargo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sum.length() > 1) {
                    sum = sum.substring(0, sum.length() - 1);
                } else {
                    sum = "0";
                }
                print.setText(sum.toString());
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
                sum = (v.getId()==R.id.btn_dot)? "0" : "";
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
               /* case R.id.btn_cleanone:
                    sum = new BigDecimal(sum).divide(BigDecimal.valueOf(100), 12, BigDecimal.ROUND_UP).stripTrailingZeros()
                            .toString();
                    break;*/
            }
            print.setText(sum);
            flag = 0;
            sum = "0";
        }
    }
}