package com.powerdata.openpa.psse;

import com.powerdata.openpa.tools.PAMath;

public abstract class SwShuntBlkList extends PsseBaseInputList<SwShuntBlk>
{
	public SwShuntBlkList(PsseInputModel model) {super(model);}

	/* Standard object retrieval */

	/** Get a SwitchedShuntBlock by it's index. */
	@Override
	public SwShuntBlk get(int ndx) { return new SwShuntBlk(ndx,this); }
	/** Get a SwitchedShuntBlock by it's ID. */
	@Override
	public SwShuntBlk get(String id) { return super.get(id); }

	/* convenience methods */

	public abstract float getShuntB(int ndx) throws PsseModelException;

	/* convenience defaults */
	
	public float getDeftShuntB(int ndx) throws PsseModelException {return PAMath.mvar2pu(getB(ndx));}

	
	/* raw methods */

	public abstract int getN(int ndx) throws PsseModelException;
	public abstract float getB(int ndx) throws PsseModelException;


	/* defaults */
	
	public int getDeftN(int ndx) {return 0;}
	public float getDeftB(int ndx) {return 0F;}

}
