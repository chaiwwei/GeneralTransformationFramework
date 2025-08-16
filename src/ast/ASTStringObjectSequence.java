package ast;

import java.util.LinkedList;

import com.DelParas;
import com.OpType;
import com.StringOperation;
import com.TimestampedOperation;
import com.VectorClock;

import ot.OTOperation;

public class ASTStringObjectSequence {
	public ASTStringObject head;
	public ASTStringObject tail;

	public ASTStringObjectSequence(String content) {
		this.tail = new ASTStringObject(null, 0, null);
		this.head = new ASTStringObject(null, 0, this.tail);
		if (content != null && content.length() > 0) {
			this.head.next = new ASTStringObject(null, content.length(), this.tail);
		} else if (content == null) {
			this.head.next = new ASTStringObject(null, 0, this.tail);
		}
	}

	public void localApply(StringOperation sop, TimestampedOperation top) {
		if (sop.type == OpType.ins) {
			ASTStringObject obj = new ASTStringObject(top, sop.str.length(), this.tail);
			ASTStringObject prev = this.findPrevObjByPos(sop.pos, top);
			addAfter(obj, prev);
		} else {
			ASTStringObject prev = findPrevObjByPos(sop.pos, top);
			slice(prev.next, sop.len, top);
		}
	}

	public StringOperation remoteApply(StringOperation sop, TimestampedOperation top) {
		StringOperation sopTransform = null;

		if (sop.type == OpType.ins) {
			ASTStringObject obj = new ASTStringObject(top, sop.str.length(), this.tail);
			ASTStringObject prev = this.findPrevObjByPos(sop.pos, top);
			ASTStringObject prevM = this.find(prev, top);
			addAfter(obj, prevM);
			sopTransform = StringOperation.createInsOperation(indexOf(obj), sop.str);
		} else {
			ASTStringObject prev = findPrevObjByPos(sop.pos, top);
			LinkedList<ASTStringObject> objList = slice2(prev.next, sop.len, top);
			LinkedList<DelParas> dels = new LinkedList<>();

			if (!objList.isEmpty()) {
				int index = 0;
				ASTStringObject cur = objList.pollFirst();
				prev = this.head.next;
				while (cur != null) {
					index = index + indexOf(cur, prev);
					dels.add(new DelParas(index, cur.len));
					prev = cur.next;
					cur = objList.pollFirst();
				}
			}
			sopTransform = StringOperation.createDelOperation(dels);
		}

		return sopTransform;

	}

	private ASTStringObject findPrevObjByPos(int pos, TimestampedOperation top) {
		if (pos == 0) {
			return this.head;
		}

		ASTStringObject obj = this.head.next;
		for (int num = 0; obj != this.tail; obj = obj.next) {
			if (obj.visible(top)) {
				obj.split(pos - num);
				num = num + obj.len;

				if (pos == num) {
					break;
				}
			}
		}

		return obj;
	}

	public int compare(StringOperation sop1, TimestampedOperation top1, StringOperation sop2, TimestampedOperation top2) {


		ASTStringObject prev1 = this.findPrevObjByPos(sop1.getPosition(), top1);
		ASTStringObject prev1M = find(prev1, top1);
		int count1 = count(prev1M);

		ASTStringObject prev2 = this.findPrevObjByPos(sop2.getPosition(), top2);
		ASTStringObject prev2M = find(prev2, top2);
		int count2 = count(prev2M);

		int result = count1 - count2;
		if (result == 0) {
			result = top1.getSID() - top2.getSID();
		}
		//System.out.println("op1: "+op1.toString());
		//System.out.println("op2: "+op2.toString());
		//this.printState();
		//System.out.println("result: "+result);
		return result;
	}

	private int count(ASTStringObject target) {
		int num = 0;
		if (target != this.head) {
			for (ASTStringObject obj = this.head.next; obj != target; obj = obj.next) {

				num = num + obj.len;
			}
		}
		num = num + target.len;
		return num;
	}

	private int indexOf(ASTStringObject target) {
		int num = 0;
		if (target != this.head) {
			for (ASTStringObject obj = this.head.next; obj != target; obj = obj.next) {
				if (obj.visible()) {
					num = num + obj.len;
				}
			}
		}
		return num;
	}

