package com;

public abstract class GTAlgorithm {
    // Replica identifier
    protected int sid;
    protected MatrixClock mclock;

    /*
     * Constructor
     */
    public GTAlgorithm(int sid, int num) {
        this.sid = sid;
        this.mclock = new MatrixClock(num);
    }
    
    public abstract StringOperation ROH(TimestampedOperation aop);

    public abstract TimestampedOperation LOH(StringOperation op);

    public void localTimeUpdate() {
        this.mclock.updateSiteVClock(this.sid, this.sid);
    }

    public void remoteTimeUpdate(int sid, VectorClock vclock) {
        this.mclock.updateSiteVClock(sid, vclock);
        this.mclock.updateSiteVClock(this.sid, sid);
    }
   
    
    public VectorClock getVClock()
    {
    	return this.mclock.getVClock(this.sid);
    }
    
    public VectorClock getNewVClock()
    {
    	return this.mclock.getVClock(this.sid).clone();
    }
    
    
    public int getSeq(int sid)
    {
    	VectorClock vclock= this.mclock.getVClock(sid);
    	return vclock.get(sid);
    }
    
    public abstract int memorySize();
    
}