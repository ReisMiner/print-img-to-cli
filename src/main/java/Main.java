import org.apache.commons.cli.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws ParseException, IOException {
        BufferedImage img = null;
        File f;
        String lookup = " .,-:;/(*#$%&";

        Options options = new Options();

        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(false);
        options.addOption(output);

        Option space = new Option("s", "with-space", false, "try to counteract stretching by adding spaces");
        output.setRequired(false);
        options.addOption(space);

        Option userLookup = new Option("c", "characters", true, "define your own characters. the more to the right, the more its used for bright pixels.");
        output.setRequired(false);
        options.addOption(userLookup);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Image to Text", options);
            System.exit(1);
        }

        try {
            f = new File(cmd.getOptionValue("i"));
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
        int splitter = 20;
        if (cmd.hasOption("c")) {
            lookup = cmd.getOptionValue("c");
            splitter = (int) Math.ceil(256f/lookup.length());
        }

        //intentionally dupe code cuz don't need to compare every pixel if the space option is set
        if (cmd.hasOption("s")) {
            for (int a : avgs) {
                if (a == -69)
                    out.append("\n");
                else
                    out.append(lookup.toCharArray()[a / splitter]).append(" ");
            }
        } else {
            for (int a : avgs) {
                if (a == -69)
                    out.append("\n");
                else
                    out.append(lookup.toCharArray()[a / splitter]);
            }
        }


        if (cmd.hasOption("o")) {
            File tmp = new File(cmd.getOptionValue("o"));
            FileWriter fw = new FileWriter(tmp);
            fw.write(out.toString());
            fw.close();
            System.out.println("Created and wrote to file: " + tmp.getPath());
        } else {
            System.out.println(out);
        }
    }
}
