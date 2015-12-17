package com.powerdata.openpa.tools.matrix;

import java.util.List;

public interface JacobianList extends List<JacobianElement>
{
	static class Element implements com.powerdata.openpa.tools.matrix.JacobianElement
	{
		int _ndx;
		JacobianList _list;
		
		Element(JacobianList l, int ndx) {_ndx = ndx; _list=l;}
		@Override
		public float getDpda() {return _list.getDpda(_ndx);}
		@Override
		public float getDpdv() {return _list.getDpdv(_ndx);}
		@Override
		public float getDqda() {return _list.getDqda(_ndx);}
		@Override
		public float getDqdv() {return _list.getDqdv(_ndx);}
		@Override
		public void setDpda(float v) {_list.setDpda(_ndx, v);}
		@Override
		public void setDpdv(float v) {_list.setDpdv(_ndx, v);}
		@Override
		public void setDqda(float v) {_list.setDqda(_ndx, v);}
		@Override
		public void setDqdv(float v) {_list.setDqdv(_ndx, v);}
		@Override
		public void incDpda(float v) {_list.incDpda(_ndx, v);}
		@Override
		public void incDpdv(float v) {_list.incDpdv(_ndx, v);}
		@Override
		public void incDqda(float v) {_list.incDqda(_ndx, v);}
		@Override
		public void incDqdv(float v) {_list.incDqdv(_ndx, v);}
		@Override
		public void decDpda(float v) {_list.decDpda(_ndx, v);}
		@Override
		public void decDpdv(float v) {_list.decDpdv(_ndx, v);}
		@Override
		public void decDqda(float v) {_list.decDqda(_ndx, v);}
		@Override
		public void decDqdv(float v) {_list.decDqdv(_ndx, v);}
		@Override
		public void add(JacobianElement e) {_list.add(_ndx, e);}
		@Override
		public void sub(JacobianElement e) {_list.sub(_ndx, e);}
	}
	
	@Override
	default JacobianElement get(int index)
	{
		return new Element(this, index);
	}

	float getDpda(int ndx);
	float getDpdv(int ndx);
	float getDqda(int ndx);
	float getDqdv(int ndx);
	void setDpda(int ndx, float v);
	void setDpdv(int ndx, float v);
	void setDqda(int ndx, float v);
	void setDqdv(int ndx, float v);
	default void incDpda(int ndx, float v) { setDpda(ndx, getDpda(ndx)+v); }
	default void incDpdv(int ndx, float v) { setDpdv(ndx, getDpdv(ndx)+v); }
	default void incDqda(int ndx, float v) { setDqda(ndx, getDqda(ndx)+v); }
	default void incDqdv(int ndx, float v) { setDqdv(ndx, getDqdv(ndx)+v); }
	default void decDpda(int ndx, float v) { incDpda(ndx, -v); }
	default void decDpdv(int ndx, float v) { incDpdv(ndx, -v); }
	default void decDqda(int ndx, float v) { incDqda(ndx, -v); }
	default void decDqdv(int ndx, float v) { incDqdv(ndx, -v); }
	default void add(int ndx, JacobianElement e)
	{
		incDpda(ndx, e.getDpda());
		incDpdv(ndx, e.getDpdv());
		incDqda(ndx, e.getDqda());
		incDqdv(ndx, e.getDqdv());
	}
	default void sub(int ndx, JacobianElement e)
	{
		decDpda(ndx, e.getDpda());
		decDpdv(ndx, e.getDpdv());
		decDqda(ndx, e.getDqda());
		decDqdv(ndx, e.getDqdv());
	}
	
	/**
	 * reset all the elements to 0
	 */
	void reset();
}
