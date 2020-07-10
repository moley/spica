package org.spica.javaclient.actions.workingsets.template;

import java.util.List;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;

public interface InitializeFrom {

  List<WorkingSetSourcePartInfo> initialize (String fromUrl, final String branch);
}
