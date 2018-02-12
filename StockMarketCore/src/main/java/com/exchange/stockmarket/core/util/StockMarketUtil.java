package com.exchange.stockmarket.core.util;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Harsh Gupta on {2/10/18}
 */
@NoArgsConstructor
public class StockMarketUtil {

    /**
     * Returns Current Time
     *
     * @return {@link Date}
     */
    public static Date getCurrentTime() {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        Calendar rightNow = Calendar.getInstance(timeZone);
        return rightNow.getTime();
    }

    /**
     * Warning : This method throws a Throwable object in case of an exception.
     * Sneaky throw avoid the explicit catching of exception at the callee.
     * Callee should be aware that both the classes should have a no args constructor.
     * Wrapper around BeanUtils class. Eradicates the need of creating an object from the callee.
     *
     * @param source
     * @param toClazz
     * @param ignoreProperties : List of properties to be ignore during conversion
     * @return {@link Object} of toClass
     */
    @SneakyThrows
    public static Object convert(Object source, Class toClazz, String... ignoreProperties) {
        Object toObject = toClazz.newInstance();
        BeanUtils.copyProperties(source, toObject, ignoreProperties);
        return toObject;
    }

}
