package de.bit.pl2.p3;


import ij.ImagePlus;

import java.io.File;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // todo argparse
        boolean output = false;
        String path = "C:\\Users\\regin\\Desktop\\testset";
        File folder = new File(path);

        ImageReader imageReader = new ImageReader();
        List<ImagePlus> imageList = imageReader.readFilesFromFolder(folder);

        ObjectFinder objectFinder = new ObjectFinder();
        List<ImagePlus> objectList = objectFinder.parseList(imageList, output);

        if (output) {
            ImageSaver imageSaver = new ImageSaver();
            imageSaver.saveColorLabel(objectList, folder);
        }
        else {
            for (ImagePlus imp : objectList) {
                imp.show();
            }
        }
    }

}
