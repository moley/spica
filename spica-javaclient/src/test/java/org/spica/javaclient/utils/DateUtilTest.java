package org.spica.javaclient.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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


}
