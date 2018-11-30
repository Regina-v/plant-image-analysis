package de.bit.pl2.p3;

import ij.ImagePlus;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImageReaderTest {

    @org.junit.jupiter.api.Test
    void filterFolder() {
        //given
        File folder = new File(this.getClass().getResource("/testset").getPath());
        //when
        ImageReader imageReader = new ImageReader();
        List<File> testList = imageReader.filterFolder(folder);
        //then
        assertEquals(5, testList.size());
    }

    @org.junit.jupiter.api.Test
    void readFilesFromFolder() {
        //given
        File folder = new File(this.getClass().getResource("/testset").getPath());
        //when
        ImageReader imageReader = new ImageReader();
        List<ImagePlus> testList = imageReader.readFilesFromFolder(folder);
        //then
        assertEquals("plant0002_rgb",testList.get(0).getShortTitle());
        assertEquals("plant001_rgb", testList.get(1).getShortTitle());
        assertEquals("plant027_rgb", testList.get(2).getShortTitle());
    }
}