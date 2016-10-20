/**
 * 
 */
package com.amvdce.algo001;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

/**
 * @author Francis
 *
 */
public class TestTableSeating extends TestCase {
	private static final double DELTA = 1e-9;
	private int numbTable; 
	private int[] probs;
	@Override
	protected void setUp() throws Exception {
		numbTable = 12;
		probs = new int[]{0,0,0,0,0,50,50};
	}
	
	public void testCountTrue() {
		TableSeating tS = new TableSeating();
		assertTrue(tS.countTrue(new boolean[]{true, true, false, false}) == 3);
	}
	
	public void testCanSit() {
		TableSeating tS = new TableSeating();
		boolean [] tables = new boolean[]{false, false, true, false, false, false, true, true, false, false, false, false};
		int pos = 3;
		int tabPerCust = 3;
		for (int i = 0; i < 3000; i++) {
			pos = i%11;
			assertTrue(tS.canSit(tables, pos, tabPerCust));
		}
	}
	
	public void testSitCust() {
		TableSeating tS = new TableSeating();
		boolean [] tables = new boolean[]{true, true, false, false, false};
		int pos = 2;
		int tabPerCust = 3;
		assertTrue(tS.countTrue(tS.sitCust(tables, pos, tabPerCust)) == 4);
	}
	
	public void testSitable() {
		TableSeating tS = new TableSeating();
		boolean [] tables = new boolean[]{false, false, false, false};
		int tabPerCust = 2;
		assertTrue(tS.countSitable(tables, tabPerCust) == 4);
	}
	
	public void testGetsitable() {
		TableSeating tS = new TableSeating();
		boolean [] tables = new boolean[]{false, false, false, false};
		List<Integer> list = Arrays.asList(0,1,3);
		int tabPerCust = 2;
		assertTrue(tS.getSitable(tables, tabPerCust).equals(new Vector<Integer>(list)));
	}
	
	public void testGetExpectedChild() {
		TableSeating tS = new TableSeating();
		boolean [] tables = new boolean[4];
		// first, work on 
		/*
		 * 	4
		 *	{0,100}
		 *	Returns: 3.3333333333333335
		 * prob[1]: 2 table per cust, output should be sum(4, 2, 4, 0) = 10/(4-1) 
		 */
		double expected = tS.getExpected(tables, 0, 2, null);
		assertEquals(expected, (double)(10.0/3), DELTA);
		
	}

	public void testGetExpected() {
		TableSeating tS = new TableSeating();
		
		int[] probs = new int[]{0,100};
		// first, work on 
		/*
		 * 	4
		 *	{0,100}
		 *	Returns: 3.3333333333333335
		 * prob[1]: 2 table per cust, output should be sum(4, 2, 4, 0) = 10/(4-1) 
		 */
		double expected = (double)(10.0/3);
		double actual = tS.getExpected(4,new int[]{100});
//		actual = tS.getExpected(4,probs);
//		assertEquals(actual, expected, DELTA);
//		probs = new int[]{0,0,0,0,0,50,50};
//		expected = tS.getExpected(5,probs);
//		assertEquals(expected, (double)(0.0), DELTA);
//		probs = new int[]{0,100};
//		actual = tS.getExpected(6,probs);
//		expected = (32.0/3 + 8 + 6 )/5;
//		assertEquals(actual, expected, DELTA);
		/*
		 * 	Main: output now at 52.28267852227195, tabPerCust=1
		 *  Main: output now at 105.54640601979484, tabPerCust=2
		 *  Main: output now at 161.11162023313688, tabPerCust=3
		 *  Main: output now at 219.58624350037366, tabPerCust=4
		 *  Main: output now at 282.0091821050748, tabPerCust=5
		 *  Main: output now at 348.9051713149552, tabPerCust=6
		 *  Main: output now at 421.2117757810553, tabPerCust=7
		 *  Main: output now at 499.3756319710553, tabPerCust=8
		 *  Main: output now at 584.2458929710554, tabPerCust=9
		 *  Main: output now at 676.2087929710553, tabPerCust=10
		 *  Main: output now at 787.1087929710553, tabPerCust=11
		 *  Final Output: 12=7.871087929710553
		 */
		probs = new int[]{9,9,9,9,9,9,9,9,9,9,10};
		expected = tS.getExpected(12,probs);
		assertEquals(expected, (double)(7.871087929710551), DELTA);
		
	}
	
	public void testMaxUnfilled() {
		TableSeating tS = new TableSeating();
		
		boolean[] tables = new boolean[]{false, false, true, false, false, false, true, true, false, false, false, false};
		int expected = 4;
		int actual = tS.maxFalse(tables);
		assertEquals(actual , expected);
	}
	
	public void testPrintTime() {
		Date start = new Date();
		System.out.print("Start - "+start);
		Date end = new Date();
		System.out.println("different: " + (end.getTime() - start.getTime()));
	}
}
