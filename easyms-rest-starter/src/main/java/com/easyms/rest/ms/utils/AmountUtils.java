package com.easyms.rest.ms.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author khames.
 */
public class AmountUtils {

    private static final int DECIMALS = 2;

    public static String amountAsCents(BigDecimal amount) {
        return amount.multiply(new BigDecimal(100)).toBigInteger().toString();
    }

    public static BigDecimal rounded(String amount) {
        return rounded(new BigDecimal(amount));
    }

    public static BigDecimal rounded(BigDecimal amount) {
        return amount.setScale(DECIMALS, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal rounded(BigDecimal amount, int scale, RoundingMode roundingMode) {
        return amount.setScale(scale, roundingMode);
    }

    public static BigDecimal divide(BigDecimal b1, BigDecimal b2) {
        return rounded(b1.divide(b2, RoundingMode.HALF_EVEN));
    }

    public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale, RoundingMode roundingMode) {
        return rounded(b1.divide(b2, roundingMode), scale, roundingMode);
    }

}
