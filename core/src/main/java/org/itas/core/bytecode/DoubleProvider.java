package org.itas.core.bytecode;

import javassist.CtClass;
import javassist.CtField;

/**
 * double数据[field]类型字节码动态生成
 * @author liuzhen(liuxing521a@gmail.com)
 * @crateTime 2015年2月26日下午5:10:55
 */
class DoubleProvider extends AbstractFieldProvider 
    implements FieldProvider, TypeProvider {

	private static final String STATEMENT_SET = 
			"\t\t" +
			"state.setDouble(%s, get%s());";
	
	private static final String RESULTSET_GET = 
			"\t\t" +
			"set%s(result.getDouble(\"%s\"));";
	
	public static final DoubleProvider PROVIDER = new DoubleProvider();
	
	private DoubleProvider() {
	}
	
	@Override
	public String setStatement(int index, CtField field) {
		return String.format(STATEMENT_SET, index, upCase(field.getName()));
	}

	@Override
	public String getResultSet(CtField field) {
		return String.format(RESULTSET_GET, upCase(field.getName()), field.getName());
	}

	@Override
	public boolean isType(Class<?> clazz) {
		return javaType.double_ == clazz || javaType.doubleWrap == clazz;
	}
	
	@Override
	public boolean isType(CtClass clazz) {
		return javassistType.double_ == clazz || javassistType.doubleWrap == clazz;
	}

	@Override
	public String sqlType(CtField field) {
		return String.format("`%s` DOUBLE(14, 4) NOT NULL DEFAULT '0.0'", field.getName());
	}
}
