package org.spica.devclient.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Created by OleyMa on 25.10.16.
 */
public class Consts {

  private static final Logger LOGGER = Logger.getLogger(Consts.class.getName());

  public final static int ICON_SIZE_SMALL = 30;
  public final static int ICON_SIZE_MEDIUM = 50;
  public final static int ICON_SIZE_LARGE = 70;

  public final static int DEFAULT_LISTVIEW_WIDTH = 450;
  public final static int DEFAULT_WIDTH = 1400;
  public final static int DEFAULT_HEIGHT = 800;


  public final static String ADONAI_HOME_PROP = "adonai.home";

  public final static String USER_HOME = System.getProperty(ADONAI_HOME_PROP) != null ? System.getProperty(ADONAI_HOME_PROP) : System.getProperty("user.home");
  public final static File LEGUAN_HOME = new File (USER_HOME, ".adonai");
  public final static File ADDITIONALS_PATH = new File (LEGUAN_HOME, "additionals");

  private static HashMap<ImageKey, Image> imagesCache = new HashMap<ImageKey, Image>();

  public final static ImageView createImageView (final String name, int iconSize) {
    ImageView imageView = getOrLoadImage(new ImageKey(name, iconSize));
    return imageView;
  }

  public final static Image createImage (String name, int iconSize) {
    ImageView imageView = getOrLoadImage(new ImageKey(name, iconSize));
    return imageView.getImage();
  }

  public final static ImageView getOrLoadImage (final ImageKey imageKey) {
    Image cachedImage = imagesCache.get(imageKey);
    if (cachedImage == null) {
      LOGGER.info("create image " + imageKey.getName());
      cachedImage = new Image("/icons/" + imageKey.getName() + ".png", imageKey.getIconSize(), imageKey.getIconSize(), true, true, true);
      imagesCache.put(imageKey, cachedImage);
    }
    else
      LOGGER.info("load image " + imageKey.getName() + " from cache");

    return new ImageView(cachedImage);

  }



}
