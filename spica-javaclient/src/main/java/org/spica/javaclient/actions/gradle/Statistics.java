package org.spica.javaclient.actions.gradle;

import java.util.List;
import org.spica.javaclient.utils.LogUtil;

public class Statistics {

  public void show (final List<GradleLauncher> gradleLauncherList, long overallDuration) {


    System.out.println ();
    System.out.println ();
    System.out.println (LogUtil.green("STATISTICS"));
    System.out.println ();
    String header = String.format("  %-30s %10s   %20s ms", "Module", "ReturnCode", "Duration");
    System.out.println (LogUtil.green(header));


    long durationSum = 0;
    long numberErrors = 0;

    for (GradleLauncher next: gradleLauncherList) {
      if (next.getReturnCode() != 0)
        numberErrors ++;
      durationSum += next.getDuration();
      String statisticInfo = String.format("  %-30s %10d   %20d ms", next.getId(), next.getReturnCode(), next.getDuration());
      System.out.println (LogUtil.green(statisticInfo));
    }

    System.out.println ();
    if (numberErrors > 0)
      System.out.println (LogUtil.red("Number errors                    : " + numberErrors));

    System.out.println (LogUtil.green("Sum duration of gradle builds  : " + durationSum + "ms"));
    System.out.println (LogUtil.green("Overall duration               : " + overallDuration + "ms"));




  }
}
