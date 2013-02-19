package simpl.descriptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetaInformationCollection implements IMetaInformationProvider {

	private Map<String, MetaInformation> metaInfoMap;
	
	public MetaInformationCollection()
	{
		this.metaInfoMap = new HashMap<String, MetaInformation>();
	}
	
	@Override
	public void addMetaInformation(MetaInformation imo) {
		this.metaInfoMap.put(imo.getAnnotationName(), imo);
	}

	@Override
	public Collection<MetaInformation> getMetaInformation() {
		// TODO Auto-generated method stub
		return this.metaInfoMap.values();
	}

	@Override
	public boolean containsMetaInformation(String name) {
		// TODO Auto-generated method stub
		return this.metaInfoMap.containsKey(name);
	}

	@Override
	public MetaInformation getMetaInformation(String name) {
		// TODO Auto-generated method stub
		return this.metaInfoMap.get(name);
	}
}
