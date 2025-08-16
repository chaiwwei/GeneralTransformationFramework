package crdt;

import java.util.LinkedList;

public class ParentStringObject {
	protected class ChildObject {
		RGAStringObject content;
		ChildObject next;
		ChildObject prev;

		public ChildObject(RGAStringObject content) {
			this.content = content;
			this.next = null;
			this.prev = null;
		}
	}

	public RGAId id;
	private ChildObject head;
	private ChildObject tail;

	public ParentStringObject(RGAId id) {
		this.id = id;
		this.head = new ChildObject(null);
		this.tail = new ChildObject(null);
		this.head.next = this.tail;
		this.tail.prev = this.head;
	}

	public LinkedList<RGAStringObject> collect(int offset, int len) {

		
		ChildObject cur = this.head.next;
		LinkedList<RGAStringObject> result = new LinkedList<RGAStringObject>();
		
		while (len > 0) {
			
			RGAStringObject obj = cur.content;
			if (obj.offset == offset) {
				obj.split(len);
				len = len - obj.length;
				offset = offset + obj.length;
				result.add(obj);
			} else if (offset > obj.offset && obj.offset + obj.length > offset) {
				obj.split(offset - obj.offset);
			}

			cur = cur.next;
			if (cur == this.tail) {
				cur = this.head.next;
			}
		}
	
		return result;
	}

	public RGAStringObject searchEndOffset(int offset) {
		ChildObject cur = this.head.next;
		RGAStringObject obj = null;

		while (true) {
			obj = cur.content;
			if (obj.offset + obj.length == offset) { // rgaOp.offset指向prev节点的最后一个character
				break;
			} else if (offset > obj.offset && obj.offset + obj.length > offset) {
				obj.split(offset - obj.offset);
				break;
			}

			cur = cur.next;
			if (cur == this.tail) {
				cur = this.head.next;
			}
		}

		return obj;
	}

	public void addNext(ChildObject prev, RGAStringObject obj) {
		ChildObject newChild = new ChildObject(obj);
		newChild.next = prev.next;
		prev.next.prev = newChild;
		prev.next = newChild;
		newChild.prev = prev;
	}

	public void addLast(RGAStringObject obj) {
		ChildObject newChild = new ChildObject(obj);
		this.tail.prev.next = newChild;
		newChild.next = this.tail;
		newChild.prev = this.tail.prev;
		this.tail.prev = newChild;
	}
}
