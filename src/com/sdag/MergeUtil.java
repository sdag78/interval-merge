package com.sdag;

import java.util.ArrayList;
import java.util.List;

public class MergeUtil {

	// Input: [25,30] [2,19] [14, 23] [4,8]
	public static List<Interval> parseIntervalsString(String inputStr) throws BadInputStringException {

		if (!inputStr.contains("[")) {
			throw new BadInputStringException();
		}
		if (countElements(inputStr, '[') != countElements(inputStr, ']')) {
			throw new BadInputStringException();
		}
		
		try {
			List<Interval> output = new ArrayList<Interval>(); // could use a set to ensure no duplicates? No requirements about that.
	
			// pre-process / clean the data: remove "comma space", replace with just "comma"
			inputStr = cleanInputString(inputStr);
	
			// parse the elements:
			// 1. split on space
			String[] splits = inputStr.split(" ");
			
			for (String split : splits) {
				
				// 2. strip brackets
				int beginIndex = 1, endIndex = split.length()-1;
				String substring = split.substring(beginIndex, endIndex);
				
				// 3. split on comma
				String[] intervals = substring.split(",");
				
				// 4. convert strings to int
				
				int min = Integer.parseInt(intervals[0]);
				int max = Integer.parseInt(intervals[1]);
				
				// 5. create Interval object and add to list
				output.add(new Interval(min, max));
			}
			return output;
		}
		
		catch(ArrayIndexOutOfBoundsException e) {
			throw new BadInputStringException();
		}
		catch(NumberFormatException e) {
			throw new BadInputStringException();
		}
	}

	private static int countElements(String inputStr, char chr) {
		int count =0;
		for (int i=0; i<inputStr.length(); i++) {
			if (inputStr.charAt(i) == chr) {
				count += 1;
			}
		}
		return count;
	}

	public static String cleanInputString(String inputStr) {
		inputStr = inputStr.replaceAll(", ", ",");
		return inputStr;
	}

	public static List<Interval> mergeIntervals(List<Interval> intervals) {
		List<Interval> outputIntervals = new ArrayList<Interval>(); // could use a set to ensure no duplicates
		for (Interval interval : intervals) {
			outputIntervals = doMerge(outputIntervals, interval);
		}
		return outputIntervals;
	}
	
	private static List<Interval> doMerge(List<Interval> outputIntervals, Interval interval) {
		// find the intervals in the output list which overlap (can be multiple), remove them from the list and replace with a new interval covering the full range
		List<Interval> newOutputIntervals = new ArrayList<Interval>();
		//getOverlaps
		List<Interval> toMerge = new ArrayList<Interval>();
		for (Interval thisInterval : outputIntervals) {
			if (overlaps(thisInterval, interval)) {
				toMerge.add(thisInterval);
				toMerge.add(interval);
			} else {
				// to avoid concurrent access error when: outputIntervals.remove(thisInterval);
				// create a newOutputIntervals list
				newOutputIntervals.add(thisInterval);
			}
		}
		// if no overlapping intervals are found, then add the interval to the list
		if (toMerge.size() == 0) {
			newOutputIntervals.add(interval);
		} else {
			// merge all intervals in the toMerge list and add the resulting interval to the intervals list.
			Interval newInterval = merge(toMerge);
			newOutputIntervals.add(newInterval);
		}
		return newOutputIntervals;
	}

	public static boolean overlaps(Interval i1, Interval i2) {
		int min1 = i1.getMin(), max1 = i1.getMax();
		int min2 = i2.getMin(), max2 = i2.getMax();
		boolean i1overlapsi2 = (min1 >= min2 && min1 <= max2) || (max1 >= min2 && max1 <= max2);
		boolean i2overlapsi1 = (min2 >= min1 && min2 <= max1) || (max2 >= min1 && max2 <= max1);
		return i1overlapsi2 || i2overlapsi1;
	}

	private static Interval merge(List<Interval> toMerge) {
		// find the smallest min and the largest max, and return these as a new interval
		int min = toMerge.get(0).getMin();
		int max = toMerge.get(0).getMax();
		for (Interval thisInterval : toMerge) {
			if (thisInterval.getMin() < min) min = thisInterval.getMin();
			if (thisInterval.getMax() > max) max = thisInterval.getMax();
		}
		return new Interval(min, max);
	}

	public static String intervalsToString(List<Interval> mergedIntervals) {
		ArrayList<String> als = new ArrayList<String>();
		for(Interval interval : mergedIntervals) {
			als.add(interval.toString());
		}
		return String.join(" ", als);
	}

	public static String doIntervalMerge(String inputStr) throws BadInputStringException {
		List<Interval> intervals = MergeUtil.parseIntervalsString(inputStr);
		List<Interval> mergedIntervals = MergeUtil.mergeIntervals(intervals);
		return MergeUtil.intervalsToString(mergedIntervals);
	}
}

