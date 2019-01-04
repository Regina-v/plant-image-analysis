package de.bit.pl2.p3;

import ij.ImagePlus;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Options options = new Options();
        Option inputOption = new Option("i", "input", true, "input folder with RGB images");
        Option outputOption = new Option("o", "output", true, "output folder");

        options.addOption(inputOption);
        options.addOption(outputOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmdLine;

        try {
            cmdLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("--input [INPUT] --output [OUTPUT]", options);
            System.exit(1);
            return;
        }

        if (cmdLine.hasOption("i") && cmdLine.hasOption("o")) {
            File inputPath = new File(cmdLine.getOptionValue("input"));
            String outputPathBase = cmdLine.getOptionValue("output");

            // read images from a folder and store as ImagePlus objects in List
            ImageReader imageReader = new ImageReader();
            List<ImagePlus> inputImages = imageReader.readFilesFromFolder(inputPath);
            // Actually process the images
            processImages(inputImages, outputPathBase);

        } else {
            formatter.printHelp("--input [INPUT] --output [OUTPUT]:", options);
            System.exit(1);
        }
    }

    private static void processImages(List<ImagePlus> inputImages, String outputPathBase) {
        // Create the output folders from the base folder
        String outputPathObject = outputPathBase + File.separator + "images";
        try {
            Files.createDirectories(Paths.get(outputPathObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File outputPathObjectFile = new File(outputPathObject);

        // apply Weka (from resources) classifier to images in list
        WekaSeg weka = new WekaSeg();
        List<ImagePlus> classifiedImages = weka.applyClassifier(inputImages);

        // find objects with watershed
        ObjectFinder objectFinder = new ObjectFinder();
        List<ImagePlus> objectImages = objectFinder.parseList(classifiedImages);

        // Analyze images
        ObjectAnalyzer objectAnalyzer = new ObjectAnalyzer();
        objectAnalyzer.parseList(inputImages, objectImages, outputPathBase);

        // save images
        ImageSaver imageSaver = new ImageSaver();
        imageSaver.saveImages(objectImages, outputPathObjectFile);
    }
}
