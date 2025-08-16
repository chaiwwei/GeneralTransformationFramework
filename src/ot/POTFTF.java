package ot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.StringOperation;
import com.TimestampedOperation;
import com.VectorClock;

public class POTFTF extends POT implements IGetOpList {

	public POTFTF(int sid, int num, FalseTieStringTransformer transformer, boolean gcTurnOn) {
		super(sid, num, transformer, gcTurnOn);
		transformer.setControlAlgorithm(this);
	}

	/*
	 * public ArrayList<OTOperation> getCausalOps(OTOperation op1, OTOperation op2)
	 * {
	 * 
	 * ArrayList<OTOperation> result = new ArrayList<>();
	 * 
	 * for (OTOperation item : this.HB) { // System.out.println("item1:" +
	 * item.toString()); if (!item.isReferToSameOp(op1) &&
	 * !item.isReferToSameOp(op2)) { if (item.isCausalBefore(op1) ||
	 * item.isCausalBefore(op2)) { // System.out.println("item:" + item.toString());
	 * result.add(item); } }
	 * 
	 * }
	 * 
	 * return result; }
	 */

	public ArrayList<OTOperation> getCausalOps(OTOperation op1, OTOperation op2) {

		ArrayList<OTOperation> comSet = new ArrayList<OTOperation>();
		ArrayList<OTOperation> op12Set = new ArrayList<OTOperation>();
		boolean op1SetCompleted = false;
		boolean op2SetCompleted = false;

		for (OTOperation item : this.HB) {
			// System.out.println("item1:" + item.toString());
			boolean flag1 = item.isCausalBefore(op1);
			boolean flag2 = item.isCausalBefore(op2);

			if (flag1 && flag2) {
				// System.out.println("item:" + item.toString());
				comSet.add(item);
			} else if ((flag1 && !item.isReferToSameOp(op1)) || (flag2 && !item.isReferToSameOp(op2))) {
				op12Set.add(item);
			} else {
				op1SetCompleted = op1SetCompleted || op1.totalOrder(item) <= 0;
				op2SetCompleted = op2SetCompleted || op2.totalOrder(item) <= 0;
				if (op1SetCompleted && op2SetCompleted) {
					break;
				}
			}
		}

		Iterator<OTOperation> it = comSet.iterator();
		while (it.hasNext()) {
			OTOperation item = it.next();
			boolean conOps = false;
			for (OTOperation temp : op12Set) {
				if (item.isConcurrent(temp)) {
					conOps = true;
					break;
				}
			}
			if (!conOps) {
				it.remove();
			}
		}

		if (!comSet.isEmpty()) {
			comSet.addAll(op12Set);
			return comSet;
		} else {
			return op12Set;
		}
	}

	public int memorySize() {
		// System.out.println("this.HB: " + this.HB.size());
		// System.out.println("TM: " + super.memorySize());
		return this.HB.size() + super.memorySize();
	}

	@Override
	protected void clear(VectorClock minVC) {

		for (Map.Entry<Integer, ArrayList<OTOperation>> entry : TM.entrySet()) {
			traverseListAndClear(entry.getValue().iterator(), minVC);
		}
		hbClear(minVC);
	}

	private void hbClear(VectorClock vc) {

		Iterator<OTOperation> it = this.HB.iterator();
		List<OTOperation> others = new ArrayList<OTOperation>();

		for (OTOperation item : this.HB) {
			item.isGarbage = item.isGarbage || item.getSEQ() <= vc.get(item.getSID());
			if (!item.isGarbage) {
				others.add(item);
			}
		}

		while (it.hasNext()) {
			OTOperation item = it.next();

			if (item.isGarbage) {
				boolean noConOps = true;
				for (OTOperation temp : others) {
					if (item.isConcurrent(temp)) {
						noConOps = false;
						break;
					}
				}

				if (noConOps) {
					it.remove();
				} else {
					break;
				}
			}
		}

		// System.out.println("deleting " + count + " op in HB.");
	}

	public static void main(String[] args) {
		FalseTieStringTransformer transformer1 = new FalseTieStringTransformer();
		FalseTieStringTransformer transformer2 = new FalseTieStringTransformer();
		FalseTieStringTransformer transformer3 = new FalseTieStringTransformer();
		FalseTieStringTransformer transformer4 = new FalseTieStringTransformer();

		POTFTF site0 = new POTFTF(0, 4, transformer1, false);
		POTFTF site1 = new POTFTF(1, 4, transformer2, false);
		POTFTF site2 = new POTFTF(2, 4, transformer3, false);
		// POTFTF site3 = new POTFTF(3, 4, transformer4, false);

		transformer1.setControlAlgorithm(site0);
		transformer2.setControlAlgorithm(site1);
		transformer3.setControlAlgorithm(site2);
		// transformer4.setControlAlgorithm(site3);

		StringOperation sop01 = StringOperation.createInsOperation(1, "89");
		StringOperation sop02 = StringOperation.createInsOperation(5, "gh");

		StringOperation sop11 = StringOperation.createInsOperation(1, "cd");
		StringOperation sop12 = StringOperation.createInsOperation(0, "45");

		StringOperation sop21 = StringOperation.createInsOperation(3, "56");
		StringOperation sop22 = StringOperation.createInsOperation(2, "fg");

		// StringOperation sop31 = StringOperation.createInsOperation(3, "j1");
		// StringOperation sop32 = StringOperation.createInsOperation(1, "hi");

		TimestampedOperation top01 = site0.LOH(sop01);
		TimestampedOperation top02 = site0.LOH(sop02);

		TimestampedOperation top11 = site1.LOH(sop11);
		TimestampedOperation top12 = site1.LOH(sop12);

		TimestampedOperation top21 = site2.LOH(sop21);
		TimestampedOperation top22 = site2.LOH(sop22);

		// TimestampedOperation top31 = site3.LOH(sop31);
		// TimestampedOperation top32 = site3.LOH(sop32);

		top01.setOrder(1);
		top02.setOrder(2);
		top11.setOrder(3);
		top12.setOrder(4);
		top21.setOrder(5);
		top22.setOrder(6);
		// top31.setOrder(7);
		// top32.setOrder(8);

		// site0.ROH(top11);
		// site0.ROH(top12);
		// site0.ROH(top21);
		// site0.ROH(top22);
		StringOperation sopResult = null;
		System.out.println("top01");
		sopResult = site2.ROH(top01);
		System.out.println(sopResult);
		System.out.println("top02");
		sopResult = site2.ROH(top02);
		System.out.println(sopResult);
		System.out.println("top11");
		sopResult = site2.ROH(top11);
		System.out.println(sopResult);
		System.out.println("top12");
		sopResult = site2.ROH(top12);
		System.out.println(sopResult);
	}

}