	private int indexOf(ASTStringObject target, ASTStringObject start) {
		int num = 0;

		if (target != this.head) {
			for (ASTStringObject obj = start; obj != target; obj = obj.next) {
				if (obj.visible()) {
					num = num + obj.len;
				}
			}
		}
		return num;
	}

	public ASTStringObject find(ASTStringObject prev, TimestampedOperation top) {
		ASTStringObject preObj = prev;
		ASTStringObject cur = prev.next;
		while (cur != this.tail) {
			if (cur.appeared(top)) {
				break;
			} else if (!cur.priorityBySid(top) && (preObj.next == cur || ASTObject.casualBefore(cur, preObj.next)))// cur
																													// is
																													// causal
																													// before
																													// preObj
			{
				preObj = cur;

			}
			cur = cur.next;
		}
		return preObj;
	}

	public void addAfter(ASTStringObject newObj, ASTStringObject prev) {
		newObj.next = prev.next;
		prev.next = newObj;
	}

	private void slice(ASTStringObject start, int len, TimestampedOperation top) {
		for (ASTStringObject obj = start; len > 0 && obj != this.tail; obj = obj.next) {
			if (obj.visible(top)) {
				obj.split(len);
				obj.mark(top);
				len = len - obj.len;
			}
		}
	}

	private LinkedList<ASTStringObject> slice2(ASTStringObject start, int len, TimestampedOperation top) {
		LinkedList<ASTStringObject> markedStringObject = new LinkedList<>();
		// System.out.println("top :"+top.toString());
		for (ASTStringObject obj = start; len > 0 && obj != this.tail; obj = obj.next) {
			if (obj.visible(top)) {
				obj.split(len);
				if (obj.visible()) {
					markedStringObject.add(obj);
				}
				obj.mark(top);
				len = len - obj.len;
			}
		}

		return markedStringObject;
	}

	protected void gc(VectorClock minVC) {

		for (ASTStringObject cur = this.head.next; cur != this.tail; cur = cur.next) {
			if (cur.iop != null && cur.iop.getSEQ() <= minVC.get(cur.iop.getSID())) {
				cur.iop = null;
			}

			if (cur.dop != null) {
				for (TimestampedOperation top : cur.dop) {
					if (top.getSEQ() <= minVC.get(top.getSID())) {
						cur.dop = null;
						break;
					}
				}
			}
		}
	}

	protected void merge() {
		ASTStringObject prev = this.head.next;
		ASTStringObject cur = prev.next;
		while (prev != this.tail && cur != this.tail) {
			if (prev.iop == cur.iop && prev.dop == cur.dop) {
				prev.len = prev.len + cur.len;
				prev.next = cur.next;
				cur = cur.next;
			} else {
				prev = prev.next;
				cur = cur.next;
			}
		}
	}

	public int memorySize() {
		int size = 0;
		for (ASTStringObject cur = this.head.next; cur != this.tail; cur = cur.next) {
			size = size + 1;
		}
		return size;
	}

	public void printState() {

		String outputStr = "";

		for (ASTStringObject cur = this.head.next; cur != this.tail; cur = cur.next) {
			if (cur.iop != null) {
				outputStr += "insert ";
				outputStr += "{" + cur.iop.getSID() + "," + cur.iop.getSEQ() + "}";
				outputStr += "{len: " + cur.len + "}";
			} else {
				outputStr += "origin";
				if (cur.len == 0) {
					outputStr += "{len: NAN}";
				} else {
					outputStr += "{len: " + cur.len + "}";
				}
			}

			outputStr += " ";
			if (cur.dop != null) {

				for (TimestampedOperation top : cur.dop) {
					outputStr += "delete";
					outputStr += "{" + top.getSID() + "," + top.getSEQ() + "}";

					if (cur.len == 0) {
						outputStr += " {len: NAN}";
					} else {
						outputStr += "{len: " + cur.len + "}";
					}
					outputStr += " ";
				}
			}
			outputStr += "\n";
		}

		System.out.println(outputStr);
	}
}