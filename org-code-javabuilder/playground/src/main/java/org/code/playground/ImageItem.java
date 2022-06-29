package org.code.playground;

import java.io.FileNotFoundException;
import java.util.HashMap;
import org.code.protocol.ClientMessageDetailKeys;
import org.code.protocol.ContentManager;
import org.code.protocol.JavabuilderContext;

public class ImageItem extends Item {
  private final ContentManager contentManager;

  private int width;
  private String filename;

  /**
   * Creates an item that can be displayed in the Playground. An item consists of an image,
   * referenced by the name of the image file in the asset manager. The image for the item will be
   * scaled to fit the width and height provided, which may distort the image.
   *
   * @param filename the file name of the image for this item in the asset manager
   * @param x the distance, in pixels, from the left side of the board
   * @param y the distance, in pixels, from the top of the board
   * @param width the width of the item, in pixels
   * @param height the height of the item, in pixels
   * @throws FileNotFoundException if the file specified is not the in the asset manager
   */
  public ImageItem(String filename, int x, int y, int width, int height)
      throws FileNotFoundException {
    this(
        filename,
        x,
        y,
        width,
        height,
        JavabuilderContext.getInstance().getGlobalProtocol().getContentManager());
  }

  // Visible for testing only
  ImageItem(String filename, int x, int y, int width, int height, ContentManager contentManager)
      throws FileNotFoundException {
    super(x, y, height);

    this.contentManager = contentManager;
    this.contentManager.verifyAssetFilename(filename);

    this.width = width;
    this.filename = filename;
  }

  /**
   * Retrieve the filename for this item.
   *
   * @return the filename in the asset manager for the image associated with this item.
   */
  public String getFilename() {
    return this.filename;
  }

  /**
   * Sets the filename for this item.
   *
   * @param filename the filename in the asset manager for the image to associate with this item
   * @throws FileNotFoundException if the file specified is not the in the asset manager
   */
  public void setFilename(String filename) throws FileNotFoundException {
    this.contentManager.verifyAssetFilename(filename);
    this.filename = filename;
    this.sendChangeMessage(ClientMessageDetailKeys.FILENAME, filename);
  }

  /**
   * Set the width for the item.
   *
   * @param width the width of the item, in pixels
   */
  public void setWidth(int width) {
    this.width = width;
    this.sendChangeMessage(ClientMessageDetailKeys.WIDTH, Integer.toString(width));
  }

  /**
   * Get the width for the item.
   *
   * @return the width of the item, in pixels
   */
  public int getWidth() {
    return this.width;
  }

  @Override
  protected HashMap<String, String> getDetails() {
    HashMap<String, String> details = super.getDetails();
    details.put(ClientMessageDetailKeys.FILENAME, this.getFilename());
    details.put(ClientMessageDetailKeys.WIDTH, Integer.toString(this.getWidth()));
    return details;
  }
}
