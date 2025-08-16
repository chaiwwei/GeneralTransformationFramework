package crdt;

import java.util.HashMap;
import java.util.LinkedList;

import com.DelParas;
import com.StringOperation;
import com.VectorClock;

public class RGACharObjectSequence {
	public RGACharObject tail = new RGACharObject(null,"%");
	public RGACharObject head = new RGACharObject(new RGAId(-1, 0, 0), "#");
	private HashMap<RGAId,RGACharObject> map=new HashMap<RGAId,RGACharObject>();

	public RGACharObjectSequence(String content) {
		this.head.next = this.tail;
		if (content != null && content.length() > 0) {
			int len = content.length();
			RGACharObject prev = this.head;
			for (int i = 0; i < len; i++) {
				RGACharObject obj = new RGACharObject(new RGAId(-1, 0, i + 1),content.substring(i,i+1));
				addAfter(prev, obj);
				prev = obj;
				map.put(obj.id,obj);
			}
		}
	}

	public void addAfter(RGACharObject prev, RGACharObject obj) {
		obj.next = prev.next;
		prev.next = obj;
	}

	public RGAInsOperation insert(RGAId newId, int pos, String str) {
		RGACharObject obj = new RGACharObject(newId,str);
		RGACharObject prev = this.findPrevObjByPos(pos);
		addAfter(prev, obj);
		this.map.put(obj.id,obj);
		return new RGAInsOperation(prev.id, 0, newId, str);
	}

	public StringOperation insert(RGAId prevId, RGAId newId, String str) {

		RGACharObject obj = new RGACharObject(newId,str);
		RGACharObject prev = this.findObjById(prevId);
		while (true) {
			if (prev.next == this.tail) {
				break;
			} else if (this.compare(newId, prev.next.id) > 0) {
				break;
			} else {
				prev = prev.next;
			}
		}

		addAfter(prev, obj);
		int pos = this.indexOf(obj);
		this.map.put(obj.id,obj);
		return StringOperation.createInsOperation(pos, str);
	}

	public RGADelOperation delete(RGAId newId, int pos) {
		RGADelOperation rop = new RGADelOperation(newId);
		RGACharObject target = this.findObjByPos(pos);
		rop.add(target.mark(newId));
		if(rop.idList.isEmpty())
		{
			System.out.println("local rop idList Empty");
		}
		
		return rop;
	}

	public StringOperation delete(RGAId newId, LinkedList<RGADelPara> idList) {
		StringOperation sop = StringOperation.createDelOperation(new LinkedList<DelParas>());
		RGADelPara firstRGADel = idList.pollFirst();
		RGACharObject target = this.findObjById(firstRGADel.id);
		if (target.isVisible() == true) {
			target.mark(newId);
			sop.add(new DelParas(this.indexOf(target), 1));
		}
		return sop;
	}

	public int indexOf(RGACharObject endObject, RGACharObject startObject) {
		int pos = 0;
		for (RGACharObject cur = startObject; cur != endObject; cur = cur.next) {
			if (cur.isVisible() == true) {
				pos = pos + 1;
			}
		}
		return pos;
	}

	public int indexOf(RGACharObject endObject) {
		int pos = 0;
		for (RGACharObject cur = this.head.next; cur != endObject; cur = cur.next) {
			if (cur.isVisible() == true) {
				pos = pos + 1;
			}
		}
		return pos;
	}

	public RGACharObject findPrevObjByPos(int pos) {
		RGACharObject target = this.head;
		if (pos > 0) {
			int num = 0;
			RGACharObject cur = this.head.next;
			for (; cur != tail; cur = cur.next) {
				if (cur.isVisible() == true) {
					if (num + 1 == pos) {
						break;
					}
					num = num + 1;
				}
			}
			target = cur;
		}
		return target;
	}

	public RGACharObject findObjByPos(int pos) {
		int num = 0;
		RGACharObject cur = this.head.next;
		for (; cur != tail; cur = cur.next) {
			if (cur.isVisible() == true) {
				if (num == pos) {
					break;
				}
				num = num + 1;
			}
		}
		return cur;
	}

	public RGACharObject findObjById(RGAId id) {
		
		if(id.compareTo(this.head.id)==0)
		{
			return this.head;
		}else {
			return this.map.get(id);
		}
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
		RGACharObject cur = this.head.next;
		RGACharObject prev = this.head;
		int svc = minVC.getSum();
		while (cur != this.tail) {
			if (cur.dop != null && cur.dop.seq <= minVC.get(cur.dop.sid)) {
				// cur is invisible at all sites;
				if (cur.next == this.tail) {
					prev.next = this.tail;
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
		RGACharObject cur = this.head.next;
		while (cur != this.tail) {

			size = size + 1;

			cur = cur.next;
		}
		return size;
	}
	
	public void visualize()
	{
		RGACharObject cur = this.head.next;
		while (cur != this.tail) {
			if(cur.isVisible()==true)
			{
				System.out.print(" "+cur.str+" ");
			}else
			{
				System.out.print(" * ");
			}
			cur = cur.next;
		}
		System.out.println();
	}

}