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
	void sub(JacobianElement e);
}
