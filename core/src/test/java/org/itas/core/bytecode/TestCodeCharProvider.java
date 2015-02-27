package org.itas.core.bytecode;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class TestCodeCharProvider {

	private CodeCharProvider codeType;
	
	@Before
	public void setUP() {
		codeType = new CodeCharProvider(new Modify() {
			@Override
			protected String toModify() {
				return null;
			}
		});
	}
	
	@Test
	public void testSetStatement() throws Exception {
		ClassPool pool = ClassPool.getDefault();
		CtClass clazz = pool.get(Model.class.getName());
		CtField field = clazz.getDeclaredField("bs");
		
		String content = codeType.setStatement(field);
		Assert.assertEquals("\t\tstate.setString(1, String.valueOf(getBs()));", content);
		
		content = codeType.getResultSet(field);
		Assert.assertEquals("\t\tsetBs(result.getString(\"bs\").charAt(0));", content);
	}
	
	class Model {
		
		private char bs;

		public char getBs() {
			return bs;
		}

		public void setBs(char bs) {
			this.bs = bs;
		}
		
	}
	
}