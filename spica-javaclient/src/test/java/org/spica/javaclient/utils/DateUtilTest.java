package org.spica.javaclient.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import org.spica.commons.DateUtil;

public class DateUtilTest {

    private DateUtil dateUtil = new DateUtil();

    private LocalDate today = LocalDate.now();

    @Test
    public void getDate4 () {
        LocalDate day = dateUtil.getDate("1208");

        Assert.assertEquals ("Day invalid", day.getDayOfMonth(), 12);
        Assert.assertEquals ("Month invalid", day.getMonthValue(), 8);
        Assert.assertEquals ("Year invalid", day.getYear(), today.getYear());
    }

    @Test
    public void getDateNormal () {
        LocalDate day = dateUtil.getDate("12.08");

        Assert.assertEquals ("Day invalid", day.getDayOfMonth(), 12);
        Assert.assertEquals ("Month invalid", day.getMonthValue(), 8);
        Assert.assertEquals ("Year invalid", day.getYear(), today.getYear());
    }

    @Test
    public void getLongFormat () {
        LocalDate day = dateUtil.getDate("12.08.2019");

        Assert.assertEquals ("Day invalid", day.getDayOfMonth(), 12);
        Assert.assertEquals ("Month invalid", day.getMonthValue(), 8);
        Assert.assertEquals ("Year invalid", day.getYear(), 2019);
    }

    @Test
    public void longFormatToDate () {
        String asString = dateUtil.getDateAsString(LocalDate.of(2021, 05, 30));
        Assert.assertEquals ("30.05.2021", asString);

    }


}
