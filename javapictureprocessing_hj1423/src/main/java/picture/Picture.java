package picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A class that encapsulates and provides a simplified interface for manipulating an image. The
 * internal representation of the image is based on the RGB direct colour model.
 */
public class Picture {

  /**
   * The internal image representation of this picture.
   */
  private final BufferedImage image;

  /**
   * Construct a new (blank) Picture object with the specified width and height.
   */
  public Picture(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  /**
   * Construct a new Picture from the image data in the specified file.
   */
  public Picture(String filepath) {
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // Implementing codes from here
  public void invert() {
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        Color c = getPixel(x, y);
        int r = 255 - c.getRed();
        int b = 255 - c.getBlue();
        int g = 255 - c.getGreen();
        setPixel(x, y, new Color(r, g, b));
      }
    }
  }

  public void grayScale() {
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        Color c = getPixel(x, y);
        int avr = (c.getBlue() + c.getRed() + c.getGreen()) / 3;
        setPixel(x, y, new Color(avr, avr, avr));
      }
    }
  }

  public Picture rotate(int degree) {
    int width = image.getWidth();
    int height = image.getHeight();
    double midWidth = (double) image.getWidth() / 2 - 0.5;
    double midHeight = (double) image.getHeight() / 2 - 0.5;
    int newWidth = height;
    int newHeight = width;
    if (degree == 180) {
      newWidth = width;
      newHeight = height;
    }
    Picture newPic = new Picture(newWidth, newHeight);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        // make the midpoint to be (0,0) temporally
        Color c = getPixel(x, y);
        int newX = 0;
        int newY = 0;
        if (degree == 90) {
          newX = height - 1 - y;
          newY = x;
        } else if (degree == 180) {
          newX = width - 1 - x;
          newY = height - 1 - y;
        } else {
          newX = y;
          newY = width - 1 - x;
        }

        // Update pixel color in the new image
        newPic.setPixel(newX, newY, c);
        newPic.image.setRGB(newX, newY, image.getRGB(x, y));
      }
    }
    return newPic;
  }

  public void flip(String direction) {
    // direction = "H" -> horizontal, along y-axis
    // direction = "V -> vertical, along x-axis
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage newImage = new BufferedImage(width, height, image.getType());
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (direction == "H") {
          newImage.setRGB(width - 1 - x, y, image.getRGB(x, y));
        } else if (direction == "V") {
          newImage.setRGB(x, height - 1 - y, image.getRGB(x, y));
        }
      }
    }
    image.setData(newImage.getData());
  }

  public Picture blend(List<Picture> pictures) {
    int minWidth = image.getWidth();
    int minHeight = image.getHeight();
    // find the smallest width and height
    for (Picture picture : pictures) {
        if (picture.getWidth() < minWidth) minWidth = picture.getWidth();
        if (picture.getHeight() < minHeight) minHeight = picture.getHeight();
    }
    Picture newPic = new Picture(minWidth, minHeight);
    for (int x = 0; x < minWidth; x++) {
      for (int y = 0; y < minHeight; y++) {
        int sumR = getPixel(x, y).getRed();
        int sumG = getPixel(x, y).getGreen();
        int sumB = getPixel(x, y).getBlue();
        for (Picture picture : pictures) {
          sumR += picture.getPixel(x, y).getRed();
          sumG += picture.getPixel(x, y).getGreen();
          sumB += picture.getPixel(x, y).getBlue();
        }
        int pictureNum = pictures.size() + 1;
        Color c = new Color(sumR/pictureNum, sumG/pictureNum, sumB/pictureNum);
        newPic.setPixel(x, y, c);
      }
    }
    return newPic;
  }

  public void blur() {
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage newImage = new BufferedImage(width, height, image.getType());
    int[][] ds = {
            {-1, -1}, {0, -1}, {1, -1},
            {-1, 0}, {0, 0}, {1, 0},
            {-1, 1}, {0, 1}, {1, 1}
    };
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) { // boundary pixels
          newImage.setRGB(x, y, image.getRGB(x, y));
        } else {
          int sumR = 0;
          int sumG = 0;
          int sumB = 0;
          for (int i = 0; i < ds.length; i++) {
            sumR += getPixel(x + ds[i][0], y + ds[i][1]).getRed();
            sumG += getPixel(x + ds[i][0], y + ds[i][1]).getGreen();
            sumB += getPixel(x + ds[i][0], y + ds[i][1]).getBlue();
          }
          newImage.setRGB(
                  x,
                  y,
                  0xff000000
                          | (((0xff & sumR / 9) << 16)
                          | ((0xff & sumG / 9) << 8)
                          | (0xff & sumB / 9)));
        }
      }
    }
    image.setData(newImage.getData());
  }

  /**
   * Test if the specified point lies within the boundaries of this picture.
   *
   * @param x the x co-ordinate of the point
   * @param y the y co-ordinate of the point
   * @return <tt>true</tt> if the point lies within the boundaries of the picture, <tt>false</tt>
   * otherwise.
   */
  public boolean contains(int x, int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

  /**
   * Returns true if this Picture is graphically identical to the other one.
   *
   * @param other The other picture to compare to.
   * @return true iff this Picture is graphically identical to other.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof Picture)) {
      return false;
    }

    Picture otherPic = (Picture) other;

    if (image == null || otherPic.image == null) {
      return image == otherPic.image;
    }
    if (image.getWidth() != otherPic.image.getWidth()
        || image.getHeight() != otherPic.image.getHeight()) {
      return false;
    }

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getRGB(i, j) != otherPic.image.getRGB(i, j)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Return the height of the <tt>Picture</tt>.
   *
   * @return the height of this <tt>Picture</tt>.
   */
  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Return the colour components (red, green, then blue) of the pixel-value located at (x,y).
   *
   * @param x x-coordinate of the pixel value to return
   * @param y y-coordinate of the pixel value to return
   * @return the RGB components of the pixel-value located at (x,y).
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public Color getPixel(int x, int y) {
    int rgb = image.getRGB(x, y);
    return new Color((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
  }

  /**
   * Return the width of the <tt>Picture</tt>.
   *
   * @return the width of this <tt>Picture</tt>.
   */
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int hashCode() {
    if (image == null) {
      return -1;
    }
    int hashCode = 0;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        hashCode = 31 * hashCode + image.getRGB(i, j);
      }
    }
    return hashCode;
  }

  public void saveAs(String filepath) {
    try {
      ImageIO.write(image, "png", new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Update the pixel-value at the specified location.
   *
   * @param x   the x-coordinate of the pixel to be updated
   * @param y   the y-coordinate of the pixel to be updated
   * @param rgb the RGB components of the updated pixel-value
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public void setPixel(int x, int y, Color rgb) {

    image.setRGB(
        x,
        y,
        0xff000000
            | (((0xff & rgb.getRed()) << 16)
            | ((0xff & rgb.getGreen()) << 8)
            | (0xff & rgb.getBlue())));
  }

  /**
   * Returns a String representation of the RGB components of the picture.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        sb.append("(");
        sb.append(rgb.getRed());
        sb.append(",");
        sb.append(rgb.getGreen());
        sb.append(",");
        sb.append(rgb.getBlue());
        sb.append(")");
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }
}
