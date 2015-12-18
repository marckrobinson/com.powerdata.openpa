package com.powerdata.openpa.tools.matrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import com.powerdata.openpa.ACBranch;
import com.powerdata.openpa.ACBranchListIfc;
import com.powerdata.openpa.BusList;
import com.powerdata.openpa.BusRefIndex;
import com.powerdata.openpa.PAModel;
import com.powerdata.openpa.PflowModelBuilder;
import com.powerdata.openpa.pwrflow.ACBranchJacobianList;
import com.powerdata.openpa.pwrflow.ACBranchJacobianListI;
import com.powerdata.openpa.tools.PAMath;
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
		public void add(JacobianElement e) {_m.addValue(_r, _c, e);}
		@Override
		public void subtract(JacobianElement e) {_m.subValue(_r, _c, e);}
		@Override
		public String toString()
		{
			return String.format("[%f,%f,%f,%f]", getDpda(), getDpdv(), getDqda(), getDqdv());
		}
	}

	default float getDpda(int row, int column) {return getValue(row, column).getDpda();}
	default float getDpdv(int row, int column) {return getValue(row, column).getDpdv();}
	default float getDqda(int row, int column) {return getValue(row, column).getDqda();}
	default float getDqdv(int row, int column) {return getValue(row, column).getDqdv();}
	default void setDpda(int row, int column, float v) {getValue(row, column).setDpda(v);} 
	default void setDpdv(int row, int column, float v) {getValue(row, column).setDpdv(v);}
	default void setDqda(int row, int column, float v) {getValue(row, column).setDqda(v);}
	default void setDqdv(int row, int column, float v) {getValue(row, column).setDqdv(v);}
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
	@Override
	default void addValue(int row, int column, JacobianElement e)
	{
		incDpda(row, column, e.getDpda());
		incDpdv(row, column, e.getDpdv());
		incDqda(row, column, e.getDqda());
		incDqdv(row, column, e.getDqdv());
	}
	@Override
	default void subValue(int row, int column, JacobianElement e)
	{
		decDpda(row, column, e.getDpda());
		decDpdv(row, column, e.getDpdv());
		decDqda(row, column, e.getDqda());
		decDqdv(row, column, e.getDqdv());
	}

	default void dump(PrintWriter pw, String[] colid, String[] rowid)
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
				pw.format("%f,%f,%f,%f,",
					getDp
			}
		}
	}

	
	public static void main(String...args) throws Exception
	{
		String uri = null;
		File poutdir = new File(System.getProperty("user.dir"));
		for(int i=0; i < args.length;)
		{
			String s = args[i++].toLowerCase();
			int ssx = 1;
			if (s.startsWith("--")) ++ssx;
			switch(s.substring(ssx))
			{
				case "uri":
					uri = args[i++];
					break;
				case "outdir":
					poutdir = new File(args[i++]);
					break;
			}
		}
		if (uri == null)
		{
			System.err.format("Usage: -uri model_uri "
					+ "[ --outdir output_directory (deft to $CWD ]\n");
			System.exit(1);
		}
		final File outdir = poutdir;
		if (!outdir.exists()) outdir.mkdirs();
		PflowModelBuilder bldr = PflowModelBuilder.Create(uri);
		bldr.enableFlatVoltage(false);
		bldr.setLeastX(0.0001f);
		bldr.setUnitRegOverride(false);
		PAModel m = bldr.load();
		BusRefIndex bri = BusRefIndex.CreateFromSingleBuses(m);
		BusList buses = bri.getBuses();
		int nbus = buses.size();
		float[] vm = PAMath.vmpu(buses);
		float[] va = PAMath.deg2rad(buses.getVA());

		JacobianMatrix jm = new ArrayJacobianMatrix(nbus, nbus);
		Set<ACBranchJacobianList> jl = new HashSet<>();
		for(ACBranchListIfc<? extends ACBranch> b : m.getACBranches())
			jl.add(new ACBranchJacobianListI(b, bri).calc(vm, va));
		jl.forEach(i -> i.apply(jm));
		jm.dump(new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "jmtrx.csv")))));
		
	}

}
