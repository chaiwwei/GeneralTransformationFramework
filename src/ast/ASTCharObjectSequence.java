package ast;

import java.util.LinkedList;

import com.DelParas;
import com.TimestampedOperation;
import com.VectorClock;

public class ASTCharObjectSequence {
	public ASTCharObject head;
	public ASTCharObject tail;

	public ASTCharObjectSequence(String content) {

		this.tail = new ASTCharObject(null, null);
		this.head = new ASTCharObject(null, this.tail);

		if (content != null && content.length() > 0) {
			int len = content.length();
			ASTCharObject prev = this.head;
			for (int i = 0; i < len; i++) {
				ASTCharObject obj = new ASTCharObject(null, this.tail);
				prev.next = obj;
				prev = obj;
			}
		}
	}

	public void localInsert(int pos, String str, TimestampedOperation top) {
		ASTCharObject obj = new ASTCharObject(top, this.tail);
		ASTCharObject prev = this.findPrevObjByPos(pos, null);
		obj.next = prev.next;
		prev.next = obj;
	}

	public int remoteInsert(int pos, String str, TimestampedOperation top) {
		ASTCharObject obj = new ASTCharObject(top, this.tail);
		ASTCharObject prev = this.findPrevObjByPos(pos, top);
		this.add(prev, obj, top);
		return this.indexOf(obj);
	}

	public void localDelete(int pos, int len, TimestampedOperation top) {
		ASTCharObject obj = this.findObjByPos(pos, null);
		obj.mark(top);
	}

	public LinkedList<DelParas> remoteDelete(int pos, int len, TimestampedOperation top) {
		LinkedList<DelParas> result;
		ASTCharObject obj = this.findObjByPos(pos, top);
		if (obj.visible() == false) {
			result = new LinkedList<DelParas>();
		} else {
			int tpos = this.indexOf(obj);
			result = new LinkedList<DelParas>();
			result.add(new DelParas(tpos, len));
		}
		obj.mark(top);
		return result;
	}

	private ASTCharObject findPrevObjByPos(int pos, TimestampedOperation top) {
		if (pos == 0) {
			return this.head;
		}
		ASTCharObject obj = this.head.next;
		for (int num = 0; obj != this.tail; obj = obj.next) {
			if (obj.visible(top)) {
				if (pos == num + 1) {
					break;
				} else {
					num = num + 1;
				}
			}
		}

		return obj;
	}

	private ASTCharObject findObjByPos(int pos, TimestampedOperation top) {
		ASTCharObject obj = this.head.next;
		for (int num = 0; obj != this.tail; obj = obj.next) {
			if (obj.visible(top)) {
				if (pos == num) {
					break;
				} else {
					num = num + 1;
				}
			}
		}

		return obj;
	}

	private void add(ASTCharObject start, ASTCharObject obj, TimestampedOperation top) {

		ASTCharObject prev = start;
		ASTCharObject cur = prev.next;
		while (cur != this.tail) {
			if (cur.appeared(top) == true) {
				break;
			} else if (obj.priorityBySVC(cur)) {
				break;
			} else {
				prev = cur;
			}
			cur = cur.next;
		}
		obj.next = cur;
		prev.next = obj;
	}

	private int indexOf(ASTCharObject target) {
		int num = 0;
		for (ASTCharObject obj = this.head.next; obj != target; obj = obj.next) {
			if (obj.visible()) {
				num = num + 1;
			}
		}
		return num;
	}

	public void clear(VectorClock minVC) {
		ASTCharObject cur = this.head.next;
		while (cur != this.tail) {
			if (cur.iop.getSEQ() <= minVC.get(cur.iop.getSID())) {
				cur.iop = null;
			}

			for (TimestampedOperation top : cur.dop) {
				if (top.getSEQ() <= minVC.get(cur.iop.getSID())) {
					cur.dop = null;
					break;
				}
			}

			cur = cur.next;
		}
	}

	public int memorySize() {
		int size = 0;
		ASTCharObject cur = this.head.next;
		while (cur != this.tail) {

			size = size + 1;

			cur = cur.next;
		}
		return size;
	}
}