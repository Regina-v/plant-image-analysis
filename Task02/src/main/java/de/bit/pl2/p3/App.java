package de.bit.pl2.p3;

import ij.ImagePlus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException 
    {
    	if(args.length < 2)
    	{
    		System.out.println("Error: Missing parameter");
    		System.out.println("Usage: App <inputFolder> <outputFolder>");
    		return;
    	}
    	
        //String inputDir = "C:\\Users\\regin\\Desktop\\2017\\A2";
        //String outputDir = "C:\\Users\\regin\\Desktop\\outputA1_";
    	//Regina's path
        //String[] input = new String[] {"C:\\Users\\regin\\Desktop\\2017\\A1", "C:\\Users\\regin\\Desktop\\2017\\A2"};
        //String[] classOutput = new String[] {"C:\\Users\\regin\\Desktop\\outputA1_class", "C:\\Users\\regin\\Desktop\\outputA2_class"};
        //String[] objectOutput = new String[] {"C:\\Users\\regin\\Desktop\\outputA1_objects", "C:\\Users\\regin\\Desktop\\outputA2_objects"};
        
        //Francel's path
        //String[] input = new String[] {"C:\\Users\\Spokie\\Documents\\Plant_project\\A1", "C:\\Users\\Spokie\\Documents\\Plant_project\\A2"};
        //String[] classOutput = new String[] {"C:\\Users\\Spokie\\Documents\\Plant_project\\outputA1_class", "C:\\Users\\Spokie\\Documents\\Plant_project\\outputA2_class"};
        //String[] objectOutput = new String[] {"C:\\Users\\Spokie\\Documents\\Plant_project\\outputA1_objects", "C:\\Users\\Spokie\\Documents\\Plant_project\\outputA2_objects"};
        
//        String[] input = new String[] {"C:\\Users\\Spokie\\Documents\\Plant_project\\Test"};
//        String[] classOutput = new String[] {"C:\\Users\\Spokie\\Documents\\Plant_project\\output_test_class"};
//        String[] objectOutput = new String[] {"C:\\Users\\Spokie\\Documents\\Plant_project\\output_test_object"};

        File inputPath = new File(args[0]);
        String outputPathBase = args[1];

        // read images from a folder and store as ImagePlus objects in List
        ImageReader imageReader = new ImageReader();
        List<ImagePlus> inputImages = imageReader.readFilesFromFolder(inputPath);
        // Actually process the images
        processImages(inputImages, outputPathBase);

        // Single Image Test
//        ImagePlus img = IJ.openImage("C:\\Users\\regin\\Desktop\\plant031_rgb_class.png");
//        ObjectFinder objectFinder = new ObjectFinder();
//        ImagePlus output = objectFinder.removeOutliers(img);
//        output.show();
    }

    private static void processImages(List<ImagePlus> inputImages, String outputPathBase) throws IOException
    {
    	// Create the output folders from the base folder
    	String outputPathClass = outputPathBase + File.separator + "class";
    	String outputPathObject = outputPathBase + File.separator + "object";
    	Files.createDirectories(Paths.get(outputPathClass));
    	Files.createDirectories(Paths.get(outputPathObject));
    	File outputPathClassFile = new File(outputPathClass);
    	File outputPathObjectFile = new File(outputPathObject);

        // apply Weka (from resources) classifier to images in list
        WekaSeg weka = new WekaSeg();
        List<ImagePlus> classifiedImages = weka.applyClassifier(inputImages);

        // find objects with watershed
        ObjectFinder objectFinder = new ObjectFinder();
        List<ImagePlus> objectImages = objectFinder.parseList(classifiedImages);

        // save images
        ImageSaver imageSaver = new ImageSaver();
        imageSaver.saveImages(classifiedImages, outputPathClassFile);
        imageSaver.saveImages(objectImages, outputPathObjectFile);
        
        // Analyze images
        ImageAnalyzer imageAnalyzer = new ImageAnalyzer();
        for(int i = 0; i < inputImages.size(); i++)
        {
        	ImagePlus inputImage = inputImages.get(i);
        	ImagePlus watershedImage = objectImages.get(i);
        	imageAnalyzer.analyzeImage(inputImage, watershedImage);
        }
        imageAnalyzer.writeResultsToCSVFile(new File(outputPathBase + File.separator + "results.csv"));
    }
}
