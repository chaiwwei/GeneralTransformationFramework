package ot;

import com.OpType;
import com.StringOperation;

public class AbsorbStringTransformer extends AbstractTransformer {

	@Override
	public void SIT(OTOperation o1, OTOperation o2) {
		StringOperation op1 = o1.getSOP();
		StringOperation op2 = o2.getSOP();
		//System.out.println(" before SIT: op1 " + op1.toString() + " op2 " + op2.toString());

		if (op1.len == 0 || op1.pos == -1) {
			return;
		}

		if (op2.len == 0 || op2.pos == -1) {
			return;
		}

		if (op1.type == OpType.ins) {
			if (op2.type == OpType.ins) {
				if (op1.pos > op2.pos) {
					op1.pos = op1.pos + op2.len;
				} else if (op1.pos == op2.pos && o1.getSID() > o2.getSID()) {
					op1.pos = op1.pos + op2.len;
				} else {
					op2.pos = op2.pos + op1.len;
				}
			} else {
				if (op1.pos < op2.pos) {
					op2.pos = op2.pos + op1.len;
				} else if (op1.pos >= (op2.pos + op2.len)) {
					op1.pos = op1.pos - op2.len;
				} else { // op1.pos>=op2.pos and op1.pos<op2.pos+op2.len;
					op1.pos = -1;
					op2.len = op1.len + op2.len;
				}
			}
		} else {
			if (op2.type == OpType.ins) {
				if (op1.pos + op1.len <= op2.pos) {
					op2.pos = op2.pos - op1.len;
				} else if (op1.pos > op2.pos) {
					op1.pos = op1.pos + op2.len;
				} else { // op2.pos>=op1.pos and op2.pos<op1.pos+op1.len
					op1.len = op1.len + op2.len;
					op2.pos = -1;
				}
			} else {
				if (op1.pos + op1.len <= op2.pos) {
					op2.pos = op2.pos - op1.len;
				} else if (op1.pos >= (op2.pos + op2.len)) {
					op1.pos = op1.pos - op2.len;
				} else if (op1.pos < op2.pos && (op1.pos + op1.len <= op2.pos + op2.len)) {
					int overlapping = op1.pos + op1.len - op2.pos;
					op1.len = op1.len - overlapping;
					op2.len = op2.len - overlapping;
					op2.pos = op1.pos;
				} else if (op1.pos >= op2.pos && (op1.pos + op1.len <= op2.pos + op2.len)) {
					op2.len = op2.len - op1.len;
					op1.len = 0;
				} else if (op1.pos >= op2.pos && (op1.pos + op1.len > op2.pos + op2.len)) {
					int overlapping = op2.pos + op2.len - op1.pos;
					op1.len = op1.len - overlapping;
					op2.len = op2.len - overlapping;
					op1.pos = op2.pos;
				} else {
					op1.len = op1.len - op2.len;
					op2.len = 0;
				}
			}
		}

		//System.out.println("after SIT: op1 " + op1.toString() + " op2 " + op2.toString());
	}

}
