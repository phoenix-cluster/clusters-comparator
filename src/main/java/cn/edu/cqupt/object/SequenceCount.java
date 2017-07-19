package cn.edu.cqupt.object;

/**
 * Created by Johnny on 2017/3/22.
 */
public class SequenceCount {
	private final String sequence;
	private final int count;

	public SequenceCount(String sequence, int count) {
		this.sequence = sequence;
		this.count = count;
	}

	public String getSequence() {
		return sequence;
	}

	public int getCount() {
		return count;
	}
}