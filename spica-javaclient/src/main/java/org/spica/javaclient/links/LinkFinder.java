package org.spica.javaclient.links;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.LinkType;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;

public class LinkFinder {

    private Model model = new Model();

    public List<LinkInfo> findMatchingLinks ( final Model model, final TaskInfo topicInfo,
                                              final File currentDir) {
        List<LinkInfo> matchingLinks = new ArrayList<LinkInfo>();
        for (LinkInfo next: model.getLinkInfos()) {
            if (next.getType().equals(LinkType.GENERAL))
                matchingLinks.add(next);
            else if (next.getType().equals(LinkType.PROJECT)) {
                if (topicInfo != null && topicInfo.getProjectId() != null) {
                    if (topicInfo.getProjectId().equalsIgnoreCase(next.getReference()))
                        matchingLinks.add(next);
                }
            }
            else if (next.getType().equals(LinkType.TOPIC)) {
                if (topicInfo != null && topicInfo.getId().equalsIgnoreCase(next.getReference()))
                    matchingLinks.add(next);
            }
            else if (next.getType().equals(LinkType.PATH)) {
                if (currentDir.getAbsolutePath().startsWith(next.getReference()))
                    matchingLinks.add(next);
            }
        }
        return matchingLinks;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
