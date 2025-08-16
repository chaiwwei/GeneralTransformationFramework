package ttf;

import com.AbstractOperation;
import com.GTAlgorithm;
import com.OpType;
import com.StringOperation;
import com.TimeStampedOpString;

import ot.POT;

public class TTF extends GTAlgorithm{
	TTFObjectSequence os;
	POT ot;
	
	public TTF(int sid, int num, String content) {
		super(sid, num);
		this.ot=new POT(sid, num, new TTFTransformer());
		this.os=new TTFObjectSequence(content);
		// TODO Auto-generated constructor stub
	}

	@Override
	public StringOperation ROH(AbstractOperation aop) {
		// TODO Auto-generated method stub
		StringOperation sop=this.ot.ROH(aop);

		
		if(sop.type==OpType.ins)
		{
			sop.pos = this.os.insert(sop.pos);
			
		}else
		{
			sop.pos = this.os.delete(sop.pos);
			if(sop.pos==-1)
			{
				sop.subOps.clear();
			}
		}
		return sop;
	}

	@Override
	public AbstractOperation LOH(StringOperation sop) {
		// TODO Auto-generated method stub
		if(sop.type==OpType.ins)
		{
			sop.pos = this.os.insert(sop.pos);
			
		}else
		{
			sop.pos = this.os.delete(sop.pos);
		}
	
		return 	this.ot.LOH(sop);
	}

	@Override
	public int memorySize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void receiveLocal(TimeStampedOpString aop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StringOperation ROH(TimeStampedOpString aop) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
