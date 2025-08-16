package crdt;

public class RGASId implements Comparable<RGASId> {
	public int svc;
	public int sid;
	public int seq;
	public int offset;

	public RGASId(int svc, int sid, int seq, int offset) {
		this.svc = svc;
		this.sid = sid;
		this.seq = seq;
		this.offset = offset;
	}

	public String toString() {
		return this.svc + "," + this.sid + "," + this.seq + "," + this.offset;
	}

	public static RGASId fromString(String svc, String sid, String seq, String offset) {
		return new RGASId(Integer.parseInt(svc), Integer.parseInt(sid), Integer.parseInt(seq),
				Integer.parseInt(offset));
	}

	@Override
	public int compareTo(RGASId id2) {
		// TODO Auto-generated method stub
		if (this.svc == id2.svc) {
			if (this.sid == id2.sid) {
				return this.seq - id2.seq;
			} else {
				return this.sid - id2.sid;
			}
		} else {
			return this.svc - id2.svc;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RGASId other = (RGASId) obj;

		if (this.svc != other.svc) {
			return false;
		}
		if (this.sid != other.sid) {
			return false;
		}
		if (this.seq != other.seq) {
			return false;
		}
		if (this.offset != other.offset) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + this.svc;
		hash = 79 * hash + this.sid;
		hash = 79 * hash + this.seq;
		return hash;
	}
}
