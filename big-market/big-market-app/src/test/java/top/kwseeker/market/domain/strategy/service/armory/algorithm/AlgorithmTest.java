package top.kwseeker.market.domain.strategy.service.armory.algorithm;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AlgorithmTest {

    @Test
    public void testDecimalConvert() {
        BigDecimal minAwardRate = new BigDecimal("0.12345");
        BigDecimal bigDecimal = BigDecimal.valueOf(minAwardRate.doubleValue());
        double convert = convert(bigDecimal);
        System.out.println(convert);
        convert = convert(new BigDecimal("0.0001"));
        System.out.println(convert);
    }

    protected double convert(BigDecimal min) {
        if (BigDecimal.ZERO.compareTo(min) == 0) return 1D;

        String minStr = min.toString();

        // 小数点前
        String beginVale = minStr.substring(0, minStr.indexOf("."));
        int beginLength = 0;
        if (Double.parseDouble(beginVale) > 0) {
            beginLength = minStr.substring(0, minStr.indexOf(".")).length();
        }

        // 小数点后
        String endValue = minStr.substring(minStr.indexOf(".") + 1);
        int endLength = 0;
        if (Double.parseDouble(endValue) > 0) {
            endLength = minStr.substring(minStr.indexOf(".") + 1).length();
        }

        return Math.pow(10, beginLength + endLength);
    }
}