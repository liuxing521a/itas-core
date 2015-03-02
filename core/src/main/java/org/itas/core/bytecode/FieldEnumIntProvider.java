package org.itas.core.bytecode;

import static org.itas.core.util.ByteCodeUtils.firstKeyUpCase;
import javassist.CtField;

/**
 * 枚举关键字为int数据[field]类型字节码动态生成
 * @author liuzhen(liuxing521a@gmail.com)
 * @crateTime 2015年2月26日下午4:51:14
 */
class FieldEnumIntProvider extends AbstractFieldProvider {
	
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
	

	public FieldEnumIntProvider() {
		
	}
	
	@Override
	public String setStatement(CtField field) {
		return String.format(STATEMENT_SET, field.getName(), firstKeyUpCase(field.getName()), 
				field.getName(), firstKeyUpCase(field.getName()), provider.getAndIncIndex(), field.getName());
	}

	@Override
	public String getResultSet(CtField field) throws Exception {
		return String.format(RESULTSET_GET, firstKeyUpCase(field.getName()), 
				field.getType().getName(), field.getName());
	}

}
