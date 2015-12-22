package com.powerdata.openpa.se;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntFunction;
import com.powerdata.openpa.ACBranch;
import com.powerdata.openpa.ACBranchListIfc;
import com.powerdata.openpa.BusList;
import com.powerdata.openpa.BusRefIndex;
import com.powerdata.openpa.PAModel;
import com.powerdata.openpa.PAModelException;
import com.powerdata.openpa.PflowModelBuilder;
import com.powerdata.openpa.pwrflow.ACBranchJacobianList;
import com.powerdata.openpa.pwrflow.ACBranchJacobianListI;
import com.powerdata.openpa.pwrflow.ACBranchJacobianList.ACBranchJacobian;
import com.powerdata.openpa.tools.PAMath;
import com.powerdata.openpa.tools.matrix.ArrayJacobianMatrix;
import com.powerdata.openpa.tools.matrix.FloatArrayMatrix;
import com.powerdata.openpa.tools.matrix.JacobianElement;
import com.powerdata.openpa.tools.matrix.JacobianMatrix;

public class MeasurementMatrix extends FloatArrayMatrix
{
	@FunctionalInterface
	public interface MtrxHeader
	{
		void apply(int index, 
	}
	public MeasurementMatrix(Collection<ACBranchJacobianList> bj, JacobianMatrix mj) throws PAModelException
	{
		super(bj.stream().mapToInt(i -> i.size()).sum()+mj.getRowCount()*2, mj.getColumnCount()*2);
		construct(bj, mj, i -> null, i -> null);
	}
	
	public MeasurementMatrix(Collection<ACBranchJacobianList> bj, JacobianMatrix mj, IntFunction<String> rowid, IntFunction colid) throws PAModelException
	{
		super(bj.stream().mapToInt(i -> i.size()).sum()+mj.getRowCount()*2, mj.getColumnCount()*2);
		
	}
	
	
	void construct(Collection<ACBranchJacobianList> bj, JacobianMatrix mj, IntFunction<String> rowid, IntFunction<String> colid) throws PAModelException
	{
		int nbus = mj.getColumnCount();
		
		int nrow = 0;
		for (ACBranchJacobianList blist : bj)
		{
			for(ACBranchJacobian j : blist)
			{
				JacobianElement fj = j.getFromSelf(), tj = j.getToSelf();
				int fb = j.getFromBus().getIndex(), tb = j.getToBus().getIndex();
				int fb2 = fb+nbus, tb2 = tb+nbus;
				addValue(nrow, fb, fj.getDpda());
				addValue(nrow, tb, tj.getDpda());
				addValue(nrow, fb2, fj.getDpdv());
				addValue(nrow, tb2, tj.getDpdv());
				++nrow;
				
				addValue(nrow, fb, fj.getDqda());
				addValue(nrow, tb, tj.getDqda());
				addValue(nrow, fb2, fj.getDqdv());
				addValue(nrow, tb2, tj.getDqdv());
				++nrow;
			}
		}
		
		for(int irow=0; irow < getRowCount(); ++irow)
		{
			for(int icol=0; icol < getColumnCount(); ++icol)
			{
				int icol2 = icol+nbus;
				JacobianElement e = mj.getValue(irow, icol);
				addValue(nrow, icol, e.getDpda());
				addValue(nrow, icol2, e.getDpdv());
				++nrow;

				addValue(nrow, icol, e.getDqda());
				addValue(nrow, icol2, e.getDqdv());
				++nrow;
			}
		}
	}
	
	static public void main(String...args) throws Exception
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
		
		MeasurementMatrix mm = new MeasurementMatrix(jl, jm, );
		
		String[] busnames = buses.getID();
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "measmtrx.csv"))));
		mm.dump(pw, mkRowIDs(jl, busnames ), mkColId(busnames));
	}

	public static String[] mkColId(String[] busnames)
	{
		int n = busnames.length;
		String[] rv = Arrays.copyOf(busnames, n*2);
		System.arraycopy(busnames, 0, rv, n, n);
		return rv;
	}

	public static String[] mkRowIDs(Set<ACBranchJacobianList> jl, String[] busnames)
	{
		for
	}
}
