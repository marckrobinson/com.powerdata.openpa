package com.powerdata.openpa.psse;

public abstract class PsseModel
{	
	public abstract float getSBASE();
	public float getDeftSBASE() {return 100F;}
	
	/** find a Bus by ID */ 
	public Bus getBus(String id) throws PsseModelException
	{
		return getBuses().get(id);
	}
	
	abstract public String getContainerName();
	abstract public BusList<?> getBuses() throws PsseModelException;
	abstract public GeneratorList<?> getGenerators() throws PsseModelException;
	abstract public NontransformerBranchList<?> getNontransformerBranches() throws PsseModelException;
	abstract public TransformerList<?> getTransformers() throws PsseModelException;
	abstract public OwnerList<?> getOwners() throws PsseModelException;
	abstract public AreaInterchangeList<?> getAreas() throws PsseModelException;
	abstract public ZoneList<?> getZones() throws PsseModelException;
	abstract public ImpCorrTblList<?> getImpCorrTables() throws PsseModelException;
	abstract public LoadList<?> getLoads() throws PsseModelException;
}

