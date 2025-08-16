package ast;

import com.VectorClock;

public class BorderObjectSequence extends ASTStringObjectSequence {

	public BorderObjectSequence(String content) {
		super(content);
	}

	public void addBetween(ASTStringObject newObj, ASTStringObject prev, ASTStringObject next, VectorClock vc) {
		/*
		 * ASTStringObject preObj = prev; ASTStringObject cur = prev.next; while (cur !=
		 * next) { if (cur.appeared(vc) == true) { break; } else if (newObj.compare(cur)
		 * > 0) { break; } else { preObj = cur; } cur = cur.next; } newObj.next =
		 * preObj.next; preObj.next = newObj;
		 */
	}


}