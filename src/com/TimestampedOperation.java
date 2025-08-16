package com;

public class TimestampedOperation extends AbstractOperation {

	protected VectorClock vc; // Vector clock
	protected int sid;
	protected final String customizedOpStr;
	private int order;

	public TimestampedOperation(VectorClock vc, int sid, String customizedOpStr) {
		this.vc = vc;
		this.sid = sid;
		this.customizedOpStr = customizedOpStr;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getCustomizedOpStr() {
		return this.customizedOpStr;
	}

	public VectorClock getVC() {
		return this.vc;
	}

	public int getSID() {
		return this.sid;
	}

	public int getSEQ() {
		return this.vc.get(this.sid);
	}

	public int getSEQ(int sid) {
		return this.vc.get(sid);
	}

	public int totalOrderByScalarTimestamps(TimestampedOperation op2) {
		// System.out.println("compare order: {" + this.sid + ", " + this.getSEQ() + "}
		// " + "{" + op2.getSID() + ","
		// + op2.getSEQ() + "}");
		// System.out.println("order: " + this.order + " - " + op2.order);
		return this.order - op2.order;
	}

	public int totalOrderByVectorTimestamps(TimestampedOperation op2) {
		int svc1 = this.vc.getSum();
		int svc2 = op2.vc.getSum();
		if (svc1 == svc2) {
			return this.sid - op2.sid;
		} else {
			return svc1 - svc2;
		}
	}

	public boolean isCasualBefore(TimestampedOperation op2) {
		return this.getSEQ(this.sid) <= op2.getSEQ(this.sid);
	}

	public boolean isConcurrent(TimestampedOperation op2) {
		boolean result = true;
		if (this.getSEQ(this.sid) <= op2.getSEQ(this.sid)) {
			result = false;
		} else if (this.getSEQ(op2.getSID()) >= op2.getSEQ(op2.getSID())) {
			result = false;
		}
		return result;
	}

	public String toString() {
		return this.customizedOpStr + " {" + this.getSID() + "," + this.getSEQ() + "} ";
	}
}