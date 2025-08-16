package ast;

import java.util.LinkedList;

import com.VectorClock;

public class CaiObjectSequence extends ASTStringObjectSequence{


    public CaiObjectSequence(String content) {
    	super(content);
    }



	/*
	 * public LinkedList < ASTStringObject > findMin(ASTStringObject start,
	 * ASTStringObject end) { //start.next!=end; LinkedList < ASTStringObject > mins
	 * = new LinkedList < ASTStringObject > (); mins.add(start.next);
	 * 
	 * ASTStringObject cur = start.next.next; while (cur != end) { int result =
	 * cur.compare(mins.peekFirst()); if (result < 0) { mins.clear(); mins.add(cur);
	 * } else if (result == 0) { mins.add(cur); } cur = cur.next; } return mins; }
	 */

    public void addBetween(ASTStringObject newObj, ASTStringObject prev, ASTStringObject obj,VectorClock vc) {
		/*
		 * ASTStringObject next = this.findNext(prev, vc); while (prev.next != next) {
		 * LinkedList < ASTStringObject > mins = this.findMin(prev, next); if
		 * (obj.compare(mins.peekFirst()) < 0) { next = mins.peekFirst(); } else { prev
		 * = mins.peekLast(); } } prev.next = obj; obj.next = next;
		 */
    }

	/*
	 * public ASTStringObject findNext(ASTStringObject prev,VectorClock vc) {
	 * ASTStringObject cur = prev.next; while (cur != this.tail) { if
	 * (cur.appeared(vc) == true) { break; } cur = cur.next; } return cur; }
	 */





}