package de.bit.pl2.p3;

import ij.IJ;
import ij.ImagePlus;

import java.io.File;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // note all that hardcoded stuff needs to be changed into CLI in the end
        //String inputDir = "C:\\Users\\regin\\Desktop\\2017\\A2";
        //String outputDir = "C:\\Users\\regin\\Desktop\\outputA1_";
        String[] input = new String[] {"C:\\Users\\regin\\Desktop\\2017\\A1", "C:\\Users\\regin\\Desktop\\2017\\A2"};
        String[] classOutput = new String[] {"C:\\Users\\regin\\Desktop\\outputA1_class", "C:\\Users\\regin\\Desktop\\outputA2_class"};
        String[] objectOutput = new String[] {"C:\\Users\\regin\\Desktop\\outputA1_objects", "C:\\Users\\regin\\Desktop\\outputA2_objects"};

        for (int i = 0; i < 2; i++) {
            // read images from a folder and store as ImagePlus objects in List
            ImageReader imageReader = new ImageReader();
            List<ImagePlus> imageList = imageReader.readFilesFromFolder(new File(input[i]));

            // apply Weka (from resources) classifier to images in list
            WekaSeg weka = new WekaSeg();
            List<ImagePlus> resultList = weka.applyClassifier(imageList);

            // find objects with watershed
            ObjectFinder objectFinder = new ObjectFinder();
            List<ImagePlus> objectList = objectFinder.parseList(imageList);

            // save images
            ImageSaver imageSaver = new ImageSaver();
            imageSaver.saveImages(resultList, new File(classOutput[i]));
            imageSaver.saveImages(objectList, new File(objectOutput[i]));
        }

        // Single Image Test
//        ImagePlus img = IJ.openImage("C:\\Users\\regin\\Desktop\\plant031_rgb_class.png");
//        ObjectFinder objectFinder = new ObjectFinder();
//        ImagePlus output = objectFinder.removeOutliers(img);
//        output.show();

    }
}
