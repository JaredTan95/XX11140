package caculate;

import java.math.BigDecimal;

/**
 * Created by tanjian on 16/10/1.
 * 调用BigDecimal实现方法计算.
 */
public enum Counts {
    ADD, SUB, MULTIPLY, DIVIDE, MARK, ROOT;
    public String Values(String num1, String num2) {
        BigDecimal number1 = new BigDecimal(num1);
        BigDecimal number2 = new BigDecimal(num2);
        BigDecimal number = BigDecimal.valueOf(0);
        switch (this) {
            case ADD:
                number = number1.add(number2);
                break;
            case SUB:
                number = number1.subtract(number2);
                break;
            case MULTIPLY:
                number = number1.multiply(number2);
                break;
            case DIVIDE:
                number = number1.divide(number2,20,BigDecimal.ROUND_UP);
                break;

        }
        return number.stripTrailingZeros().toString();

    }
}
