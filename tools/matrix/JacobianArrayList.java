package com.powerdata.openpa.tools.matrix;

import java.util.AbstractList;
import java.util.Arrays;

public class JacobianArrayList extends AbstractList<JacobianElement>
		implements JacobianList
{
	float[] _dpda, _dpdv, _dqda, _dqdv;
	float[][] _vals;
	
	public JacobianArrayList(int size)
	{
		_dpda = new float[size];
		_dpdv = new float[size];
		_dqda = new float[size];
		_dqdv = new float[size];
		_vals = new float[][] {_dpda, _dpdv, _dqda, _dqdv};
	}
	
	@Override
	public float getDpda(int ndx) { return _dpda[ndx]; }
	@Override
	public float getDpdv(int ndx) { return _dpdv[ndx]; }
	@Override
	public float getDqda(int ndx) { return _dqda[ndx]; }
	@Override
	public float getDqdv(int ndx) { return _dqdv[ndx]; }
	@Override
	public void setDpda(int ndx, float v) { _dpda[ndx] = v; }
	@Override
	public void setDpdv(int ndx, float v) { _dpdv[ndx] = v; }
	@Override
	public void setDqda(int ndx, float v) { _dqda[ndx] = v; }
	@Override
	public void setDqdv(int ndx, float v) { _dqdv[ndx] = v; }
	@Override
	public JacobianElement get(int index) { return new JacobianList.Element(this, index); }
	@Override
	public int size() { return _dpda.length; }
	@Override
	public void reset()
	{
		for(float[] a : _vals) Arrays.fill(a, 0f);
	}
	
	
	
}
