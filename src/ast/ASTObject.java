package ast;

import java.util.ArrayList;

import com.TimestampedOperation;

public class ASTObject {
	public TimestampedOperation iop;
	public ArrayList<TimestampedOperation> dop;
	public int len;

	public ASTObject() {
		this.iop = null;
		this.dop = null;
		this.len = 0;
	}

	public ASTObject(TimestampedOperation iop, int len) {
		this.iop = iop;
		this.dop = null;
		this.len = len;
	}

	public boolean priorityBySVC(ASTObject obj) {
		if (this.iop == null) {
			return false;
		}

		if (obj.iop == null) {
			return true;
		}

		return this.iop.isCasualBefore(obj.iop);
	}

	public boolean priorityBySid(ASTObject obj) {

		int result = this.iop.getSID() - obj.iop.getSID();

		if (result == 0) {
			result = this.iop.getSEQ() - obj.iop.getSEQ();
		}

		return result > 0 ? true : false;
	}
	
	public boolean priorityBySid(TimestampedOperation top) {

		int result = this.iop.getSID() - top.getSID();

		if (result == 0) {
			result = this.iop.getSEQ() - top.getSEQ();
		}

		return result > 0 ? true : false;
	}

	public void mark(TimestampedOperation id) {
		if (this.dop != null) {
			this.dop.add(id); // item includes the vector clock of delete operations
		} else {
			this.dop = new ArrayList<>();
			this.dop.add(id);
		}
	}

	// obj1 appears before obj2;
	// obj1.iop and obj2.iop cannot both be null.
	public static boolean casualBefore(ASTObject obj1, ASTObject obj2) {
		boolean result = false;

		if (obj1.iop == null) {
			result = true;
		} else if (obj2.iop == null) {
			result = false;
		} else {
			result = obj1.iop.isCasualBefore(obj2.iop);
		}

		return result;
	}

	public boolean appeared(TimestampedOperation top) {

		return this.iop == null || this.iop.isCasualBefore(top);
	}

	public boolean deleted(TimestampedOperation top) {
		boolean result = false;
		if (this.dop != null) {
			for (TimestampedOperation topx : this.dop) {
				if (topx.isCasualBefore(top)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	public boolean visible() {
		return this.dop == null;
	}

	public boolean visible(TimestampedOperation top) {
		return this.appeared(top) && !this.deleted(top);
	}
}