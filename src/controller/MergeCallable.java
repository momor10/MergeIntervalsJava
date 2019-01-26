package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import model.Interval;

public class MergeCallable implements Callable<List<Interval>> {
	private List<Interval> inputList;

	public MergeCallable(List<Interval> inputList) {
		super();
		this.inputList = inputList;
	}

	@Override
	public List<Interval> call() throws Exception {
		return merge(inputList);
	}
	
	/**
	 * @param list
	 * list of intervals to be merged when overlapping
	 * @return
	 * merged list of intervals 
	 */
	public static List<Interval> merge(List<Interval> list){
		/*sort list by start of interval in O(nLogn)*/
		Collections.sort(list, (a, b) -> Integer.compare(a.getmLower(), b.getmLower()));
		List<Interval> resultList = new ArrayList<Interval>();
		for(int i=0;i<list.size();i++) {
			if(resultList.isEmpty() || resultList.get(resultList.size() - 1).getmUpper()<list.get(i).getmLower())
				 /*if the intervals don't overlap add the new one to the result list*/
				resultList.add(list.get(i));
			else
				/*new interval overlaps with current interval, merge them! start is already the minimum because of sorting, end is max of both intervals*/
				resultList.get(resultList.size() - 1).setmUpper(Math.max(resultList.get(resultList.size() - 1).getmUpper(), list.get(i).getmUpper()));
		}
		return resultList;
	}

}
