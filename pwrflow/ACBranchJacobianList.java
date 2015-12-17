package com.powerdata.openpa.pwrflow;

import com.powerdata.openpa.PAModelException;
import com.powerdata.openpa.tools.matrix.JacobianElement;

public interface ACBranchJacobianList extends 
	ACBranchExtList<com.powerdata.openpa.pwrflow.ACBranchJacobianList.ACBranchJacobian> 
{
	static public class ACBranchJacobian extends ACBranchExtList.ACBranchExt
	{
		ACBranchJacobianList _list;
		
		ACBranchJacobian(ACBranchJacobianList list, int index)
		{
			super(list, index);
		}
	
		public JacobianElement getFromSelf() {return _list.getFromSelf(_ndx);}
		public JacobianElement getToSelf() {return _list.getToSelf(_ndx);}
		public JacobianElement getFromMutual() {return _list.getFromMutual(_ndx);}
		public JacobianElement getToMutual() {return _list.getToMutual(_ndx);}
		
	}

	JacobianElement getFromSelf(int ndx);
	JacobianElement getToSelf(int ndx);
	JacobianElement getFromMutual(int ndx);
	JacobianElement getToMutual(int ndx);
	@Override
	default ACBranchJacobian get(int index)
	{
		return new ACBranchJacobian(this, index);
	}
	
	void calc(float[] vmpu, float[] varad) throws PAModelException;

}
