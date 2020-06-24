package org.spica.commons.services.download;

import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.spica.commons.SpicaProperties;

public class DownloadService {

  public File download (final File localPath, final String name, final String url, final boolean executable) throws IOException {
    if (! localPath.exists())
      localPath.mkdirs();

    File toFile = new File (localPath, name);

    InputStream in = new URL(url).openStream();
    Files.copy(in, toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    if (executable)
      toFile.setExecutable(true);

    return toFile;
  }

  public String getDownloadedFileName (final String url) {
    return url.replace("/", "#");
  }

  public String getPageContent (final String url) throws IOException {
    return Resources.toString(new URL (url), Charset.defaultCharset());
  }

  public String getContent (final String url, final String searchKey) throws IOException {
    String content = getPageContent(url);
    Document doc = Jsoup.parse(content);
    String asString = doc.toString();
    int index = asString.indexOf(searchKey);
    if (index < 0)
      return "";
    else {
      return asString.substring(index);
    }
  }

  public List<String> getOwnTextOfTags (final String url, final String tag, final String attribute, final String filter) throws IOException {
    List<String> texts = new ArrayList<String>();
    String content = getPageContent(url);
    Document doc = Jsoup.parse(content);
    for (Element next: doc.getElementsByTag(tag)) {
      if (next.hasAttr(attribute)) {
        String unfiltered = next.ownText();
        if (filter == null || unfiltered.contains(filter))
          texts.add(unfiltered);
      }
    }

    return texts;
  }

  public List<String> getAttributeOfTags (final String url, final String tag, final String attribute, final String filter) throws IOException {
    List<String> attributes = new ArrayList<String>();
    String content = getPageContent(url);
    Document doc = Jsoup.parse(content);
    for (Element next: doc.getElementsByTag(tag)) {
      if (next.hasAttr(attribute)) {
        String unfiltered = next.attr(attribute);
        if (filter == null || unfiltered.contains(filter))
          attributes.add(unfiltered);
      }
    }

    return attributes;
  }

  public boolean siteChanged (final String url) throws IOException {
    File spicaHome = SpicaProperties.getSpicaHome();
    File siteChanged = new File (spicaHome, "siteChanged");
    File downloadedFile = new File (siteChanged, getDownloadedFileName(url));
    if (downloadedFile.exists()) {
      String newFilename = downloadedFile.getName() + "_NEW";
      File newDownloadedFile = download(siteChanged, newFilename, url, false);
      String newDownloadedFileContent = Files.readString(newDownloadedFile.toPath()).replaceAll("\\s+","");
      String downloadedFileContent = Files.readString(downloadedFile.toPath()).replaceAll("\\s+","");
      if (newDownloadedFileContent.equals(downloadedFileContent)) {
        newDownloadedFile.delete();
        return false;
      } else {
        newDownloadedFile.renameTo(downloadedFile);
        return true;
      }
    }
    else {
      download(siteChanged, downloadedFile.getName(), url, false);
      return true;
    }

  }
}
