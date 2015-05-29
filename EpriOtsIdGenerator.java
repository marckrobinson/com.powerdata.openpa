package com.powerdata.openpa;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Generate object ID's suitable to the EPRI OTS
 * 
 * @author chris@powerdata.com
 *
 */
public class EpriOtsIdGenerator
{
	List<Exception> _errs = new ArrayList<>(0);
	/** MCS from EXPDIMA */
	static final int _MCS = 25;
	/** MCE from EXPDIMA */
	static final int _MCE = 32;
	/** Formatter for most names */
	static final String _GenFmt = String.format("%%2s%%-%dd%%-%dd", _MCS, _MCE);
	
	@FunctionalInterface
	interface FormatFunction
	{
		String fmt(BaseObject obj) throws PAModelException;
	}
	
	static abstract class OneTermFmtFunc implements FormatFunction
	{
		@Override
		public String fmt(BaseObject obj) throws PAModelException
		{
			OneTermDev d = (OneTermDev) obj;
			return String.format(_GenFmt, getCode(), d.getBus().getStation().getKey(), d.getKey());
		}
		protected abstract String getCode();
	}

	static Map<ListMetaType,FormatFunction> _Format = new EnumMap<>(ListMetaType.class);
	static
	{
		_Format.put(ListMetaType.Gen, new OneTermFmtFunc() {@Override protected String getCode() {return "GU";}});
	}
	
	public <T extends BaseObject> List<String> genIds(BaseList<T> list)
	{
		_errs.clear();
		return new AbstractList<String>()
		{
			FormatFunction _formatter = _Format.get(list.getListMeta());
			@Override
			public String get(int index)
			{
				try
				{
					return _formatter.fmt(null);
				}
				catch (PAModelException e)
				{
					e.printStackTrace();
					_errs.add(e);
				}
				return null;
			}

			@Override
			public int size()
			{
				return list.size();
			}
		};
	}
	
	public List<Exception> getErrors() {return _errs;}
	
	
	public static void main(String...args)
	{
		System.out.println(EpriOtsIdGenerator._GenFmt);
	}
}
