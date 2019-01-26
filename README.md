# MergeIntervalsJava
A little Java GUI Application that will merge overlapping intervals thread based.

Time complexity: Sorting the application dominates, so O(nLogn)

Space complexity : Sorting will use Space on dividing (MergeSort), so O(n)

robustness: 
On Gui level, the spinners make sure that only integers are used as input. It's also checked for duplicates and the start of an interval has to be smaller than the end.
as for big input data: 
  - the count of input data is obviously limited by the available memory (a limit for the count could be set)
  - the algorithm will work in a thread, so a crash of the main thread will be avoided
  - because of the time complexity the algorithm will be "fast" even with a big count of input data
  
the merge function is to be found at https://github.com/momor10/MergeIntervalsJava/blob/master/src/controller/MergeCallable.java
