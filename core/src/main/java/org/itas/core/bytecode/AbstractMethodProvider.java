//package org.itas.core.bytecode;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import javassist.CtClass;
//import javassist.CtField;
//import javassist.NotFoundException;
//
//import org.itas.core.util.DataBase;
//import org.itas.util.ItasException;
//
///**
// * 方法[method]字节码动态生成
// * @author liuzhen(liuxing521a@gmail.com)
// * @crateTime 2015年2月28日上午10:14:10
// */
//abstract class AbstractMethodProvider implements MethodProvider, DataBase {
//	
//	/**
//	 * 支持属性类型
//	 */
//	private static final Map<CtClass, Type> SUPPORT_TYPE;
//
//	static {
//		Map<CtClass, Type> tmpmap = new HashMap<>();
//		
//		tmpmap.put(javassistType.boolean_, Type.booleanType);
//		tmpmap.put(javassistType.booleanWrap, Type.booleanType);
//		tmpmap.put(javassistType.byte_, Type.byteType);
//		tmpmap.put(javassistType.byteWrap, Type.byteType);
//		tmpmap.put(javassistType.char_, Type.charType);
//		tmpmap.put(javassistType.charWrap, Type.charType);
//		tmpmap.put(javassistType.short_, Type.shortType);
//		tmpmap.put(javassistType.shortWrap, Type.shortType);
//		tmpmap.put(javassistType.int_, Type.intType);
//		tmpmap.put(javassistType.intWrap, Type.intType);
//		tmpmap.put(javassistType.long_, Type.longType);
//		tmpmap.put(javassistType.longWrap, Type.longType);
//		tmpmap.put(javassistType.float_, Type.floatType);
//		tmpmap.put(javassistType.floatWrap, Type.floatType);
//		tmpmap.put(javassistType.double_, Type.doubleType);
//		tmpmap.put(javassistType.doubleWrap, Type.doubleType);
//		tmpmap.put(javassistType.string_, Type.stringType);
//		tmpmap.put(javassistType.simple_, Type.simpleType);
//		tmpmap.put(javassistType.resource_, Type.resourceType);
//		tmpmap.put(javassistType.enumByte_, Type.enumByteType);
//		tmpmap.put(javassistType.enumInt_, Type.enumIntType);
//		tmpmap.put(javassistType.enumString_, Type.enumStringType);
//		tmpmap.put(javassistType.list_, Type.listType);
//		tmpmap.put(javassistType.set_, Type.setType);
//		tmpmap.put(javassistType.map_, Type.mapType);
//		tmpmap.put(javassistType.timeStamp, Type.timeStampType);
//		
//		SUPPORT_TYPE = Collections.unmodifiableMap(tmpmap);
//		
//	}
//	
//	protected CtClass ctClass;
//	
//	protected StringBuffer buffer;
//	
//	@Override
//	public void begin(CtClass clazz) throws Exception {
//		this.ctClass = clazz;
//		this.buffer = new StringBuffer();
//	}
//	
//	protected Type getType(CtField field) throws NotFoundException {
//		Type type = SUPPORT_TYPE.get(field.getType());
//		if (type == null) {
//			throw new ItasException("field type unsupported:" + field.getType());
//		}
//		
//		return type;
//	}
//	
//	@Override
//	public int getAndIncIndex() {
//		return 0;
//	}
//	
//}
