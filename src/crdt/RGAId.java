package crdt;

import com.VectorClock;

public class RGAId implements Comparable<RGAId>{
	public int svc;
	public int sid;
	public int seq;

	public RGAId(int svc, int sid, int seq) {
		this.svc = svc;
		this.sid = sid;
		this.seq = seq;
	}
	
	public static RGAId from(VectorClock vc, int sid)
	{
		return new RGAId(vc.getSum(),sid,vc.get(sid));
	}

	public String toString() {
		return this.svc + "," + this.sid + "," + this.seq;
	}

	public static RGAId fromString(String svc, String sid, String seq) {
		return new RGAId(Integer.parseInt(svc), Integer.parseInt(sid), Integer.parseInt(seq));
	}

	@Override
	public int compareTo(RGAId id2) {
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
        final RGAId other = (RGAId) obj;

        if (this.svc != other.svc) {
            return false;
        }
        if (this.sid != other.sid) {
            return false;
        }
        if (this.seq != other.seq) {
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
    
    public RGAId deepCopy()
    {
    	return new RGAId(this.svc, this.sid, this.seq);
    }
}
