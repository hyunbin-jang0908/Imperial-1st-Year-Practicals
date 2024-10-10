package picture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PictureProcessor {

  public static void main(String[] args) {
    String keyword = args[0];

    if (Objects.equals(keyword, "rotate")) {
      Picture pic = new Picture(args[2]);
      Picture result = pic.rotate(Integer.parseInt(args[1]));
      result.saveAs(args[3]);
    } else if (Objects.equals(keyword, "flip")) {
      Picture pic = new Picture(args[2]);
      pic.flip(args[1]);
      pic.saveAs(args[3]);
    } else if (Objects.equals(keyword, "blend")) {
      Picture pic = new Picture(args[1]);  // create Picture from input_1
      List<Picture> pics = new ArrayList<>();
      for (String path: Arrays.copyOfRange(args, 2, args.length - 1)) {
        pics.add(new Picture(path));
      }
      pic.blend(pics).saveAs(args[args.length - 1]);
    } else {
      Picture pic = new Picture(args[1]);
      if (Objects.equals(keyword, "invert")) pic.invert();
      else if (Objects.equals(keyword, "grayscale")) pic.grayScale();
      else if (Objects.equals(keyword, "blur")) pic.blur();
      pic.saveAs(args[2]);
    }
  }
}
