package crdt;

public class RGACharObject {
    public RGACharObject next;
    public RGAId dop;
    public RGAId id;
    public String str;

    public RGACharObject(RGAId iop, String str) {
        this.next = null;
        this.dop = null;
        this.id = iop;
        this.str=str;
    }

    public boolean isVisible() {
        return this.dop == null;
    }

    public RGADelPara mark(RGAId rgaDel) {
        this.dop = rgaDel;
        return new RGADelPara(this.id, 0, 1);

    }
}