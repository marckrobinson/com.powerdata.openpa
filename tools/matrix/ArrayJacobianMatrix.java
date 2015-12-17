package com.powerdata.openpa.tools.matrix;

public class ArrayJacobianMatrix implements JacobianMatrix
{
	int _nrow, _ncol;
	JacobianArrayList[] _m;
	
	public ArrayJacobianMatrix(int nrow, int ncol)
	{
		_nrow = nrow;
		_ncol = ncol;
		_m = new JacobianArrayList[ncol];
	}
	
	@Override
	public int getRowCount()
	{
		return _nrow;
	}

	@Override
	public int getColumnCount()
	{
		return _ncol;
	}

	@Override
	public void setValue(int row, int column, JacobianElement value)
	{
		_m[column].set(row, value);
	}

	@Override
	public JacobianElement getValue(int row, int column)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addValue(int row, int column, JacobianElement value)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subValue(int row, int column, JacobianElement value)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getDpda(int row, int column)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDpdv(int row, int column)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDqda(int row, int column)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDqdv(int row, int column)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDpda(int row, int column, float v)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDpdv(int row, int column, float v)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDqda(int row, int column, float v)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDqdv(int row, int column, float v)
	{
		// TODO Auto-generated method stub
		
	}
}
