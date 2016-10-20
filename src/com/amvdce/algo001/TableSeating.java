/**
 * 
 */
package com.amvdce.algo001;

import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Francis
 *
 */
/**
 * @author Francis
 *
 */
public class TableSeating {
	private int[] probs;
	
	/**
	 * find the max number of contiguous unfilled tables
	 * @param arr
	 * @return
	 */
	protected int maxFalse(boolean[] arr) {
		int max = 0;
		int streak = 0;
		for (boolean i:arr) {
			if (!i) {
				streak++;
			} 
			max = streak > max?streak:max;
			if (i) streak = 0;
		}
		return max;
	}
	
	/**
	 * Count the number of unfilled tables
	 * @param arr
	 * @return
	 */
	protected int countFalse(boolean[] arr) {
		int output = 0;
		for (boolean i:arr) {
			if (!i) output++;
		}
		return output;
	}
	
	/**
	 * Count the number of filled tables
	 * @param arr
	 * @return
	 */
	protected int countTrue(boolean[] arr) {
		int output = 0;
		for (boolean i:arr) {
			if (i) output++;
		}
		return output;
	}
	
	/**
	 * Check if the customer can be seated
	 * @param tables
	 * @param pos
	 * @param tabPerCust
	 * @return
	 */
	protected boolean canSit(boolean[] tables, int pos, int tabPerCust) {
		if (pos + tabPerCust > tables.length) return false;
		for (int i = pos; i < pos+tabPerCust; i++) {
			if (tables[i]) return false;
		}
		return true;		
	}
	
//	protected boolean canSit(boolean[] tables, int pos, int tabPerCust) {
//		if (pos + tabPerCust > tables.length) return false;
//		boolean[] tableSub = Arrays.copyOfRange(tables, pos, pos+tabPerCust);
//		if (countFalse(tableSub) < tableSub.length) return false;
//		return true;		
//	}
	
	
	/**
	 * Count how many sits are available for the current per cust size 
	 * @param tables
	 * @param tabPerCust
	 * @return
	 */
	protected int countSitable(boolean[] tables, int tabPerCust) {
		int count = 0;
		for (int i = 0; i < tables.length; i++ ){
			if (canSit(tables, i, tabPerCust)) count++;
		}
		return count;		
	}
	
	/**
	 * Return all the available table numbers 
	 * @param tables
	 * @param tabPerCust
	 * @return
	 */
	protected Vector<Integer> getSitable(boolean[] tables, int tabPerCust) {
		Vector<Integer> list = new Vector<Integer>();
		for (int i = 0; i < tables.length; i++ ){
			if (canSit(tables, i, tabPerCust)) list.add(i);
		}
		return list;		
	}
	
	/**
	 * Sit the customer
	 * @param tables
	 * @param pos
	 * @param tabPerCust
	 * @return
	 */
	protected boolean[] sitCust (boolean[] tables, int pos, int tabPerCust) {
		boolean[] output = new boolean[tables.length];
		for (int i=0; i<tables.length; i++) {
			output[i] = tables[i];
			if (i >= pos && i < pos + tabPerCust) {
				output[i] = true;
			}
		}
		return output;
	}
	
	/**
	 * Calculate the eventual number of tables filled at the lowest leave of the inverted tree
	 * @param currID
	 * @param output
	 * @param expected
	 * @param sitables
	 * @param tabPerCust 
	 * @return
	 */
	protected double updateFilled(String currID, double output, double expected, Vector<Integer> sitables, int tabPerCust) {
		int denominator = sitables.isEmpty()? 1:sitables.size();
		//System.out.println("["+ currID +"].avail="+ sitables +",o="+output + "+=("+expected+" x "+(this.probs[tabPerCust-1]/100.0)+ ")/"+denominator+", = "+(output + (expected*(this.probs[tabPerCust-1]/100.0)/denominator)));
		output += (this.probs[tabPerCust-1]/100.0)*(expected/denominator);
		return output;
	}
	
