package com.powerdata.openpa.tools.matrix;

import java.io.PrintWriter;

/**
 * A simple array-backed matrix with floating-point values
 * 
 * @author chris@powerdata.com
 *
 */
public class FloatArrayMatrix implements FloatMatrix
{
	float[][] _m;
	
	public FloatArrayMatrix(int nrow, int ncol)
	{
		_m = new float[nrow][ncol];
	}
	
	@Override
	public int getRowCount()
	{
		return _m.length;
	}

	@Override
	public int getColumnCount()
	{
		return _m[0].length;
	}

	@Override
	public void setValue(int row, int column, float value)
	{
		_m[row][column] = value;
	}

	@Override
	public float getValue(int row, int column)
	{
		return _m[row][column];
	}

	@Override
	public void addValue(int row, int column, float value)
	{
		_m[row][column] += value;
	}

	@Override
	public void multValue(int row, int column, float value)
	{
		_m[row][column] *= value;
	}
	
	public void dump(PrintWriter pw, String[] rowid, String[] colid)
	{
		int nc = getColumnCount(), nr = getRowCount();
		for(int i=0; i < nc; ++i)
		{
			pw.print(',');
			pw.print(colid[i]);
		}
		pw.println();
		for(int ir=0; ir < nr; ++ir)
		{
			pw.print(rowid[ir]);
			pw.print(',');
			for(int ic=0; ic < nc; ++ic)
			{
				pw.format("%f", getValue(ir, ic));
			}
			pw.println();
		}
	}

}
