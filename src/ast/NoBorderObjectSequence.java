package ast;
import java.util.LinkedList;

import com.VectorClock;


public class NoBorderObjectSequence extends ASTStringObjectSequence {

    public NoBorderObjectSequence(String content) {
        super(content);
    }

    public void addBetween(ASTStringObject newObj, ASTStringObject prev, ASTStringObject next,VectorClock vc) {
		/*
		 * LinkedList < ASTStringObject > conObjs = new LinkedList < ASTStringObject >
		 * (); for (ASTStringObject cur = prev.next; cur != next; cur = cur.next) { if
		 * (cur.appeared(vc) == false) { conObjs.add(cur); } }
		 * 
		 * ASTStringObject preObj = prev; ASTStringObject cur = conObjs.pollFirst();
		 * while (cur != null) { if (newObj.compare(cur) > 0) { break; } else { preObj =
		 * cur; } cur = conObjs.pollFirst(); } newObj.next = preObj.next; preObj.next =
		 * newObj;
		 */
    }

}