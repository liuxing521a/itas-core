package org.itas.core.code.type;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import junit.framework.Assert;

import org.itas.core.code.Modify;
import org.junit.Before;
import org.junit.Test;

public class TestBoolCode {

	private BoolCode codeType;
	
	@Before
	public void setUP() {
		codeType = new BoolCode(new Modify() {
			@Override
			protected String toModify() {
				return null;
			}
		});
	}
	
	@Test
	public void testSetStatement() throws Exception {
		ClassPool pool = ClassPool.getDefault();
		CtClass clazz = pool.get("org.itas.core.code.type.TestBoolCode$Model");
		CtField field = clazz.getDeclaredField("bs");
		
		String content = codeType.setStatement(field);
		Assert.assertEquals("\t\tstate.setBoolean(1, getBs());", content);
		
		content = codeType.getResultSet(field);
		Assert.assertEquals("\t\tsetBs(result.getBoolean(\"bs\"));", content);
	}
	
	class Model {
		
		private boolean bs;

		public boolean getBs() {
			return bs;
		}

		public void setBs(boolean bs) {
			this.bs = bs;
		}
		
	}
	
}