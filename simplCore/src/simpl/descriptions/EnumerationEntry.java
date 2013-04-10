package simpl.descriptions;

import java.util.Collection;

import simpl.annotations.dbal.simpl_scalar;

/**
 * A class to represent entries in an enumeration
 * @author tom
 *
 */
public class EnumerationEntry implements IMetaInformationProvider{

	/**
	 * The name of the entry.
	 */
	@simpl_scalar
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * The integer value of the entry; may not be set if the compiler is delegated to that task.
	 */
	@simpl_scalar
	private Integer value;
	private MetaInformationCollection metainfo;
	
	public EnumerationEntry(){}
	
	public EnumerationEntry(String name)
	{
		this.name = name;
		this.metainfo = new MetaInformationCollection();
	}
	
	public EnumerationEntry(String name, Integer value)
	{
		this.name = name;
		this.value = value;
		this.metainfo = new MetaInformationCollection();
	}

	@Override
	public void addMetaInformation(MetaInformation imo) {
		this.metainfo.addMetaInformation(imo);
	}

	@Override
	public Collection<MetaInformation> getMetaInformation() {
		return this.metainfo.getMetaInformation();
	}

	@Override
	public boolean containsMetaInformation(String name) {
		return this.metainfo.containsMetaInformation(name);
	}

	@Override
	public MetaInformation getMetaInformation(String name) {
		return this.metainfo.getMetaInformation(name);
	}
}
