package com.powerdata.openpa.tools.matrix;

import com.powerdata.openpa.tools.matrix.Matrix;

public interface JacobianMatrix extends Matrix<JacobianElement>
{
	public static class Element implements JacobianElement
	{
		int _r, _c;
		JacobianMatrix _m;
		protected Element(JacobianMatrix m, int row, int col)
		{
			_m = m;
			_r = row;
			_c = col;
		}
		@Override
		public float getDpda() {return _m.getDpda(_r, _c);}
		@Override
		public float getDpdv() {return _m.getDpdv(_r, _c);}
		@Override
		public float getDqda() {return _m.getDqda(_r, _c);}
		@Override
		public float getDqdv() {return _m.getDqdv(_r, _c);}
		@Override
		public void setDpda(float v) {_m.setDpda(_r, _c, v);}
		@Override
		public void setDpdv(float v) {_m.setDpdv(_r, _c, v);}
		@Override
		public void setDqda(float v) {_m.setDqda(_r, _c, v);}
		@Override
		public void setDqdv(float v) {_m.setDqdv(_r, _c, v);}
		@Override
		public void incDpda(float v) {_m.incDpda(_r, _c, v);}
		@Override
		public void incDpdv(float v) {_m.incDpdv(_r, _c, v);}
		@Override
		public void incDqda(float v) {_m.incDqda(_r, _c, v);}
		@Override
		public void incDqdv(float v) {_m.incDqdv(_r, _c, v);}
		@Override
		public void decDpda(float v) {_m.decDpda(_r, _c, v);}
		@Override
		public void decDpdv(float v) {_m.decDpdv(_r, _c, v);}
		@Override
		public void decDqda(float v) {_m.decDqda(_r, _c, v);}
		@Override
		public void decDqdv(float v) {_m.decDqdv(_r, _c, v);}
		@Override
		public void add(JacobianElement e) {_m.add(_r, _c, e);}
		@Override
		public void sub(JacobianElement e) {_m.sub(_r, _c, e);}
	}

	float getDpda(int row, int column);
	float getDpdv(int row, int column);
	float getDqda(int row, int column);
	float getDqdv(int row, int column);
	void setDpda(int row, int column, float v);
	void setDpdv(int row, int column, float v);
	void setDqda(int row, int column, float v);
	void setDqdv(int row, int column, float v);
	default void incDpda(int row, int column, float v)
	{
		setDpda(row, column, v+getDpda(row, column));
	}
	default void incDpdv(int row, int column, float v)
	{
		setDpdv(row, column, v+getDpdv(row, column));
	}
	default void incDqda(int row, int column, float v)
	{
		setDqda(row, column, v+getDqda(row, column));
	}
	default void incDqdv(int row, int column, float v)
	{
		setDqdv(row, column, v+getDqdv(row, column));
	}
	default void decDpda(int row, int column, float v)
	{
		incDpda(row, column, -v);
	}
	default void decDpdv(int row, int column, float v)
	{
		incDpdv(row, column, -v);
	}
	default void decDqda(int row, int column, float v)
	{
		incDqda(row, column, -v);
	}
	default void decDqdv(int row, int column, float v)
	{
		incDqdv(row, column, -v);
	}
	default void add(int row, int column, JacobianElement e)
	{
		incDpda(row, column, e.getDpda());
		incDpdv(row, column, e.getDpdv());
		incDqda(row, column, e.getDqda());
		incDqdv(row, column, e.getDqdv());
	}
	default void sub(int row, int column, JacobianElement e)
	{
		decDpda(row, column, e.getDpda());
		decDpdv(row, column, e.getDpdv());
		decDqda(row, column, e.getDqda());
		decDqdv(row, column, e.getDqdv());
	}

}
