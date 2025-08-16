package ot;

import com.StringOperation;
import com.TimestampedOperation;
import com.VectorClock;

public class OTOperation {
	public OTOperation(TimestampedOperation top, StringOperation sop) {
		this.sop = sop;
		this.top = top;
		this.originSOP = sop.clone();
		this.isGarbage = false;
		this.isCopy = false;
	}

	private OTOperation(StringOperation sop) {
		this.sop = sop;
	}

	boolean isCopy;
	public StringOperation sop;
	private TimestampedOperation top;
	private StringOperation originSOP;
	boolean isGarbage;

	public int getSEQ() {
		return this.top.getSEQ();
	}

	public int getSID() {
		return this.top.getSID();
	}

	public TimestampedOperation getTOP() {
		return this.top;
	}

	public VectorClock getVectorClock() {
		return this.top.getVC();
	}

	public boolean isCopy() {
		return isCopy;
	}

	public boolean isReferToSameOp(OTOperation op) {
		return this.getSID() == op.getSID() && this.getSEQ() == op.getSEQ();
	}

	public OTOperation deepCopy() {
		OTOperation copy = new OTOperation(this.sop.clone());
		copy.isCopy = true;
		copy.top = this.top;
		copy.originSOP = this.originSOP;
		copy.isGarbage = this.isGarbage;
		return copy;
	}

	public OTOperation shallowCopy() {
		OTOperation copy = new OTOperation(this.sop);
		copy.isCopy = false;
		copy.top = this.top;
		copy.originSOP = this.originSOP;
		copy.isGarbage = this.isGarbage;
		return copy;
	}

	/*
	 * public OTOperation shallowCopy() { OTOperation copy = new
	 * OTOperation(this.sop); copy.isCopy = false; copy.top = this.top;
	 * copy.originSOP = this.originSOP; copy.isGarbage = this.isGarbage; return
	 * copy; }
	 */

	public boolean isCausalBefore(OTOperation op) {
		return this.top.isCasualBefore(op.top);
	}

	public boolean isConcurrent(OTOperation op2) {
		return this.top.isConcurrent(op2.top);
	}

	public int totalOrder(OTOperation op2) {
		return this.top.totalOrderByScalarTimestamps(op2.top);
	}

	public StringOperation getSOP() {
		return this.sop;
	}

	public StringOperation getOrigin() {
		return this.originSOP;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(this.sop.toString());
		/*
		 * if (this.op.type == OpType.ins) { sb.append("ins|"); sb.append(this.op.pos);
		 * sb.append("|"); sb.append(op.str); sb.append("|");
		 * sb.append(this.top.getSID()); sb.append(","); sb.append(this.top.getSEQ()); }
		 * else { sb.append("del|"); sb.append(this.op.pos); sb.append("|");
		 * sb.append(op.len); sb.append("|"); sb.append(this.top.getSID());
		 * sb.append(","); sb.append(this.top.getSEQ()); }
		 */

		sb.append("|");
		sb.append(this.top.getSID());
		sb.append(",");
		sb.append(this.top.getSEQ());
		sb.append("|");
		sb.append(this.getVectorClock().toString());

		return sb.toString();
	}
}