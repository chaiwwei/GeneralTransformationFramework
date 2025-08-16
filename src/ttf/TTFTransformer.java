package ttf;

import com.DelParas;
import com.OpType;
import com.StringOperation;

import ot.AbstractTransformer;
import ot.OTOperation;

public class TTFTransformer extends AbstractTransformer{
	
	 public void SIT(OTOperation op1, OTOperation op2) {
	        StringOperation sop1 = op1.getSOP();
	        StringOperation sop2 = op2.getSOP();

	        if (sop1.type == OpType.ins && sop2.type == OpType.ins) {
	            SIT_II(sop1, sop2, op1.getSID() - op2.getSID());
	        } else if (sop1.type == OpType.del && sop2.type == OpType.del) {
				
	        } else if (sop1.type == OpType.ins && sop2.type == OpType.del) {
	            SIT_ID(sop1, sop2);
	        } else {
	            SIT_ID(sop2, sop1);
	        }
	    }

	    private void SIT_II(StringOperation op1, StringOperation op2, int pri) {
	        if (op1.pos > op2.pos) {
	            op1.pos = op1.pos + 1;
	        } else if (op1.pos == op2.pos) {
	            if (pri > 0) { // op1.sid > op2.sid
	                op1.pos = op1.pos + 1;
	            } else {
	                op2.pos = op2.pos + 1;
	            }
	        } else {
	            op2.pos = op2.pos + 1;
	        }
	    }

	    private void SIT_ID(StringOperation op1, StringOperation op2) {
	        DelParas opx = op2.subOps.peekFirst();
	        if (op1.pos <= opx.pos) {
	            opx.pos = opx.pos + 1;
	        } 
	    }
}
