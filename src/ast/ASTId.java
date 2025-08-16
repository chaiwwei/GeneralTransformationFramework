package ast;

public class ASTId {
	public int svc;
	public int sid;
	public int seq;

	public ASTId(int svc, int sid, int seq) {
		this.svc = svc;
		this.sid = sid;
		this.seq = seq;
	}

	public String toString() {
		return "{svc: " + this.svc + " sid: " + this.sid + " seq: " + this.seq + "}";
	}

	public boolean equals(Object obj) {

		// 检查是否为同一个对象的引用
		if (this == obj)
			return true;

		// 检查是否为null以及是否为正确的类型
		if (obj == null || getClass() != obj.getClass())
			return false;

		// 向下转型为当前类的类型
		ASTId id = (ASTId) obj;

		return id.svc == this.svc && id.sid == this.sid && id.seq == this.seq;
	}
}
