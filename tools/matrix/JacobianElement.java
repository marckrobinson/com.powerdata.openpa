package com.powerdata.openpa.tools.matrix;

public interface JacobianElement
{
	float getDpda();
	float getDpdv();
	float getDqda();
	float getDqdv();
	void setDpda(float v);
	void setDpdv(float v);
	void setDqda(float v);
	void setDqdv(float v);
	void incDpda(float v);
	void incDpdv(float v);
	void incDqda(float v);
	void incDqdv(float v);
	void decDpda(float v);
	void decDpdv(float v);
	void decDqda(float v);
	void decDqdv(float v);
	void add(JacobianElement e);
	void subtract(JacobianElement e);
	
	
	static class JacobianElementContainer implements JacobianElement
	{
		float _dpda, _dpdv, _dqda, _dqdv;
		
		public JacobianElementContainer(float dpda, float dpdv, float dqda, float dqdv)
		{
			_dpda = dpda;
			_dpdv = dpdv;
			_dqda = dqda;
			_dqdv = dqdv;
		}
		
		@Override
		public float getDpda() {return _dpda; }
		@Override
		public float getDpdv() {return _dpdv;}
		@Override
		public float getDqda() {return _dqda;}
		@Override
		public float getDqdv() {return _dqdv;}
		@Override
		public void setDpda(float v) {_dpda = v;}
		@Override
		public void setDpdv(float v) {_dpdv = v;}
		@Override
		public void setDqda(float v) {_dqda = v;}
		@Override
		public void setDqdv(float v) {_dqdv = v;}
		@Override
		public void incDpda(float v) {_dpda += v;}
		@Override
		public void incDpdv(float v) {_dpdv += v;}
		@Override
		public void incDqda(float v) {_dqda += v;}
		@Override
		public void incDqdv(float v) {_dqdv += v;}
		@Override
		public void decDpda(float v) {_dpda -= v;}
		@Override
		public void decDpdv(float v) {_dpdv -= v;}
		@Override
		public void decDqda(float v) {_dqda -= v;}
		@Override
		public void decDqdv(float v) {_dqdv -= v;}
		@Override
		public void add(JacobianElement e) 
		{
			_dpda += e.getDpda();
			_dpdv += e.getDpdv();
			_dqda += e.getDqda();
			_dqdv += e.getDqdv();
		}

		@Override
		public void subtract(JacobianElement e)
		{
			_dpda -= e.getDpda();
			_dpdv -= e.getDpdv();
			_dqda -= e.getDqda();
			_dqdv -= e.getDqdv();
		}
		@Override
		public String toString()
		{
			return String.format("[%f,%f,%f,%f]", _dpda, _dpdv, _dqda, _dqdv);
		}

	}
}
