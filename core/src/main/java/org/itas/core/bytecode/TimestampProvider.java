package org.itas.core.bytecode;

import javassist.CtClass;
import javassist.CtField;

/**
 * 日期数据[field]类型字节码动态生成
 * @author liuzhen(liuxing521a@gmail.com)
 * @crateTime 2015年2月27日下午3:38:58
 */
class TimestampProvider extends AbstractFieldProvider 
		implements FieldProvider, TypeProvider {

	private static final String STATEMENT_SET = 
			"\t\t" +
			"state.setTimestamp(%s, get%s());";
	
	private static final String RESULTSET_GET = 
			"\t\t" +
			"set%s(result.getTimestamp(\"%s\"));";
	
	public static final TimestampProvider PROVIDER = new TimestampProvider();
	
	private TimestampProvider() {
	}
	
	@Override
	public boolean isType(Class<?> clazz) {
		return javaType.timeStamp == clazz;
	}
	
	@Override
	public boolean isType(CtClass clazz)  throws Exception {
		return clazz.subtypeOf(javassistType.timeStamp);
	}

	@Override
	public String sqlType(CtField field) throws Exception {
		return String.format("`%s` TIMESTAMP NOT NULL", field.getName());
	}

	@Override
	public String setStatement(int index, CtField field) {
		return String.format(STATEMENT_SET, index, upCase(field.getName()));
	}

	@Override
	public String getResultSet(CtField field) {
		return String.format(RESULTSET_GET, upCase(field.getName()), field.getName());
	}

}
