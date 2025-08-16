package ot;

import java.util.Iterator;
import java.util.LinkedList;

import com.DelParas;
import com.OpType;
import com.StringOperation;

public class StringTransformer extends AbstractTransformer {

	public void SIT(OTOperation op1, OTOperation op2) {
		StringOperation sop1 = op1.getSOP();
		StringOperation sop2 = op2.getSOP();
		if (sop1.type == OpType.ins && sop2.type == OpType.ins) {
			SIT_II(sop1, op1, sop2, op2);
		} else if (sop1.type == OpType.del && sop2.type == OpType.del) {
			if (sop1.subOps == null) {
				sop1.add(new DelParas(sop1.pos, sop1.len));
			}

			if (sop2.subOps == null) {
				sop2.add(new DelParas(sop2.pos, sop2.len));
			}

			SIT_DD_Copy(sop1, sop2);
		} else if (sop1.type == OpType.ins && sop2.type == OpType.del) {
			if (sop2.subOps == null) {
				sop2.add(new DelParas(sop2.pos, sop2.len));
			}

			SIT_ID(sop1, sop2);
		} else {
			if (sop1.subOps == null) {
				sop1.add(new DelParas(sop1.pos, sop1.len));
			}

			SIT_ID(sop2, sop1);
		}

	}

	private void SIT_II(StringOperation op1, OTOperation o1, StringOperation op2, OTOperation o2) {
		if (op1.pos > op2.pos) {
			op1.pos = op1.pos + op2.str.length();
		} else if (op1.pos == op2.pos) {
			if (compare(o1,o2) > 0) { // op1.sid > op2.sid
				op1.pos = op1.pos + op2.str.length();
			} else {
				op2.pos = op2.pos + op1.str.length();
			}
		} else {
			op2.pos = op2.pos + op1.str.length();
		}
	}

	private void SIT_ID(StringOperation op1, StringOperation op2) {
		int num = op2.subOps.size();
		for (int count = 0; count < num; count++) {
			DelParas opx = op2.subOps.pollFirst();
			if (opx.pos + opx.len <= op1.pos) {
				op1.pos = op1.pos - opx.len;
				op2.subOps.addLast(opx);
			} else if (opx.pos >= op1.pos) {
				opx.pos = opx.pos + op1.str.length();
				op2.subOps.addLast(opx);
			} else {
				op2.subOps.addLast(new DelParas(opx.pos, op1.pos - opx.pos));
				// modify on 2025-04-09
				op2.subOps.addLast(new DelParas(opx.pos + op1.len, opx.pos + opx.len - op1.pos));
				op1.pos = opx.pos;
			}
		}
	}

	private void clearIdentity(LinkedList<DelParas> paras) {
		Iterator<DelParas> iter = paras.iterator();
		while (iter.hasNext()) {
			DelParas para = iter.next();
			if (para.pos == -1) {
				iter.remove();
			}
		}

	}

	private void SIT_DD_Copy(StringOperation op1, StringOperation op2) {
		Iterator<DelParas> iter1 = op1.subOps.iterator();
		while (iter1.hasNext()) {
			DelParas opx = iter1.next();
			Iterator<DelParas> iter2 = op2.subOps.iterator();
			while (iter2.hasNext() && opx.pos != -1) {
				DelParas opy = iter2.next();
				if (opy.pos == -1) {
					continue;
				}

				if (opx.pos + opx.len <= opy.pos) {
					opy.pos = opy.pos - opx.len;
				} else if (opx.pos >= (opy.pos + opy.len)) {
					opx.pos = opx.pos - opy.len;
				} else if (opx.pos == opy.pos) {
					if (opx.pos + opx.len < opy.pos + opy.len) {
						opx.pos = -1;
						opy.len = opy.len - opx.len;
					} else if (opx.pos + opx.len == opy.pos + opy.len) {
						opx.pos = -1;
						opy.pos = -1;
					} else {
						opx.len = opx.len - opy.len;
						opy.pos = -1;
					}
				} else if (opx.pos > opy.pos) {
					if (opx.pos + opx.len > opy.pos + opy.len) {
						int overlapping = opy.pos + opy.len - opx.pos;
						opy.len = opy.len - overlapping;

						opx.len = opx.len - overlapping;
						opx.pos = opy.pos;
					} else {
						opx.pos = -1;
						opy.len = opy.len - opx.len;
					}
				} else {
					if (opx.pos + opx.len < opy.pos + opy.len) {
						int overlapping = opx.pos + opx.len - opy.pos;
						opy.len = opy.len - overlapping;
						opy.pos = opx.pos;

						opx.len = opx.len - overlapping;
					} else {
						opx.len = opx.len - opy.len;
						opy.pos = -1;
					}
				}
			}
		}
		clearIdentity(op1.subOps);
		clearIdentity(op2.subOps);
	}

	private void SIT_DD(StringOperation op1, StringOperation op2) {
		int num1 = op1.subOps.size();
		for (int count1 = 0; count1 < num1; count1++) {
			DelParas opx = op1.subOps.pollFirst();
			int num2 = op2.subOps.size();
			for (int count2 = 0; opx.pos != -1 && count2 < num2; count2++) {
				DelParas opy = op2.subOps.pollFirst();
				if (opx.pos + opx.len <= opy.pos) {
					opy.pos = opy.pos - opx.len;
					op2.subOps.addLast(opy);
				} else if (opx.pos >= (opy.pos + opy.len)) {
					opx.pos = opx.pos - opy.len;
					op2.subOps.addLast(opy);
				} else if (opx.pos == opy.pos) {
					if (opx.pos + opx.len < opy.pos + opy.len) {
						opx.pos = -1;
						opy.len = opy.len - opx.len;
						op2.subOps.addFirst(opy);
					} else if (opx.pos + opx.len == opy.pos + opy.len) {
						opx.pos = -1;
					} else {
						opx.len = opx.len - opy.len;
					}
				} else if (opx.pos > opy.pos) {
					if (opx.pos + opx.len > opy.pos + opy.len) {
						int overlapping = opy.pos + opy.len - opx.pos;
						opy.len = opy.len - overlapping;
						op2.subOps.addLast(opy);

						opx.len = opx.len - overlapping;
						opx.pos = opy.pos;
					} else {
						opx.pos = -1;
						opy.len = opy.len - opx.len;
						op2.subOps.addFirst(opy);
					}
				} else {
					if (opx.pos + opx.len < opy.pos + opy.len) {
						int overlapping = opx.pos + opx.len - opy.pos;
						opy.len = opy.len - overlapping;
						opy.pos = opx.pos;
						op2.subOps.addLast(opy);

						opx.len = opx.len - overlapping;
					} else {
						opx.len = opx.len - opy.len;

					}
				}

			}

			if (opx.pos != -1) {
				op1.subOps.addLast(opx);
			}
		}
	}

	protected int compare(OTOperation op1, OTOperation op2) {
		return op1.getSID() - op2.getSID();
	}

	public static void main(String[] args) {
		StringTransformer transformer = new StringTransformer();
		LinkedList<DelParas> paras = new LinkedList<DelParas>();
		paras.add(new DelParas(6, 1));
		paras.add(new DelParas(12, 2));
		StringOperation sop = StringOperation.createDelOperation(paras);

		LinkedList<DelParas> paras2 = new LinkedList<DelParas>();
		paras2.add(new DelParas(5, 2));
		paras2.add(new DelParas(11, 1));
		StringOperation sop2 = StringOperation.createDelOperation(paras2);
		transformer.SIT_DD(sop, sop2);
		System.out.println(sop);
		System.out.println(sop2);
	}

}