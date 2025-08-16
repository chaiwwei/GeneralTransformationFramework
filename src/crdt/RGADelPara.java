package crdt;

public class RGADelPara {
	public RGAId id;
	public int offset;
	public int length;

	public RGADelPara(RGAId id, int offset, int length) {
		this.id = id;
		this.offset = offset;
		this.length = length;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id.toString());
		sb.append(",");
		sb.append(this.offset);
		sb.append(",");
		sb.append(this.length);
		return sb.toString();
	}
}
