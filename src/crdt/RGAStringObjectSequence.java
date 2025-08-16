package crdt;

import java.util.HashMap;
import java.util.LinkedList;

import com.DelParas;
import com.StringOperation;
import com.VectorClock;

public class RGAStringObjectSequence {
	public RGAStringObject tail = new RGAStringObject(new RGAId(-1, 0, 0), 0, null);
	public RGAStringObject head = new RGAStringObject(new RGAId(-1, 0, 0), 0, null);
	public HashMap<RGAId, ParentStringObject> map = new HashMap<RGAId, ParentStringObject>();

	public RGAStringObjectSequence(String content) {
		this.head.next = this.tail;
		if (content != null && content.length() > 0) {
			RGAId newId = new RGAId(0, 0, 0);
			ParentStringObject pobj = new ParentStringObject(newId);
			RGAStringObject obj = new RGAStringObject(newId, content.length(), pobj);
			pobj.addLast(obj);
			this.map.put(newId, pobj);
			this.addAfter(this.head, obj);
		}
	}

	public void addAfter(RGAStringObject prev, RGAStringObject tar) {
		tar.next = prev.next;
		prev.next = tar;
	}

	public RGAInsOperation insert(RGAId newId, int pos, String str, VectorClock vc) {
		ParentStringObject pobj = new ParentStringObject(newId);
		RGAStringObject obj = new RGAStringObject(newId, str.length(), pobj);
		pobj.addLast(obj);
		this.map.put(newId, pobj);

		RGAInsOperation rop = null;

		if (pos == 0) {
			rop = new RGAInsOperation(this.head.id, 0, newId, str);
			this.addAfter(this.head, obj);
		} else {
			RGAStringObject prev = this.findPrevObjByPos(pos);
			this.addAfter(prev, obj);

			rop = new RGAInsOperation(prev.id, prev.offset + prev.length, newId, str);
		}
		return rop;
	}

	public RGAStringObject findPrevObjByPos(int pos) {
		RGAStringObject target = this.head;
		if (pos > 0) {
			int index = 0;
			RGAStringObject cur = this.head.next;
			for (; cur != tail; cur = cur.next) {
				if (cur.isVisible() == true) {
					if (index + cur.length == pos) {
						break;
					} else if (index < pos && pos < index + cur.length) {
						cur.split(pos - index);
						break;
					}
					index = index + cur.length;
				}
			}
			target = cur;
		}
		return target;
	}

	public StringOperation insert(RGAId prevId, int offset, RGAId newId, String str) {
		ParentStringObject pobj = new ParentStringObject(newId);
		RGAStringObject obj = new RGAStringObject(newId, str.length(), pobj);
		pobj.addLast(obj);

		this.map.put(newId, pobj);

		RGAStringObject prev = this.findPrevObjById(prevId, offset);

		while (true) {
			if (prev.next == this.tail) {
				break;
			} else if (this.compare(newId, prev.next.id) > 0) {
				break;
			} else {
				prev = prev.next;
			}
		}
		
		this.addAfter(prev, obj);

		//this.visualize();
		int pos = this.indexOf(obj, this.head.next);

		return StringOperation.createInsOperation(pos, str);
	}

	public RGAStringObject findPrevObjById(RGAId id, int endOffset)
	{
		RGAStringObject cur = null;

		if (this.compare(this.head.id, id) == 0) {
			cur = this.head;
		} else {
			ParentStringObject pobj = this.map.get(id);
			cur = pobj.searchEndOffset(endOffset);
		}
		return cur;
	}

	public LinkedList<RGAStringObject> findObjsById(RGAId id, int startOffset, int length)
	{
		ParentStringObject pobj = this.map.get(id);
		
		return pobj.collect(startOffset,length);
	}

	public RGADelOperation delete(RGAId newId, int pos, int len, VectorClock vc) {
		RGADelOperation rop = new RGADelOperation(newId);
		RGAStringObject start = this.findPrevObjByPos(pos);
		this.slice(rop, start.next, len, newId);
		return rop;
	}

	public void slice(RGADelOperation rop, RGAStringObject startObj, int num, RGAId newId) {
		for (RGAStringObject cur = startObj; num > 0 && cur != this.tail; cur = cur.next) {
			if (cur.isVisible() == true) {
				cur.split(num);
				rop.add(cur.mark(newId));
				num = num - cur.length;
			}
		}
	}

	public StringOperation delete(RGAId newId, LinkedList<RGADelPara> idList) {
		StringOperation sop = StringOperation.createDelOperation(new LinkedList<DelParas>());
		RGADelPara firstPara = idList.pollFirst();
		while (firstPara != null) {
			this.subDelete(sop, newId, firstPara);
			firstPara = idList.pollFirst();
		}
		return sop;
	}

	private void subDelete(StringOperation sop, RGAId newId, RGADelPara rgaDel) {
		LinkedList<RGAStringObject> markedObjects = this.findObjsById(rgaDel.id, rgaDel.offset, rgaDel.length);
		RGAStringObject obj = markedObjects.pollFirst();
		while (obj != null) {
			if (obj.isVisible() == true) {
				sop.add(new DelParas(this.indexOf(obj, this.head.next), obj.length));
			}
			obj.mark(newId);
			obj = markedObjects.pollFirst();
		}
	}

	public int indexOf(RGAStringObject endObject, RGAStringObject startObject) {

		int pos = 0;
		
		for (RGAStringObject cur = startObject; cur != endObject; cur = cur.next) {

			if (cur.isVisible() == true) {
				pos = pos + cur.length;
			}
		}
		return pos;
	}

	public int compare(RGAId id1, RGAId id2) {
		if (id1.svc == id2.svc) {
			if (id1.sid == id2.sid) {
				return id1.seq - id2.seq;
			} else {
				return id1.sid - id2.sid;
			}
		} else {
			return id1.svc - id2.svc;
		}
	}

	public void clear(VectorClock minVC) {
		RGAStringObject cur = this.head.next;
		RGAStringObject prev = this.head;
		int svc = minVC.getSum();
		//System.out.println("minVC: "+minVC.toString());
		while (cur != this.tail) {
			
			if (cur.dop != null && cur.dop.seq <= minVC.get(cur.dop.sid)) {
				// cur is invisible at all sites;
				if (cur.next == this.tail) {
					prev.next = this.tail;
					break;
				} else if (cur.next.id.svc < svc) {
					prev.next = cur.next;
				} else {
					prev = cur;
				}
			} else {
				prev = cur;
			}
			cur = cur.next;
		}
	}

	public int memorySize() {
		int size = 0;
		RGAStringObject cur = this.head.next;
		while (cur != this.tail) {

			size = size + 1;

			cur = cur.next;
		}
		return size;
	}
	
	public void visualize()
	{
		System.out.println("----------------------------------");
		RGAStringObject cur = this.head.next;
		while (cur != this.tail) {
			System.out.print("{id:"+cur.id+" offset:"+cur.offset+" len:"+cur.length+"}"+"{dop:"+cur.dop+"}");
			System.out.print("\t");
			cur = cur.next;
		}
		System.out.println();
	}

}