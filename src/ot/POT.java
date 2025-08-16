package ot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.GTAlgorithm;
import com.StringOperation;
import com.TimestampedOperation;
import com.VectorClock;

public class POT extends GTAlgorithm {
	HashMap<Integer, ArrayList<OTOperation>> TM;
	ArrayList<OTOperation> HB;
	AbstractTransformer transformer;
	boolean gcTurnOn = false;

	public POT(int sid, int num, AbstractTransformer transformer, boolean gcTurnOn) {
		super(sid, num);

		this.TM = new HashMap<>();
		this.HB = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			if (this.sid != i) {
				this.TM.put(i, new ArrayList<>());
			}
		}
		this.transformer = transformer;
		this.gcTurnOn = gcTurnOn;
	}

	@Override
	public StringOperation ROH(TimestampedOperation top) {

		int sid = top.getSID();
		// System.out.println("site number:" + this.sid);
		OTOperation op = new OTOperation(top, StringOperation.from(top.getCustomizedOpStr()));
		// System.out.println("before ROH:" + op.toString());
		// OTOperation opOrigin = op.deepCopy();//Modification 16:32
		OTOperation opx = null;

		ArrayList<OTOperation> path = TM.get(sid);

		int index = 0;
		int size = path.size();

		// System.out.println("TM Path Size at sid: " + size);
		// -------causal ops----------------------------------------- //
		for (; index < size; index = index + 1) {
			opx = path.get(index);
			if (op.isConcurrent(opx)) {
				break;
			}
		}

		// -------concurrent ops and total order before---------------//
		// System.out.println("L1 Transforming ");
		for (; index < size; index = index + 1) {
			opx = path.get(index);
			if (op.totalOrder(opx) < 0) {
				break;
			}
			// printTM();
			// System.out.println("before: " + op.toString() + ", " + opx.toString());
			if (opx.isCopy()) {
				// System.out.println("opx is copy: "+opx.toString());
				this.transformer.SIT(op, opx);
				// System.out.println("after: " + op.toString() + ", " + opx.toString());
			} else {
				OTOperation temp = opx.deepCopy();
				this.transformer.SIT(op, temp);
				path.set(index, temp);
				// System.out.println("after: " + op.toString() + ", " + temp.toString());
			}

		}

		// ------concurrent ops and total order after----------------//

		if (index == size) {
			this.storeRemoteOp(sid, op);
		} else {

			OTOperation opL1Copy = op.deepCopy();
			for (; index < size; index = index + 1) {
				opx = path.get(index);
				// System.out.println("before: "+op.toString()+", "+opx.toString());

				this.transformer.SIT(op, opx);
				// System.out.println("after: "+op.toString()+", "+opx.toString());

			}
			this.storeRemoteOp(sid, opL1Copy);
		}

		add(this.HB, op);// Modification 16:32
		// add(this.HB, opOrigin);

		// System.out.println("AFTER ROH:" + op.toString());
		this.remoteTimeUpdate(sid, top.getVC());
		if (gcTurnOn) {
			this.clear(this.mclock.getMinVClock());
		}
		return op.getSOP();
	}

	public void storeRemoteOp(int sid, OTOperation op) {
		for (Map.Entry<Integer, ArrayList<OTOperation>> entry : TM.entrySet()) {
			int key = entry.getKey();
			if (key != sid) {
				add(entry.getValue(), op.shallowCopy());
			}
		}
	}

	public void storeLocalOp(OTOperation op) {
		for (Map.Entry<Integer, ArrayList<OTOperation>> entry : TM.entrySet()) {
			entry.getValue().add(op.shallowCopy());
		}

	}

	@Override
	public TimestampedOperation LOH(StringOperation sop) {
		this.localTimeUpdate();

		TimestampedOperation top = new TimestampedOperation(this.getNewVClock(), this.sid, sop.toString());
		OTOperation op = new OTOperation(top, sop);
		this.storeLocalOp(op);
		// this.HB.add(op.deepCopy());//Modification 16:32
		this.HB.add(op);
		return top;
	}

	public int memorySize() {
		int size = 0;
		for (Map.Entry<Integer, ArrayList<OTOperation>> entry : TM.entrySet()) {
			ArrayList<OTOperation> path = entry.getValue();
			for (OTOperation item : path) {
				if (item.isCopy) {
					size = size + 1;
				}
			}
		}
		return size;
	}

	public void add(ArrayList<OTOperation> path, OTOperation op) {
		int index = 0;
		int size = path.size();
		for (; index < size; index = index + 1) {
			OTOperation opx = path.get(index);
			if (op.totalOrder(opx) < 0) {
				break;
			}
		}
		path.add(index, op);
	}

	protected void clear(VectorClock minVC) {

		for (Map.Entry<Integer, ArrayList<OTOperation>> entry : TM.entrySet()) {
			traverseListAndClear(entry.getValue().iterator(), minVC);
		}
		traverseListAndClear(this.HB.iterator(), minVC);
	}

	protected void traverseListAndClear(Iterator<OTOperation> it, VectorClock vc) {

		// int count = 0;
		while (it.hasNext()) {
			OTOperation item = it.next();
			if (item.isGarbage || item.getSEQ() <= vc.get(item.getSID())) {
				// 删除item
				// count++;
				it.remove();
			} else {
				break;
			}
		}
		// System.out.println("deleting " +count+ " op in TM.");

	}

	private void printTM() {
		for (Map.Entry<Integer, ArrayList<OTOperation>> entry : TM.entrySet()) {
			ArrayList<OTOperation> list = entry.getValue(); // Modification 16:32
			System.out.println("TM " + entry.getKey());
			for (OTOperation item : list) {
				System.out.print("isCopy: " + item.isCopy);
				System.out.println(" " + item.toString());
			}
		}
	}

	public static void main(String[] args) {
		StringTransformer transformer1 = new StringTransformer();
		StringTransformer transformer2 = new StringTransformer();
		StringTransformer transformer3 = new StringTransformer();

		POT site0 = new POT(0, 3, transformer1, false);
		POT site1 = new POT(1, 3, transformer2, false);
		POT site2 = new POT(2, 3, transformer3, false);

		StringOperation sop01 = StringOperation.createInsOperation(6, "def");
		StringOperation sop02 = StringOperation.createInsOperation(1, "345");

		StringOperation sop11 = StringOperation.createInsOperation(3, "hij");
		StringOperation sop12 = StringOperation.createDelOperation(1, 3);

		StringOperation sop21 = StringOperation.createInsOperation(5, "abc");
		StringOperation sop22 = StringOperation.createInsOperation(3, "cde");

		TimestampedOperation top01 = site0.LOH(sop01);
		TimestampedOperation top02 = site0.LOH(sop02);

		TimestampedOperation top11 = site1.LOH(sop11);
		TimestampedOperation top12 = site1.LOH(sop12);

		TimestampedOperation top21 = site2.LOH(sop21);
		TimestampedOperation top22 = site2.LOH(sop22);

		top01.setOrder(1);
		top02.setOrder(2);
		top11.setOrder(3);
		top12.setOrder(4);
		top21.setOrder(5);
		top22.setOrder(6);

		// site0.ROH(top11);
		// site0.ROH(top12);
		// site0.ROH(top21);
		// site0.ROH(top22);
		System.out.println("top01");
		site2.ROH(top01);
		System.out.println("top02");

		site2.ROH(top02);
		System.out.println("top11");

		StringOperation sopResult = site2.ROH(top11);
		// StringOperation sopResult =site2.ROH(top12);
		System.out.println(sopResult);

	}

}