package ast;

import com.GTAlgorithm;
import com.StringOperation;
import com.TimestampedOperation;
import com.VectorClock;

public class AST extends GTAlgorithm {
	ASTStringObjectSequence os;

	public AST(int sid, int num, ASTStringObjectSequence os) {
		super(sid, num);
		// TODOT Auto-generated constructor stub
		this.os = os;
	}

	@Override
	public StringOperation ROH(TimestampedOperation top) {// " ins | pos | str | sid | vc "; "del | pos | len | sid | vc
															// "
		StringOperation sop = StringOperation.from(top.getCustomizedOpStr());
		int sid = top.getSID();
		StringOperation op = this.os.remoteApply(sop, top);
		// this.os.printState();
		this.clear(this.mclock.getMinVClock());
		this.remoteTimeUpdate(sid, top.getVC());
		return op;

	}

	@Override
	public TimestampedOperation LOH(StringOperation op) {
		localTimeUpdate();
		VectorClock vc = this.getNewVClock();
		TimestampedOperation top = new TimestampedOperation(vc, this.sid, op.toString());
		this.os.localApply(op, top);
		// this.os.printState();
		return top;
	}

	protected void clear(VectorClock minVC) {
		this.os.gc(minVC);
		this.os.merge();
	}

	public int memorySize() {
		return this.os.memorySize();
	}

}