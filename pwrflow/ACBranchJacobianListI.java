package com.powerdata.openpa.pwrflow;

import com.powerdata.openpa.ACBranch;
import com.powerdata.openpa.ACBranchListIfc;
import com.powerdata.openpa.BusRefIndex;
import com.powerdata.openpa.PAModelException;
import com.powerdata.openpa.tools.Complex;
import com.powerdata.openpa.tools.matrix.JacobianArrayList;
import com.powerdata.openpa.tools.matrix.JacobianElement;
import com.powerdata.openpa.tools.matrix.JacobianList;

public class ACBranchJacobianListI extends ACBranchExtListI<ACBranchJacobianList.ACBranchJacobian>
		implements ACBranchJacobianList
{
	JacobianList _fself;
	JacobianList _fmut;
	JacobianList _tself;
	JacobianList _tmut;
	JacobianList[] _jlist;
	int[] _fbus;
	int[] _tbus;
	
	public ACBranchJacobianListI(ACBranchListIfc<? extends ACBranch> branches, BusRefIndex bri)
			throws PAModelException
	{
		super(branches, bri);
		prep();
	}
	
	public ACBranchJacobianListI(ACBranchExtList<? extends ACBranchExt> copy)
		throws PAModelException
	{
		super(copy);
		prep();
	}
	
	
	
	private void prep() throws PAModelException
	{
		BusRefIndex.TwoTerm t = _bri.get2TBus(_list);
		_fbus = t.getFromBus();
		_tbus = t.getToBus();
		int n = size();
		_fself = new JacobianArrayList(n);
		_tself = new JacobianArrayList(n);
		_fmut = new JacobianArrayList(n);
		_tmut = new JacobianArrayList(n);
		_jlist = new JacobianList[] {_fself, _tself, _fmut, _tmut };
	}
	
	@Override
	public JacobianElement getFromSelf(int ndx) { return _fself.get(ndx); }
	@Override
	public JacobianElement getToSelf(int ndx) { return _tself.get(ndx); }
	@Override
	public JacobianElement getFromMutual(int ndx) { return _fmut.get(ndx); }
	@Override
	public JacobianElement getToMutual(int ndx) { return _tmut.get(ndx); }

	@Override
	public void calc(float[] vm, float[] va) throws PAModelException
	{
		for(JacobianList l : _jlist) l.reset();
		int n = size();
		
		float[] lshift = _list.getShift();
		float[] ftap = _list.getFromTap(), ttap = _list.getToTap();
		float[] bmag = _list.getBmag();
		float[] fbch = _list.getFromBchg(), tbch = _list.getToBchg();
		for(int i=0; i < n; ++i)
		{
			Complex y = _y.get(i);
			float b = y.im(), g = y.re();
			int f = _fbus[i], t = _tbus[i];
			float fvm = vm[f], tvm = vm[t], fva = va[f], tva = va[t];
			float shift = fva - tva - lshift[i];
			float ft = ftap[i], tt = ttap[i], ft2 = ft*ft, tt2 = tt*tt;
			float tprod = 1f/(ft*tt);
			float w = (fvm*tvm)/(ft*tt);
			float dwdvf = tvm * tprod;
			float dwdvt = fvm * tprod;
			float wg = w * g, wb = w * b;
			float cos = (float) Math.cos(shift);
			float sin = (float) Math.sin(shift);
			float gcos = cos * g, gsin = sin * g;
			float bcos = b * cos, bsin = b * sin;
			float bbmag = b + bmag[i];
			float wbcos = wb * cos, wgsin = wg * sin;
			float wgcos = wg * cos, wbsin = wb * sin;
			
			/* From-side active power */
			float tdfpdv = gcos + bsin;
			_fself.decDpdv(i, dwdvf * tdfpdv - 2f * g * fvm / ft2);
			_dfpdtm[i] = dwdvt * tdfpdv;
			float dfpdfa = wbcos - wgsin;
			_fself.decDpda(i, dfpdfa);
			_dfpdta[i] = -_dfpdfa[i]; 

			/* from-side reactive power */
			float tdfqv = gsin - bcos;
			_fself.decDqdv(i, dwdvf * tdfqv + 2f * fvm * (bbmag + fbch[i]) / ft2);
			_dfqdtm[i] = dwdvt * tdfqv;
			float dfqdfa = wgcos + wbsin;
			_fself.decDqda(i, -dfqdfa);
			_dfqdta[i] = -_dfqdfa[i];
			
			/* to-side active power */
			float tdtpdv = gcos - bsin;
			_dtpdfm[i] = dwdvf * tdtpdv;
			_tself.decDpdv(i, dwdvt * tdtpdv - 2f * g * tvm / tt2);
			float dtpdfa = -wgsin - wbcos; 
			_dtpdfa[i] = -wgsin - wbcos;
			_tself.incDpda(i, dtpdfa);
			
			/* to-side reactive power */
			float tdtqdv = gsin + bcos;
			_dtqdfm[i] = -dwdvf * tdtqdv;
			_dtqdtm[i] = -dwdvt * tdtqdv + 2f * tvm * (bbmag + tbch[i]) / tt2;
			_dtqdfa[i] = -wgcos + wbsin;
			_dtqdta[i] = -_dtqdfa[i];
			
		}
			
	}
}
