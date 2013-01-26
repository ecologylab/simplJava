package simpl.descriptions;

import simpl.annotations.dbal.simpl_scalar;

/**
 * A class to represent entries in an enumeration
 * @author tom
 *
 */
public class EnumerationEntry {

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
	
	public EnumerationEntry(){}
	
	public EnumerationEntry(String name)
	{
		this.name = name;
	}
	
	public EnumerationEntry(String name, Integer value)
	{
		this.name = name;
		this.value = value;
	}
}
