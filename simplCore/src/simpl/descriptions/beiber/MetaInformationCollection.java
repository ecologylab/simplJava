package simpl.descriptions.beiber;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetaInformationCollection implements IMetaInformationProvider {

	private Map<String, IMetaInformation> metaInfoMap;
	
	public MetaInformationCollection()
	{
		this.metaInfoMap = new HashMap<String, IMetaInformation>();
	}
	
	@Override
	public void addMetaInformation(IMetaInformation imo) {
		this.metaInfoMap.put(imo.getAnnotationName(), imo);
	}

	@Override
	public Collection<IMetaInformation> getMetaInformation() {
		// TODO Auto-generated method stub
		return this.metaInfoMap.values();
	}

	@Override
	public boolean containsMetaInformation(String name) {
		// TODO Auto-generated method stub
		return this.metaInfoMap.containsKey(name);
	}

	@Override
	public IMetaInformation getMetaInformation(String name) {
		// TODO Auto-generated method stub
		return this.metaInfoMap.get(name);
	}
}
