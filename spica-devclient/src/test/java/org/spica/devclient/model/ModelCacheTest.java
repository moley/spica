package org.spica.devclient.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ModelCache;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class ModelCacheTest {

    @Test
    public void getEventInfosRealToday () {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minus(1, ChronoUnit.DAYS);
        EventInfo eventInfo1 = new EventInfo().start(yesterday);
        EventInfo eventInfo2 = new EventInfo().start(now);

        ModelCache modelCache = new ModelCache();
        modelCache.setEventInfosReal(Arrays.asList(eventInfo1, eventInfo2));
        List<EventInfo> eventInfosRealToday = modelCache.getEventInfosRealToday();
        Assert.assertEquals ("Number of todays events invalid", 1, eventInfosRealToday.size());
        Assert.assertEquals ("Wrong event filtered", eventInfo2, eventInfosRealToday.get(0));
    }
}
