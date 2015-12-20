package com.powerdata.openpa.se;

import java.util.Collection;
import com.powerdata.openpa.PAModelException;
import com.powerdata.openpa.pwrflow.ACBranchJacobianList;
import com.powerdata.openpa.pwrflow.ACBranchJacobianList.ACBranchJacobian;
import com.powerdata.openpa.tools.matrix.FloatArrayMatrix;
import com.powerdata.openpa.tools.matrix.JacobianMatrix;

public class MeasurementMatrix extends FloatArrayMatrix
{
	public MeasurementMatrix(Collection<ACBranchJacobianList> bj, JacobianMatrix mj) throws PAModelException
	{
		super(bj.stream().mapToInt(i -> i.size()).sum(), mj.getRowCount()*2);
		int nrow = 0;
		for (ACBranchJacobianList blist : bj)
		{
			for(ACBranchJacobian xx : blist)
			{
				addValue(nrow, xx.getFromBus().getIndex(), xx.getFromSelf().getDpda());
				
			}
		}
	}
}
