package org.itas.core.bytecode;

import javassist.CtClass;
import javassist.CtField;

/**
 * 枚举关键字为int数据[field]类型字节码动态生成
 * @author liuzhen(liuxing521a@gmail.com)
 * @crateTime 2015年2月26日下午4:51:14
 */
class EnumIntProvider extends AbstractFieldProvider 
    implements FieldProvider, TypeProvider {
	
	private static final String STATEMENT_SET = 
				"\t\t" 
			+ "int eint_%s = 0;"
			+ "\n\t\t"
			+ "if (get%s() != null) {"
			+ "\n\t\t\t"
			+ "eint_%s = get%s().key();"
			+ "\n\t\t"
			+ "}"
			+ "\n\t\t"
			+ "state.setInt(%s, eint_%s);";

	private static final String RESULTSET_GET = 
			"\t\t" +
			"set%s(org.itas.core.util.Utils.EnumUtils.parse(%s.class, result.getInt(\"%s\")));";
	

	public static final EnumIntProvider PROVIDER = new EnumIntProvider();
	
	private EnumIntProvider() {
	}
	
	@Override
	public String setStatement(int index, CtField field) {
		return String.format(STATEMENT_SET, field.getName(), upCase(field.getName()), 
				field.getName(), upCase(field.getName()), index, field.getName());
	}

	@Override
	public String getResultSet(CtField field) throws Exception {
		return String.format(RESULTSET_GET, upCase(field.getName()), 
				field.getType().getName(), field.getName());
	}
	
	@Override
	public boolean isType(Class<?> clazz) {
		return javaType.enumInt_.isAssignableFrom(clazz);
	}
	
	@Override
	public boolean isType(CtClass clazz)  throws Exception {
		return clazz.subtypeOf(javassistType.enumInt_);
	}

	@Override
	public String sqlType(CtField field) {
		return String.format("`%s` INT(11) NOT NULL DEFAULT '0'", field.getName());
	}

}
