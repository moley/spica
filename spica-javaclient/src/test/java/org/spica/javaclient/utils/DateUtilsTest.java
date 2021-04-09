package org.spica.javaclient.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import org.spica.commons.DateUtils;

public class DateUtilsTest {

    private DateUtils dateUtils = new DateUtils();

    private LocalDate today = LocalDate.now();

    @Test
    public void getDate4 () {
        LocalDate day = dateUtils.getDate("1208");

        Assert.assertEquals ("Day invalid", day.getDayOfMonth(), 12);
        Assert.assertEquals ("Month invalid", day.getMonthValue(), 8);
        Assert.assertEquals ("Year invalid", day.getYear(), today.getYear());
    }

    @Test
    public void getDateNormal () {
        LocalDate day = dateUtils.getDate("12.08");

        Assert.assertEquals ("Day invalid", day.getDayOfMonth(), 12);
        Assert.assertEquals ("Month invalid", day.getMonthValue(), 8);
        Assert.assertEquals ("Year invalid", day.getYear(), today.getYear());
    }

    @Test
    public void getLongFormat () {
        LocalDate day = dateUtils.getDate("12.08.2019");
        System.out.println (day);

        Assert.assertEquals ("Day invalid", day.getDayOfMonth(), 12);
        Assert.assertEquals ("Month invalid", day.getMonthValue(), 8);
        Assert.assertEquals ("Year invalid", day.getYear(), 2019);
    }

    @Test
    public void getLongFormat2 () {
        LocalDate day = dateUtils.getDate("30.05.2021");
        Assert.assertEquals ("Day invalid", day.getDayOfMonth(), 30);
        Assert.assertEquals ("Month invalid", day.getMonthValue(), 5);
        Assert.assertEquals ("Year invalid", day.getYear(), 2021);
    }

    @Test
    public void asString () {
        String asString = dateUtils.getDateAsString(LocalDate.of(2021, 05, 30));
        Assert.assertEquals ("30.05.21", asString);

    }

    @Test
    public void asStringLongFormat () {
        String asString = dateUtils.getDateAsStringLongFormat(LocalDate.of(2021, 05, 30));
        Assert.assertEquals ("30.05.2021", asString);

    }


}
