package test.com.sdag;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.sdag.Interval;
import com.sdag.MergeUtil;
import com.sdag.BadInputStringException;

public class TestMerge {

	@Test(expected = BadInputStringException.class)  
	public void testParseBadInput() throws BadInputStringException {
		String inputStr = "[25, 30] [2,19] [14, 23] [4,8]]";
		MergeUtil.parseIntervalsString(inputStr);
	}
	
	@Test
	public void testParseInput() throws BadInputStringException {
		String inputStr = "[25, 30] [2,19] [14, 23] [4,8]";
		List<Interval> intervals = MergeUtil.parseIntervalsString(inputStr);
		Assert.assertEquals(4, intervals.size());
		
		Assert.assertEquals(25, intervals.get(0).getMin());
		Assert.assertEquals(30, intervals.get(0).getMax());
		
		Assert.assertEquals(2, intervals.get(1).getMin());
		Assert.assertEquals(19, intervals.get(1).getMax());
		
		Assert.assertEquals(14, intervals.get(2).getMin());
		Assert.assertEquals(23, intervals.get(2).getMax());
		
		Assert.assertEquals(4, intervals.get(3).getMin());
		Assert.assertEquals(8, intervals.get(3).getMax());
	}
	
	@Test
	public void testMerge() throws BadInputStringException {
		String inputStr = "[25,30] [2,19] [14, 23] [4,8]";
		List<Interval> intervals = MergeUtil.parseIntervalsString(inputStr);
		List<Interval> mergedIntervals = MergeUtil.mergeIntervals(intervals);
		
		//expected output: [2,23] [25,30]
		
		Assert.assertEquals(2, mergedIntervals.size());
		
		//order was not as expected, so created mergedIntervalsContains method
		Assert.assertTrue(mergedIntervalsContains(mergedIntervals, new Interval(2,23)));
		Assert.assertTrue(mergedIntervalsContains(mergedIntervals, new Interval(25,30)));
	}
	
	@Test
	public void testStringClean() {
		String inputStr      = "[25,30] [2,19] [14, 23] [4,8]";
		String inputStrClean = "[25,30] [2,19] [14,23] [4,8]";
		Assert.assertEquals(MergeUtil.cleanInputString(inputStr), inputStrClean);
	}
	
	@Test
	public void testStringConversion() throws BadInputStringException {
		String inputStrClean = MergeUtil.cleanInputString("[25,30] [2,19] [14, 23] [4,8]");
		List<Interval> intervals = MergeUtil.parseIntervalsString(inputStrClean);
		String outputStr = MergeUtil.intervalsToString(intervals);
		Assert.assertEquals(inputStrClean.length(), outputStr.length());
		Assert.assertEquals(inputStrClean, outputStr);
	}

	private boolean mergedIntervalsContains(List<Interval> mergedIntervals, Interval testInterval) {
		for (Interval interval : mergedIntervals) {
			if (interval.matches(testInterval)) return true;
		}
		return false;
	}
	
	@ParameterizedTest
	@MethodSource("provideIntervalsForTestOverlaps")
	void testOverlaps(Interval i1, Interval i2, boolean expected) {
	    Assert.assertEquals(expected, MergeUtil.overlaps(i1, i2));
	}
	
	private static Stream<Arguments> provideIntervalsForTestOverlaps() {
	    return Stream.of(
			Arguments.of(new Interval(10,20), new Interval(20,25), true),
			Arguments.of(new Interval(10,20), new Interval(0,10), true),
			Arguments.of(new Interval(10,20), new Interval(10,11), true),
			Arguments.of(new Interval(10,20), new Interval(9,10), true),
			Arguments.of(new Interval(10,20), new Interval(9,11), true),
			Arguments.of(new Interval(10,20), new Interval(19,21), true),
			Arguments.of(new Interval(10,20), new Interval(20,21), true),
			
			Arguments.of(new Interval(10,20), new Interval(21,22), false),
			Arguments.of(new Interval(10,20), new Interval(22,23), false),
			Arguments.of(new Interval(10,20), new Interval(8,9), false),
			Arguments.of(new Interval(10,20), new Interval(7,8), false)
			
	    );
	}
	
	@ParameterizedTest
	@MethodSource("provideStringsForTestMerges")
	void testMerges(String inputStr, String expectedStr) throws BadInputStringException {
		List<Interval> intervals = MergeUtil.parseIntervalsString(inputStr);
		List<Interval> expectedIntervals = MergeUtil.parseIntervalsString(expectedStr);
		List<Interval> mergedIntervals = MergeUtil.mergeIntervals(intervals);
		
		Assert.assertEquals(expectedIntervals.size(), mergedIntervals.size());
		
		for(Interval expectedInterval : expectedIntervals) {
			Assert.assertTrue(mergedIntervalsContains(mergedIntervals, expectedInterval));
		}
	}
	
	private static Stream<Arguments> provideStringsForTestMerges() {
	    return Stream.of(
    		Arguments.of("[25,30] [2,19] [14, 23] [4,8]", "[2,23] [25,30]"),
    		Arguments.of("[25,30] [2,19] [14, 23] [4,8] [40,50]", "[2,23] [25,30] [40,50]")
	    );
	}

}
