package crdt;

import com.GTAlgorithm;
import com.OpType;
import com.StringOperation;
import com.TimestampedOperation;
import com.VectorClock;

public class RGAS extends GTAlgorithm {
	RGAStringObjectSequence os;

	public RGAS(int sid, int num, RGAStringObjectSequence os) {
		super(sid, num);
		// TODO Auto-generated constructor stub
		this.os = os;
	}

	@Override
	public StringOperation ROH(TimestampedOperation top) {
		StringOperation sop = null;
		
		int sid=top.getSID();
		VectorClock vc= top.getVC();
		RGAId id=RGAId.from(vc, sid);
	
		RGAOperation rop = RGAOperation.fromString(top.getCustomizedOpStr(),id);

		if (rop.type == OpType.ins) {

			RGAInsOperation iop = (RGAInsOperation) rop;
			sop = this.os.insert(iop.prevId, iop.prevIdOffset, iop.id, iop.content);
		} else {

			RGADelOperation dop = (RGADelOperation) rop;
			sop = this.os.delete(dop.id, dop.idList);
		}

		//this.remoteTimeUpdate(rop.id.sid, rop.vc);
		this.remoteTimeUpdate(sid,vc);
		this.os.clear(this.mclock.getMinVClock());
		return sop;
	}

	@Override
	public String LOH(StringOperation sop) {
		// TODO Auto-generated method stub
		//System.out.println("site :"+this.sid+" gen:"+sop);
		this.localTimeUpdate();
		RGAOperation rop = null;
		RGAId newId = this.createId();
		// System.out.println(sop.toString());
		//System.out.println("before Loh:");
		//this.os.visualize();
		if (sop.type == OpType.ins) {
			rop = this.os.insert(newId, sop.pos, sop.str, this.getNewVClock());
		} else {
			rop = this.os.delete(newId, sop.pos, sop.len, this.getNewVClock());
		}
		//System.out.println("after Loh:");
		//this.os.visualize();
		return rop.toString();
	}

	public RGAId createId() {
		return new RGAId(this.getVClock().getSum(), this.sid, this.getSeq(this.sid));
	}

	public int memorySize() {
		return this.os.memorySize();
	}

}
