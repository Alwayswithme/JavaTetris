package config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

public class DataInterfaceConfig implements Serializable{
    private static final long serialVersionUID = 3942159714256001928L;
	/*
	 * the data interface's fully qualified name
	 * store in the configure file data element
	 */
	private String className;
		
	/*
	 * map about the database and local data,
	 * use to store some thing like the 
	 * database driver'name, and record file's path
	 */
	private final Map<String, String> paraMap;
	
	public DataInterfaceConfig(Element dataElement) {
		/*
		 * use the data element's
		 * attribute and subelement to init
		 */
		this.className = dataElement.attributeValue("class_name");
		this.paraMap = new HashMap<String, String>();
		@SuppressWarnings( "unchecked")
			List<Element> params = dataElement.elements("param");
		for ( Element e : params) {
			paraMap.put(e.attribute(0).getValue(), e.attribute(1).getValue());
		}
	}
	
	public String getClassName() {
		return className;
	}
	public Map<String, String> getParaMap() {
		return paraMap;
	}
}
