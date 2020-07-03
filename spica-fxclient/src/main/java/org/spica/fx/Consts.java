package org.spica.fx;

import java.io.InputStream;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consts {

  private static final Logger LOGGER = LoggerFactory.getLogger(Consts.class);

  private static HashMap<ImageKey, Image> imagesCache = new HashMap<ImageKey, Image>();

  @Deprecated
  public final static int ICONSIZE_MENU = 50;

  public final static int ICON_SIZE_TOOLBAR = 20;
  public final static int ICON_SIZE_VERY_SMALL = 20;
  public final static int ICON_SIZE_SMALL = 30;
  public final static int ICON_SIZE_MEDIUM = 50;
  public final static int ICON_SIZE_LARGE = 70;


  public final static FontIcon createIcon (String name, int iconSize) {
    FontIcon fontIcon =  new FontIcon(name);
    fontIcon.setIconSize(iconSize);

    return fontIcon;
  }

  public final static FontIcon createIcon (String name, int iconSize, String style) {
    FontIcon fontIcon =  new FontIcon(name);
    fontIcon.setIconSize(iconSize);
    fontIcon.setIconColor(Color.web(style));
    return fontIcon;
  }

  public final static Image createImage(String name, int iconSize) {
    ImageView imageView = getOrLoadImage(new ImageKey(name, iconSize));
    return imageView.getImage();
  }
  public final static ImageView getOrLoadImage(final ImageKey imageKey) {
    Image cachedImage = imagesCache.get(imageKey);
    if (cachedImage == null) {
      if (LOGGER.isDebugEnabled())
        LOGGER.debug("create image " + imageKey.getName());
      InputStream inputStream = Consts.class.getResourceAsStream(imageKey.getName());
      if (inputStream == null)
        throw new IllegalStateException(
            "Could not load image '" + imageKey.getName() + "' with classloader " + Consts.class.getClassLoader());

      cachedImage = new Image(inputStream, imageKey.getIconSize(), imageKey.getIconSize(), true, true);
      imagesCache.put(imageKey, cachedImage);
    } else {
      if (LOGGER.isDebugEnabled())
        LOGGER.debug("load image " + imageKey.getName() + " from cache");
    }

    return new ImageView(cachedImage);

  }
}
