package ot;

import java.util.ArrayList;

public interface IGetOpList {

	public abstract ArrayList<OTOperation> getCausalOps(OTOperation op1,OTOperation op2);

}
