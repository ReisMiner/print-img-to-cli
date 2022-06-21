import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedImage img = null;
        File f;
        String lookup = " .,-:;/(*#$%&";

        if(args.length < 1){
            System.out.println("please specify an image path");
            return;
        }

        try {
            f = new File(args[0]);
            img = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        img = convertToGrayScale(img);
        int[] minMax = getMinMaxPixels(img);

        int height = img.getHeight();
        int width = img.getWidth();

        StringBuilder out = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (r + g + b) / 3;

                out.append(lookup.toCharArray()[avg / 20]).append(" ");
            }
            out.append("\n");
        }
        System.out.println(out.toString());

    }

    private static BufferedImage convertToGrayScale(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();

        BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // calculate average
                int avg = (r + g + b) / 3;

                // replace RGB value with avg
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                tmp.setRGB(x, y, p);
            }
        }
        return tmp;
    }

    private static int[] getMinMaxPixels(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();

        int[] minMax = new int[]{255, 0};

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (r + g + b) / 3;

                if (avg > minMax[1])
                    minMax[1] = avg;
                if (avg < minMax[0])
                    minMax[0] = avg;
            }
        }
        return minMax;
    }
}
