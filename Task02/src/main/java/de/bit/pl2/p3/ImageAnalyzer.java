package de.bit.pl2.p3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import ij.IJ;
import ij.ImagePlus;
import ij.measure.ResultsTable;
import ij.plugin.ChannelSplitter;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;

public class ImageAnalyzer {
	public ImageAnalyzer()
	{
		stringBuilder = new StringBuilder("image_ID" + SEPARATOR + "object_ID" + SEPARATOR + "size" + SEPARATOR + "pos_x" + SEPARATOR + "pos_y" + SEPARATOR + "roundness" + SEPARATOR + "brightness_sum_red" + SEPARATOR + "brightness_average_red" + SEPARATOR + "brightness_sum_green" + SEPARATOR + "brightness_average_green" + SEPARATOR + "brightness_sum_blue" + SEPARATOR + "brightness_average_blue" + SEPARATOR + "brightness_sum" + SEPARATOR + "brightness_average" + SEPARATOR + "width_to_height_ratio" + LINE_END); 
	}

	public void analyzeImage(ImagePlus originalImage, ImagePlus watershedImage)
	{
		// Create a custom RoiManager to prevent it automatically opening a window even in batch mode
		RoiManager rm = new RoiManager(true);
		ResultsTable resultsTable = new ResultsTable();
		ParticleAnalyzer.setResultsTable(resultsTable);
		ParticleAnalyzer.setRoiManager(rm);
		// Split the original image into RGB channels
		ImagePlus[] originalChannelImages = ChannelSplitter.split(originalImage);
		//IJ.debugMode = true;
		// TODO: On some computers we do have to invert, on others we don't..
		//IJ.run(watershedImage, "Invert LUT", "");
		// Perform the analysis
		IJ.run("Set Measurements...", "area mean standard modal min centroid perimeter bounding shape feret's integrated median display redirect=None decimal=3");
		IJ.run(watershedImage, "Analyze Particles...", "size=70-Infinity exclude clear add");
		// Clone the results table, because otherwise we would overwrite it with the other measurements we are doing
		//ResultsTable resultsTable = (ResultsTable)ResultsTable.getResultsTable().clone();
		// Iterate over all leafs and add the measured data to the CSV string
		for(int leafIndex = 0; leafIndex < resultsTable.size(); leafIndex++)
		{
			addValueToCSV(watershedImage.getShortTitle(), false);
			addValueToCSV(String.valueOf(leafIndex), false);
			addColumnValueToCSV(resultsTable, "Area", leafIndex, false);
			// X and Y coordinates are the coordinates of the centroid, measured from the top-left
			addColumnValueToCSV(resultsTable, "X", leafIndex, false);
			addColumnValueToCSV(resultsTable, "Y", leafIndex, false);
			addColumnValueToCSV(resultsTable, "Round", leafIndex, false);

			// Set measurements to measure only brightness-related values
			IJ.run("Set Measurements...", "mean integrated redirect=None decimal=3");

			// Measure for each channel individually and add to CSV
			measureLeafAndAddBrightnessDataToCSV(originalChannelImages[0], rm, leafIndex);
			measureLeafAndAddBrightnessDataToCSV(originalChannelImages[1], rm, leafIndex);
			measureLeafAndAddBrightnessDataToCSV(originalChannelImages[2], rm, leafIndex);
			// Measure also for the original image
			measureLeafAndAddBrightnessDataToCSV(originalImage, rm, leafIndex);
	
			// Calculate and add the width-to-height ratio
			double leafWidth = getDoubleColumnValue(resultsTable, "Width", leafIndex);
			double leafHeight = getDoubleColumnValue(resultsTable, "Height", leafIndex);
			double leafWidthToHeightRatio = leafWidth / leafHeight;
			addValueToCSV(String.valueOf(leafWidthToHeightRatio), true);
		}
	}

	private void measureLeafAndAddBrightnessDataToCSV(ImagePlus image, RoiManager rm, int leafIndex) 
	{
		// First actually measure
		measureLeafByChannel(image, leafIndex, rm);
		// Get the brightness sum and average from the result table
		double brightnessSum = getDoubleColumnValue(ResultsTable.getResultsTable(), "RawIntDen", 0);
		double brightnessAverage = getDoubleColumnValue(ResultsTable.getResultsTable(), "Mean", 0);
		// Add both to CSV
		addValueToCSV(String.valueOf(brightnessSum), false);
		addValueToCSV(String.valueOf(brightnessAverage), false);
	}

	private void measureLeafByChannel(ImagePlus channelImage, int leafIndex, RoiManager rm)
	{
		// Apply the selection for this leaf to the channel image
		rm.select(channelImage, leafIndex);
		// Do the measurement
		IJ.run(channelImage, "Measure", "");
	}
	
	public void writeResultsToCSVFile(File file)
	{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
        {
        	writer.write(stringBuilder.toString());
        } catch (IOException e) {
        	System.out.println("Could not write to CSV file: " + e.toString());
		}
	}

	private final StringBuilder stringBuilder;
	private final static String SEPARATOR = "|";
	private final static String LINE_END = "\n";

	private void addColumnValueToCSV(ResultsTable resultsTable, String columnName, int rowIndex, boolean isLastInRow)
	{
		String value = getStringColumnValue(resultsTable, columnName, rowIndex);
		addValueToCSV(value, isLastInRow);
	}

	private void addValueToCSV(String value, boolean isLastInRow)
	{
		stringBuilder.append(value + (isLastInRow ? LINE_END : SEPARATOR));
	}

	private String getStringColumnValue(ResultsTable resultsTable, String columnName, int rowIndex)
	{
		int columnIndex = resultsTable.getColumnIndex(columnName);
		return resultsTable.getStringValue(columnIndex, rowIndex);
	}

	private double getDoubleColumnValue(ResultsTable resultsTable, String columnName, int rowIndex)
	{
		int columnIndex = resultsTable.getColumnIndex(columnName);
		return resultsTable.getValueAsDouble(columnIndex, rowIndex);
	}
}
