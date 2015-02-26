package org.itas.core.bytecode;

import java.sql.Timestamp;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import junit.framework.Assert;

import org.itas.core.bytecode.Modify;
import org.itas.core.bytecode.TimestampCode;
import org.junit.Before;
import org.junit.Test;

public class TestTimestampCode {

	private TimestampCode codeType;
	
	@Before
	public void setUP() {
		codeType = new TimestampCode(new Modify() {
			@Override
			protected String toModify() {
				return null;
			}
		});
	}
	
	@Test
	public void testSetStatement() throws Exception {
		ClassPool pool = ClassPool.getDefault();
		CtClass clazz = pool.get("org.itas.core.bytecode.TestTimestampCode$Model");
		CtField field = clazz.getDeclaredField("bs");
		
		String content = codeType.setStatement(field);
		Assert.assertEquals("\t\tstate.setTimestamp(1, getBs());", content);
		
		content = codeType.getResultSet(field);
		Assert.assertEquals("\t\tsetBs(result.getTimestamp(\"bs\"));", content);
	}
	
	class Model {
		
		private Timestamp bs;

		public Timestamp getBs() {
			return bs;
		}

		public void setBs(Timestamp bs) {
			this.bs = bs;
		}
		
	}
	
}
