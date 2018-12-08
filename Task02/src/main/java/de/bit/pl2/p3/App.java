package de.bit.pl2.p3;


import hr.irb.fastRandomForest.FastRandomForest;
import ij.ImagePlus;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ij.process.ImageConverter;
import inra.ijpb.plugins.LabelingPlugin;
import trainableSegmentation.WekaSegmentation;
import trainableSegmentation.utils.Utils;
import weka.classifiers.meta.RandomSubSpace;

public class App {
    public static void main(String[] args) {

        Weka weka = new Weka();





//        boolean output = false;
//        String path = "C:\\Users\\regin\\Desktop\\testset";
//        File folder = new File(path);
//
//        ImageReader imageReader = new ImageReader();
//        List<ImagePlus> imageList = imageReader.readFilesFromFolder(folder);
//
//        ObjectFinder objectFinder = new ObjectFinder();
//        List<ImagePlus> objectList = objectFinder.parseList(imageList, output);
//
//        if (output) {
//            ImageSaver imageSaver = new ImageSaver();
//            imageSaver.saveColorLabel(objectList, folder);
//        }
//        else {
//            for (ImagePlus imp : objectList) {
//                imp.show();
//            }
//        }


    }

}
