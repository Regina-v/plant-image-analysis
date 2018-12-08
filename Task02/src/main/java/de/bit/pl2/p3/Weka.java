package de.bit.pl2.p3;

import hr.irb.fastRandomForest.FastRandomForest;
import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageConverter;
import trainableSegmentation.WekaSegmentation;
import trainableSegmentation.utils.Utils;
import weka.classifiers.meta.RandomSubSpace;

import java.awt.*;

public class Weka {
    public Weka() {

        // starting time
        Long startTime = System.currentTimeMillis();

        // read in images
        ImagePlus image = new ImagePlus("C:\\Users\\regin\\Desktop\\plant015_rgb.png");
        ImagePlus labels = new ImagePlus("C:\\Users\\regin\\Desktop\\plant015_fg.png");
        ImagePlus testImage = new ImagePlus("C:\\Users\\regin\\Desktop\\plant010_rgb.png");

        // create Weka segmentator
        WekaSegmentation seg = new WekaSegmentation(image);

        // classifier
        //RandomSubSpace rss = new RandomSubSpace();
        FastRandomForest rf = new FastRandomForest();  // in first tests, performs better than RandomSubSpace

        // set classifier
        seg.setClassifier(rf);

        // update classLabel
        seg.setClassLabels(new String[] {"background", "plant"});

        // selected attributes (image features)
        boolean[] enableFeatures = new boolean[]{
                true,   /* Gaussian_blur */
                true,   /* Sobel_filter */
                true,   /* Hessian */
                true,   /* Difference_of_gaussians */
                true,   /* Membrane_projections */
                false,  /* Variance */
                false,  /* Mean */
                false,  /* Minimum */
                false,  /* Maximum */
                false,  /* Median */
                false,  /* Anisotropic_diffusion */
                false,  /* Bilateral */
                false,  /* Lipschitz */
                false,  /* Kuwahara */
                false,  /* Gabor */
                false,  /* Derivatives */
                false,  /* Laplacian */
                false,  /* Structure */
                false,  /* Entropy */
                false   /* Neighbors */
        };

        // enable features in the segmentator
        seg.setEnabledFeatures(enableFeatures);

        // add labeled samples in a balanced and random way
        seg.addRandomBalancedBinaryData(image, labels, "plant", "background", 1000);

        // train classifier
        seg.trainClassifier();

        // apply trained classifier to test image and get probabilities
        ImagePlus result = seg.applyClassifier( testImage, 0, false );

        // assign same LUT as in GUI
        result.setLut( Utils.getGoldenAngleLUT() );
        ImageConverter imageConverter = new ImageConverter(result);
        imageConverter.convertToGray8();
        result.updateImage();
        IJ.run(result, "Make Binary", "white");
        IJ.run(result, "Invert", "");

        // save stuff
        seg.saveClassifier( "C:\\Users\\regin\\Desktop\\classifier.model" );
        seg.saveData( "C:\\Users\\regin\\Desktop\\data.arff" );
        FileSaver fs = new FileSaver(result);
        fs.saveAsPng("C:\\Users\\regin\\Desktop\\" + result.getTitle() + ".png");

        // print elapsed time
        Long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
        result.show();

    }
}
