package org.itas.core.bytecode;

import javassist.CtField;

/**
 * String数据[field]类型字节码动态生成
 * @author liuzhen(liuxing521a@gmail.com)
 * @crateTime 2015年2月26日下午5:35:21
 */
class FieldStringProvider extends AbstractFieldProvider {

	private static final String STATEMENT_SET = 
			"\t\t" +
			"state.setString(%s, get%s());";
	
	private static final String RESULTSET_GET = 
			"\t\t" +
			"set%s(result.getString(\"%s\"));";
	
	public FieldStringProvider() {
		
	}
	
	@Override
	public String setStatement(CtField field) {
		return String.format(STATEMENT_SET, provider.getAndIncIndex(), upCase(field.getName()));
	}

	@Override
	public String getResultSet(CtField field) {
		return String.format(RESULTSET_GET, upCase(field.getName()), field.getName());
	}

}
