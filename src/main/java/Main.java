import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        BufferedImage img = null;
        File f;
        String lookup = " .,-:;/(*#$%&";

        if (args.length < 1) {
            System.out.println("please specify an image path");
            return;
        }

        try {
            f = new File(args[0]);
            img = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int[] minMax = {255, 0};
        ArrayList<Integer> avgs = new ArrayList<>();

        int height = img.getHeight();
        int width = img.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // calculate average
                int avg = (r + g + b) / 3;

                if (avg > minMax[1])
                    minMax[1] = avg;
                if (avg < minMax[0])
                    minMax[0] = avg;

                avgs.add(avg);
            }
            avgs.add(-69);
        }


        StringBuilder out = new StringBuilder();

        for (int a : avgs) {
            if (a == -69)
                out.append("\n");
            else
                out.append(lookup.toCharArray()[a / 20]).append(" ");
        }

        System.out.println(out);

    }
}
