package crdt;

public class RGAStringObject {
	public int length;
	public int offset;
	public RGAStringObject next;
	public RGAId dop;
	public RGAId id;
	public ParentStringObject pobj;

	public RGAStringObject(RGAId id, int len, ParentStringObject pobj) {
		this.length = len;
		this.next = null;
		this.offset = 0;
		this.dop = null;
		this.id = id;
		this.pobj = pobj;
	}

	public RGADelPara mark(RGAId rgaDel) {
		this.dop = rgaDel;
		return new RGADelPara(this.id, this.offset, this.length);
	}

	public boolean isVisible() {
		return this.dop == null;
	}

	public void split(int startPoint) {
		if (startPoint >= this.length) {
			return;
		}

		RGAStringObject obj = new RGAStringObject(this.id, this.length - startPoint, this.pobj);
		obj.dop = this.dop;
		obj.next = this.next;
		obj.offset = this.offset + startPoint;
		this.length = startPoint;
		this.next = obj;
		this.pobj.addLast(obj);
	}

}