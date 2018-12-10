package de.bit.pl2.p3;

import ij.IJ;
import ij.ImagePlus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImageReader {
    /**
     * Filters a folder for files ending with "_rgb.png"
     * @param directory folder with images
     * @return file list which contains all files ending with "_rgb.png"
     */
    public List<File> filterFolder(File directory) {
        List<File> results = new ArrayList<>();
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && file.getName().toLowerCase().endsWith("_rgb.png")) {
                    results.add(file);
                }
            }
        }
        return results;
    }

    /**
     * Folters a filter for files ending with "_rgb.png" and reads Images into a List.
     * @param folder folder with images
     * @return ImageList with ImagePlus instances of the files
     */
    public List<ImagePlus> readFilesFromFolder(File folder) {
        System.out.println("***** Read in files *****");
        List<ImagePlus> imageList = new ArrayList<>();
        try {
            List<File> fileList = filterFolder(folder);
            if (fileList != null) {
                for (File file : fileList) {
                    ImagePlus imp = IJ.openImage(file.getCanonicalPath());
                    imageList.add(imp);
                }
            }

        } catch (IOException e) {
            e.getStackTrace();
        }
        System.out.println("***** Files successfully loaded ****");
        return imageList;
    }
}
