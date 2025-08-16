package ot;

import java.util.ArrayList;

import com.StringOperation;
import com.VectorClock;

import ast.ASTStringObjectSequence;

public class FalseTieStringTransformer extends StringTransformer {

	IGetOpList opListGetter;

	public void setControlAlgorithm(IGetOpList opListGetter) {
		this.opListGetter = opListGetter;

	}

	@Override
	protected int compare(OTOperation op1, OTOperation op2) {
		int result = 0;
		// System.out.println(op1.toString());
		// System.out.println(op2.toString());
		StringOperation sop1Origin = op1.getOrigin();
		StringOperation sop2Origin = op2.getOrigin();
		// System.out.println(sop1Origin);
		// System.out.println(sop2Origin);

		VectorClock vc1 = op1.getVectorClock().clone();
		VectorClock vc2 = op2.getVectorClock().clone();
		int sid1 = op1.getSID();
		int sid2 = op2.getSID();
		vc1.set(sid1, vc1.get(sid1) - 1);
		vc2.set(sid2, vc2.get(sid2) - 1);

		if (VectorClock.equivalent(vc1, vc2)) {
			result = sop1Origin.getPosition() - sop2Origin.getPosition();
		} else {
			// VectorClock vcInterSect = VectorClock.intersect(op1.getVectorClock(),
			// op2.getVectorClock());

			//ArrayList<OTOperation> opList = this.opListGetter.getCausalOps(op1, op2);
			ArrayList<OTOperation> opList = this.opListGetter.getCausalOps(op1, op2);
			// opList1.addAll(opList2);

			ASTStringObjectSequence os = new ASTStringObjectSequence(null);

			//System.out.println("MIC");
			for (OTOperation op : opList) {
				//System.out.println(op.getOrigin().toString());
				os.remoteApply(op.getOrigin(), op.getTOP());
			}
			// os.printState();

			result = os.compare(sop1Origin, op1.getTOP(), sop2Origin, op2.getTOP());
		}

		if (result == 0) {
			result = sid1 - sid2;
		}

		return result;
	}
}
