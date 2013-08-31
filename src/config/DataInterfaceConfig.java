package config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

public class DataInterfaceConfig implements Serializable{
	/**
	 * 
	 */
    private static final long serialVersionUID = 3942159714256001928L;
	/*
	 * 数据接口全限定类名
	 */
	private String className;
	
	public Map<String, String> getParaMap() {
		return paraMap;
	}
	private final Map<String, String> paraMap;
	
	public DataInterfaceConfig(Element dataElement) {
		/*
		 * 根据构造器得到元素，进行初始化
		 * 将该元素转换为对象
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
}