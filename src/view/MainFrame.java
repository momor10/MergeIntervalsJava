package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.SpringLayout;

import controller.MergeCallable;
import model.Interval;

public class MainFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static JSpinner jLowerInterval;
	private static JSpinner jUpperInterval;
	static DefaultListModel<Interval> inputListModel;
	static DefaultListModel<Interval> resultListModel;

	/**
	 * 
	 */
	public MainFrame() {
		setupGUI();
		populateDefaultInputList();
	}

	/**
	 * Populate the Input list with the default values from the task.
	 */
	private void populateDefaultInputList() {
		inputListModel.add(0, new Interval(25, 30));
		inputListModel.add(0, new Interval(2, 19));
		inputListModel.add(0, new Interval(14, 23));
		inputListModel.add(0, new Interval(4, 8));
	}

	/**
	 * Setup a GUI with 2 Spinners and a Button for adding an interval. Intervals
	 * will be added to a jList holding the input intervals to be merged. A merge
	 * button will trigger processing/merging the input list. The result will be
	 * shown in a JList.
	 */
	private void setupGUI() {
		inputListModel = new DefaultListModel<Interval>();
		resultListModel = new DefaultListModel<Interval>();
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		JLabel lblAddInterval = new JLabel("Add Interval");
		springLayout.putConstraint(SpringLayout.NORTH, lblAddInterval, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblAddInterval, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblAddInterval);

		jLowerInterval = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, jLowerInterval, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, jLowerInterval, 6, SpringLayout.EAST, lblAddInterval);
		springLayout.putConstraint(SpringLayout.EAST, jLowerInterval, 66, SpringLayout.EAST, lblAddInterval);
		getContentPane().add(jLowerInterval);

		jUpperInterval = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, jUpperInterval, 6, SpringLayout.SOUTH, jLowerInterval);
		springLayout.putConstraint(SpringLayout.WEST, jUpperInterval, 0, SpringLayout.WEST, jLowerInterval);
		springLayout.putConstraint(SpringLayout.EAST, jUpperInterval, 0, SpringLayout.EAST, jLowerInterval);
		getContentPane().add(jUpperInterval);

		JButton jAddButton = new JButton("Add");
		jAddButton.addActionListener(onAddButtonClicked);
		springLayout.putConstraint(SpringLayout.NORTH, jAddButton, 6, SpringLayout.SOUTH, jUpperInterval);
		springLayout.putConstraint(SpringLayout.WEST, jAddButton, 0, SpringLayout.WEST, jLowerInterval);
		springLayout.putConstraint(SpringLayout.EAST, jAddButton, 0, SpringLayout.EAST, jLowerInterval);
		getContentPane().add(jAddButton);

		JLabel lblInputList = new JLabel("Input List");
		springLayout.putConstraint(SpringLayout.NORTH, lblInputList, 0, SpringLayout.NORTH, lblAddInterval);
		springLayout.putConstraint(SpringLayout.WEST, lblInputList, 6, SpringLayout.EAST, jLowerInterval);
		getContentPane().add(lblInputList);

		JList<Interval> jInputList = new JList<Interval>(inputListModel);
		springLayout.putConstraint(SpringLayout.NORTH, jInputList, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, jInputList, 6, SpringLayout.EAST, lblInputList);
		springLayout.putConstraint(SpringLayout.SOUTH, jInputList, -39, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, jInputList, 66, SpringLayout.EAST, lblInputList);
		getContentPane().add(jInputList);

		JLabel lblResultList = new JLabel("Result List");
		springLayout.putConstraint(SpringLayout.WEST, lblResultList, 6, SpringLayout.EAST, jInputList);
		springLayout.putConstraint(SpringLayout.SOUTH, lblResultList, 0, SpringLayout.SOUTH, lblAddInterval);
		getContentPane().add(lblResultList);

		JList<Interval> jResultList = new JList<Interval>(resultListModel);
		springLayout.putConstraint(SpringLayout.NORTH, jResultList, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, jResultList, 6, SpringLayout.EAST, lblResultList);
		springLayout.putConstraint(SpringLayout.EAST, jResultList, 65, SpringLayout.EAST, lblResultList);
		getContentPane().add(jResultList);

		JButton jMergeButton = new JButton("Merge");
		jMergeButton.addActionListener(onMergeButtonClicked);
		springLayout.putConstraint(SpringLayout.SOUTH, jResultList, -6, SpringLayout.NORTH, jMergeButton);
		springLayout.putConstraint(SpringLayout.SOUTH, jMergeButton, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, jMergeButton, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(jMergeButton);
	}

	/**
	 * add the Inveral entered to the spinners into the input list
	 */
	private static ActionListener onAddButtonClicked = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			int lowerValue = (Integer) jLowerInterval.getValue();
			int upperValue = (Integer) jUpperInterval.getValue();
			if (lowerValue < upperValue) {
				Interval interval = new Interval(lowerValue, upperValue);
				if (!inputListModel.contains(interval))
					inputListModel.add(0, interval);
			}
		}
	};

	/**
	 * OnClick of merge button clear old and trigger merging task in a thread. then
	 * retrieve the result and populate the JList with the result intervals.
	 */
	private static ActionListener onMergeButtonClicked = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			resultListModel.clear();
			/* execute merging in a thread */
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			List<Interval> inputList = Collections.list(inputListModel.elements());
			MergeCallable callable = new MergeCallable(inputList);
			long startTime = System.currentTimeMillis();
			Future<List<Interval>> future = executorService.submit(callable);
			List<Interval> result;
			try {
				/* get the result from thread and populate the gui result list */
				result = future.get();
				for (int i = 0; i < result.size(); i++)
					resultListModel.add(0, result.get(i));
				System.out.println("Task completed in " + (System.currentTimeMillis() - startTime) + "ms");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
			executorService.shutdown();
		}
	};

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setTitle("MERGE");
		frame.setSize(500, 200);
		frame.setResizable(true);
		frame.setLocation(50, 50);
		frame.setVisible(true);
	}
}
