package model;

public class Interval {
	/**
	 * Start of Interval
	 */
	private int mLower;
	/**
	 * End of Interval
	 */
	private int mUpper;

	public Interval(int mLower, int mUpper) {
		super();
		this.mLower = mLower;
		this.mUpper = mUpper;
	}

	public int getmLower() {
		return mLower;
	}

	public void setmLower(int mLower) {
		this.mLower = mLower;
	}

	public int getmUpper() {
		return mUpper;
	}

	public void setmUpper(int mUpper) {
		this.mUpper = mUpper;
	}

	@Override
	public String toString() {
		return "[" + mLower + ", " + mUpper + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return super.equals(obj);
		else {
			Interval interval = (Interval) obj;
			return interval.mLower == mLower && interval.mUpper == mUpper;
		}
	}
}