	/**
	 * At every turn, find the number of filled given the current probability 
	 * and the current customer we are working on
	 * @param tables - non-nullable
	 * @param currCust - starts with 0
	 * @param tabPerCust - table per customer
	 * @return
	 */
	public double getExpected(boolean[] tables, int currCust, int tabPerCust, String pId) {
		double output = 0.0;
		Vector<Integer> sitables = getSitable(tables, tabPerCust);
		int sitable = sitables.size();
		// at the lowest leaf of the inverted tree, calculate the eventual filled 
		if (sitable < 1) {
			String chosen = sitable == 0? "":String.valueOf(sitables.get(0));
			int additon = sitable == 0?0:tabPerCust;
			String currID = pId+"-"+currCust+""+chosen;
			output = updateFilled(currID, output, countTrue(tables)+additon, sitables, tabPerCust);
			return output;			
		} else {			
			
			//for the current customer, find out if he can sit starting from a particular position,
			// if yes, sit him, and try to sit the next customer. 
			// if this continues till all customers have been seated, count filled.
			// if this ends prematurely, i.e. currCust < numTables, just count filled
			for (int i=0; i < sitables.size(); i++) {				
				int table = sitables.get(i);
				
				//loop for each possible num of tables to be occupied by the next customer 
				for (int j=0; j < probs.length; j++) {
					int cTabPerCust = j+1;
					String currID = pId+"_"+tabPerCust+"-"+table;
					boolean[] afterSitting = sitCust(tables, table, tabPerCust);
					double expected = 0.0;
					if (probs[j] == 0 || cTabPerCust > maxFalse(afterSitting)) {
						expected = countTrue(afterSitting);
					} else {
						expected = getExpected(afterSitting, currCust+1, cTabPerCust, currID);
					}
					output = updateFilled(currID, output, expected, sitables, cTabPerCust);
				}
			}
			
		}
		return output;
	}
	
	/**
	 * At every turn, find the number of filled given the current probability 
	 * and the current customer we are working on
	 * @param tables - non-nullable
	 * @param currCust - starts with 0
	 * @param tabPerCust - table per customer
	 * @return
	 */
//	public double getExpected(boolean[] tables, int currCust, int tabPerCust, String pId) {
//		double output = 0.0;
//		Vector<Integer> sitables = getSitable(tables, tabPerCust);
//		int sitable = sitables.size();
//		// at the lowest leaf of the inverted tree, calculate the eventual filled 
//		if (sitable < 1) {
//			String chosen = sitable == 0? "":String.valueOf(sitables.get(0));
//			int additon = sitable == 0?0:tabPerCust;
//			String currID = pId+"-"+currCust+""+chosen;
//			output = updateFilled(currID, output, countTrue(tables)+additon, sitables);
//			return output;			
//		} else {			
//			//for the current customer, find out if he can sit starting from a particular position,
//			// if yes, sit him, and try to sit the next customer. 
//			// if this continues till all customers have been seated, count filled.
//			// if this ends prematurely, i.e. currCust < numTables, just count filled
//			for (int i=0; i < sitables.size(); i++) {				
//				int table = sitables.get(i);
//				String currID = pId+"-"+currCust+""+table;
//				double expected = getExpected(sitCust(tables, table, tabPerCust), currCust+1, tabPerCust, currID);				
//				output = updateFilled(currID, output, expected, sitables);
//			}
//			
//		}
//		return output;
//	}
	
	
	
	/** main function
	 * 	
	 * 	
	 * 12
	 * {9,9,9,9,9,9,9,9,9,9,10}
	 * Returns: 7.871087929710551
	 * 
	 * 
	 * At every turn, find the number of filled as of the current probability
	 * @param numTables
	 * @param probs
	 * @return the number of tables filled
	 */
	
	public double getExpected(int numTables, int[] probs) {
		double output = 0.0;
		this.probs = probs;
		//iterate through the number of probs
		for (int tabPerCust = 0; tabPerCust < probs.length; tabPerCust++) {
			if (probs[tabPerCust] == 0 || tabPerCust > numTables) continue;
			Date start = new Date();
			System.out.print("Start - "+ start);
			double temp = 0.0;
			temp = getExpected(new boolean[numTables], 0, tabPerCust+1, "s-"+(tabPerCust+1));
			output += temp*probs[tabPerCust];
			//} else output += numTables*probs[tabPerCust];
			Date end = new Date();
			System.out.println(", took " + (end.getTime() - start.getTime()) + " ms: Main: output now at "+ output +", tabPerCust=" + (tabPerCust+1) + ", end at " + end);
		}
		output /= 100;
		System.out.println("Final Output: "+ numTables +"="+output);
		return output;
	}

}
