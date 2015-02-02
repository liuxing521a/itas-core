package net.itas.core.resource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.itas.core.Pool;
import net.itas.core.Script;
import net.itas.core.enums.EByte;
import net.itas.core.enums.EString;
import net.itas.core.util.GenericUtil;
import net.itas.core.util.Utils.EnumUtils;
import net.itas.util.ItasException;
import net.itas.util.Utils.ClassUtils;

abstract class AbstractXml {
	
	@SuppressWarnings("unchecked")
	void fillField(Field field, String text, Map<String, Method> scripts) throws Exception {
		try {
			if (field.getType() == byte.class)  {
				field.setByte(this, Integer.valueOf(text).byteValue());
			} else if (field.getType() == short.class) {
				field.setShort(this, Short.valueOf(text));
			} else if (field.getType() == int.class) {
				field.setInt(this, Integer.valueOf(text));
			} else if (field.getType() == long.class) {
				field.setLong(this, Long.valueOf(text));
			} else if (field.getType() == float.class) {
				field.setFloat(this, Float.valueOf(text));
			} else if (field.getType() == double.class) {
				field.setDouble(this, Double.valueOf(text));
			} else if (field.getType() == char.class) {
				field.setChar(this, text.charAt(0));
			} else if (field.getType() == boolean.class) {
			    field.setBoolean(this, getBoolean(text));
			} else if (field.getType() == String.class) {
				field.set(this, text);
			} else if (ClassUtils.isExtends(field.getType(), Collection.class)) {
				field.set(this, GenericUtil.processCollection(field, text));
			} else if (ClassUtils.isExtends(field.getType(), Map.class)) {
				Map<Object, Object> map = GenericUtil.processorMap(field, text);
				field.set(this, Collections.unmodifiableMap(map));
			} else if (ClassUtils.isExtends(field.getType(), Resource.class)) {
				field.set(this, Pool.getResource(text));
			} else if (Script.class.isAssignableFrom(field.getType())) {
				Script script = (Script) field.getType().newInstance();
				script.setMethod(scripts.get(field.getName()));
				field.set(this, script);
			} else if (ClassUtils.isExtends(field.getType(), Enum.class)) {
				if (ClassUtils.isExtends(field.getType(), EByte.class)) {
					field.set(this, EnumUtils.parse((Class<? extends EByte>)field.getType(), Byte.valueOf(text)));
				} else if (ClassUtils.isExtends(field.getType(), EString.class)) {
					field.set(this, EnumUtils.parse((Class<? extends EString>)field.getType(), text));
				} else {
					throw new RuntimeException("unSupported:[type:" + field.getType() + "]");
				}
			} else {
				throw new RuntimeException("unSupported:[type:" + field.getType() + "]");
			}
		} catch (Exception e) {
			 throw new ItasException("field=" + field.getName() + ", value=" + text, e);
		}
	}
	
	private boolean getBoolean(String text) {
         return Integer.parseInt(text) != 0;
	}
	
}