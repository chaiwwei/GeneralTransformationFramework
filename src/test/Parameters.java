package test;

public class Parameters {
	public int numOfSite;
	public int opStrLen;
	public int ratio;
	public int round;
	public int opPerSite;

	public Parameters() {
	}

	public Parameters(int numOfSite, int opStrLen, int ratio, int round, int opPerSite) {
		this.numOfSite = numOfSite;
		this.opStrLen = opStrLen;
		this.ratio = ratio;
		this.opPerSite = opPerSite;
		this.round = round;
	}

	public String toString() {
		return "numOfSite: " + numOfSite + " op length: " + opStrLen + " ration: " + ratio + " opPerSite: " + opPerSite
				+ " round: " + round;
	}
	
	public String name()
	{
		return "Site" + numOfSite + " Length" + opStrLen + " Ration" + ratio + " PerSite" + opPerSite
				+ " Round" + round;
	}
}