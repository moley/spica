import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.spica.commons.DashboardItemType;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.services.ModelCacheService;

public class ModelCacheServiceTest {

  private ModelCacheService modelCacheService = new ModelCacheService();

  @Test
  public void closeEventDashboards () {
    Model model = new Model();
    EventInfo openEvent = new EventInfo().start(LocalDateTime.now()).id("open");
    EventInfo closedEvent = new EventInfo().start(LocalDateTime.now()).stop(LocalDateTime.now()).id("closed");

    DashboardItemInfo dashboardOpen = new DashboardItemInfo().itemType(DashboardItemType.EVENT.name()).itemReference("open");
    DashboardItemInfo dashboardClosed = new DashboardItemInfo().itemType(DashboardItemType.EVENT.name()).itemReference("closed");

    model.getEventInfosReal().addAll(Arrays.asList(openEvent, closedEvent));
    model.getDashboardItemInfos().addAll(Arrays.asList(dashboardOpen, dashboardClosed));

    modelCacheService.set(model, "");
    modelCacheService.closeEventDashboardsWhenEventIsClosed();
    Assert.assertFalse ("Closed event not closed at dashboard", dashboardClosed.isOpen());
    Assert.assertTrue ("Open event not open at dashboard", dashboardOpen.isOpen());

  }
}
