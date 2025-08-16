package ast;

import com.TimestampedOperation;

public class ASTCharObject extends ASTObject {
	ASTCharObject next;

	public ASTCharObject(TimestampedOperation top, ASTCharObject next) {
		super(top, 1);
		this.next = next;
		// TODO Auto-generated constructor stub
	}
}