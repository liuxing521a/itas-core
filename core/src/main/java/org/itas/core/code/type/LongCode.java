package org.itas.core.code.type;

import javassist.CtField;

import org.itas.core.code.CodeType;
import org.itas.core.code.Modify;

public class LongCode extends CodeType {

	public LongCode(Modify modify) {
		super(modify);
	}

	@Override
	protected String setStatement(CtField field) {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("\t\t");
		buffer.append("state.setLong(");
		buffer.append(modify.incIndex());
		buffer.append(", get");
		buffer.append(firstKeyUpCase(field.getName()));
		buffer.append("());");
		
		return buffer.toString();
	}

	@Override
	protected String getResultSet(CtField field) {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("\t\t");
		buffer.append("set");
		buffer.append(firstKeyUpCase(field.getName()));
		buffer.append("(result.getLong(\"");
		buffer.append(field.getName());
		buffer.append("\"));");
		
		return buffer.toString();
	}

}
