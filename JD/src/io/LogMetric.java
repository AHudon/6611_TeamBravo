/**
 * Team Bravo, SOEN 6611 Winter 2014
 * LogMetric
 * @author Alexandre Hudon
 * @date March 31st, 2014
 * 
 * This class is used to log parsed metrics during a run into a file named MOODMetrics.txt
 */
package io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogMetric {

	
	private static final String fileName = "MOODMetrics.txt";
	
	public static void LogMetricEvent(String metric){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
		    out.println(getTimeStamp()+ " "+metric);
		}catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static String getTimeStamp(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("[dd-M-yyyy-hh:mm:ss]");
		String date = dateFormat.format(new Date()); 
		return date;
	}
	
	public static void CloseParsingSequence(){
		LogMetricEvent("-------END-------");
	}
	
	public static void StartParsingSequence(){
		LogMetricEvent("------START------");
	}
	
}
