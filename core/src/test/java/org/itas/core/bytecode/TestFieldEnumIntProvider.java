package org.itas.core.bytecode;

import java.sql.ResultSet;
import java.sql.SQLException;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import junit.framework.Assert;

import org.itas.core.EnumInt;
import org.junit.Before;
import org.junit.Test;

import com.mysql.jdbc.PreparedStatement;

public class TestFieldEnumIntProvider {

	private FieldProvider codeType;
	
	@Before
	public void setUP() {
		codeType = new EnumIntProvider();
		codeType.setMethodProvider(new TestMethod());
	}
	
	@Test
	public void testEnum() throws Exception {
		ClassPool pool = ClassPool.getDefault();
		CtClass clazz = pool.get(Model.class.getName());
		CtField field = clazz.getDeclaredField("bs");
		
		String result = 
				"\t\t"
				+ "setBs(org.itas.core.util.Utils.EnumUtils.parse(org.itas.core.EnumInt.class, result.getInt(\"bs\")));";
		
		String content = codeType.getResultSet(field);
		Assert.assertEquals(result, content);
		
		result = 
			"\t\t"
			+ "int eint_bs = 0;"
			+ "\n\t\t"
			+ "if (getBs() != null) {"
			+ "\n\t\t\t"
			+ "eint_bs = getBs().key();"
			+ "\n\t\t"
			+ "}"
			+ "\n\t\t"
			+ "state.setInt(1, eint_bs);";
		
		content = codeType.setStatement(field);
		Assert.assertEquals(result, content);
	}
	
}
