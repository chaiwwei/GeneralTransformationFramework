package crdt;

import com.GTAlgorithm;
import com.OpType;
import com.StringOperation;
import com.TimestampedOperation;
import com.VectorClock;

public class RGA extends GTAlgorithm {
	RGACharObjectSequence os;

	public RGA(int sid, int num, String content) {
		super(sid, num);
		// TODO Auto-generated constructor stub
		this.os = new RGACharObjectSequence(content);
	}

	@Override
	public StringOperation ROH(TimestampedOperation top) {
		// TODO Auto-generated method stub
		VectorClock vc = top.getVC();
		int sid = top.getSID();
		RGAId id = RGAId.from(vc, sid);
		StringOperation sop = null;
		RGAOperation rop = RGAOperation.fromString(top.getCustomizedOpStr(), id);
		if (rop.type == OpType.ins) {
			RGAInsOperation iop = (RGAInsOperation) rop;
			sop = this.os.insert(iop.prevId, iop.id, iop.content);
		} else {
			RGADelOperation dop = (RGADelOperation) rop;
			sop = this.os.delete(dop.id, dop.idList);
		}
		// this.remoteTimeUpdate(rop.id.sid,rop.vc);
		this.remoteTimeUpdate(sid, vc);
		this.os.clear(this.mclock.getMinVClock());
		return sop;
	}

	@Override
	public String LOH(StringOperation sop) {
		// TODO Auto-generated method stub
		this.localTimeUpdate();
		RGAOperation rop = null;
		RGAId newId = this.createId();
		if (sop.type == OpType.ins) {
			rop = this.os.insert(newId, sop.pos, sop.str);
		} else {
			rop = this.os.delete(newId, sop.pos);
		}
		return rop.toString();
	}

	public RGAId createId() {

		return new RGAId(this.getVClock().getSum(), this.sid, this.getSeq(this.sid));
	}

	public int memorySize() {
		return this.os.memorySize();
	}

	public void visualizeOS() {
		this.os.visualize();
	}

}