package com.sdag;

public class DoIntervalMerge {

	public static void main(String[] args) {
		// loop will be terminated when user types "exit"
		while (true) {
			String inputStr = System.console().readLine("Enter intervals, or type \"exit\": ");
			if (inputStr.equalsIgnoreCase("exit")) {
				System.out.println("..exiting");
				System.exit(0);
			} else {
				String mergedIntervalsString;
				try {
					mergedIntervalsString = MergeUtil.doIntervalMerge(inputStr);
				} catch (BadInputStringException e) {
					System.out.println("Bad input string: " + inputStr);
					System.out.println();
					continue;
				}
				System.out.print("merged intervals: ");
				System.out.println(mergedIntervalsString);
				System.out.println();
			}
		}
	}

}
