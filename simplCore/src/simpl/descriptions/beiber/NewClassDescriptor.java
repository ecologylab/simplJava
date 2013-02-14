package simpl.descriptions.beiber;

import java.util.ArrayList;
import java.util.List;

public class NewClassDescriptor implements IClassDescriptor {

	public NewClassDescriptor()
	{
		this.fields = new ArrayList<IFieldDescriptor>();
	}
	
	public Class<?> getJavaClass() {
		return javaClass;
	}
	
	public void setJavaClass(Class<?> javaClass) {
		this.javaClass = javaClass;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	private Class<?> javaClass;
	private String name;
	private List<IFieldDescriptor> fields;
	
	public List<IFieldDescriptor> getFields() {
		return fields;
	}

	public void setFields(List<IFieldDescriptor> fields) {
		this.fields = fields;
	}
	
	
	public void addField(IFieldDescriptor ifd)
	{
		this.fields.add(ifd);
	}
}
