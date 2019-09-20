package org.spica.javaclient.links;

import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.LinkType;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LinkFinder {

    private ModelCache modelCache = new ModelCache();

    public List<LinkInfo> findMatchingLinks ( final TopicInfo topicInfo,
                                              final File currentDir) {
        List<LinkInfo> matchingLinks = new ArrayList<LinkInfo>();
        for (LinkInfo next: modelCache.getLinkInfos()) {
            if (next.getType().equals(LinkType.GENERAL))
                matchingLinks.add(next);
            else if (next.getType().equals(LinkType.PROJECT)) {
                if (topicInfo != null && topicInfo.getProject() != null) {
                    if (topicInfo.getProject().getId().equalsIgnoreCase(next.getReference()))
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

    public ModelCache getModelCache() {
        return modelCache;
    }

    public void setModelCache(ModelCache modelCache) {
        this.modelCache = modelCache;
    }
}
