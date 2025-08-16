package ast;

import com.TimestampedOperation;

public class ASTStringObject extends ASTObject {
	public ASTStringObject next;

	public ASTStringObject(TimestampedOperation top, int len, ASTStringObject next) {
		super(top, len);
		this.next = next;
	}
	
	// the len 0 means that its length is unlimited.
	public void split(int num) {

		ASTStringObject obj = null;
		if (this.len == 0) {
			obj = deepCopy(0);
		} else  {
			if (this.len <= num) {
				return;
			}
			obj = deepCopy(this.len - num);
		}

		this.len = num;
		this.next = obj;
	}
	
	private ASTStringObject deepCopy(int num)
	{
		ASTStringObject obj = new ASTStringObject(this.iop, num, this.next);
		
		if (this.dop != null) {
			for (TimestampedOperation dopItem : this.dop) {
				obj.mark(dopItem);
			}
		}
		
		return obj;
	}

}