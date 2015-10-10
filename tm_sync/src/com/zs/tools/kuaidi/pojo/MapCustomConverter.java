package com.zs.tools.kuaidi.pojo;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



public class MapCustomConverter  {

//	public MapCustomConverter(Mapper mapper) {
//		super(mapper);
//	}
//
//	@SuppressWarnings("rawtypes")
//	public boolean canConvert(Class type) {
//		// 这里只列了HashMap一种情况
//		return type.equals(HashMap.class);
//	}
//
//	@SuppressWarnings("rawtypes")
//	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
//		Map map = (Map) source;
//		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
//			Entry entry = (Entry) iterator.next();
//			ExtendedHierarchicalStreamWriterHelper.startNode(writer, entry.getKey().toString(), Entry.class);
//
//			writer.setValue(entry.getValue().toString());
//			writer.endNode();
//		}
//	}
//
//	@SuppressWarnings("rawtypes")
//	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
//		Map map = (Map) createCollection(context.getRequiredType());
//		populateMap(reader, context, map);
//		return map;
//
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map) {
//		while (reader.hasMoreChildren()) {
//			reader.moveDown();
//			Object key = reader.getNodeName();
//			Object value = reader.getValue();
//			map.put(key, value);
//			reader.moveUp();
//		}
//	}

}