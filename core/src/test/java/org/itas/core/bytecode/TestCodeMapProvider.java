package org.itas.core.bytecode;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedHashSet;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import junit.framework.Assert;
import net.itas.core.annotation.Clazz;
import net.itas.core.annotation.Size;

import org.itas.core.EnumByte;
import org.itas.core.GameObject;
import org.itas.core.Simple;
import org.itas.core.resource.Resource;
import org.junit.Before;
import org.junit.Test;

public class TestCodeMapProvider {

	private CodeMapProvider codeType;
	
	private CtClass clazz;
	
	@Before
	public void setUP() throws Exception {
		codeType = new CodeMapProvider(new Modify() {
			@Override
			protected String toModify() {
				return null;
			}
		});
		
		ClassPool pool = ClassPool.getDefault();
		clazz = pool.get(Model.class.getName());
	}
	
	@Test
	public void testSimple() throws Exception {
		CtField field = clazz.getDeclaredField("bs");
		
		String expected = 
				"\t\t"
						+ "state.setString(1, org.itas.core.util.GameObjects.toString(getBs()));";
		String content = codeType.setStatement(field);
		Assert.assertEquals(expected, content);
		
		expected = 
				"\t\t"
				+ "{"
				+ "\n\t\t\t"
				+ "String data = result.getString(\"bs\");"
				+ "\n\t\t\t"
				+ "java.util.List dataStrList = org.itas.core.util.GameObjects.parseList(data);"
				+ "\n\t\t\t"
				+ "java.util.Set dataArray = new java.util.LinkedHashSet(8);"
				+ "\n\t\t\t"
				+ "for (Object value : dataStrList) {"
				+ "\n\t\t\t\t"
				+ "dataArray.add(new org.itas.core.Simple((String)value));"
				+ "\n\t\t\t"
				+ "}"
				+ "\n\t\t\t"
				+ "setBs(dataArray);"
				+ "\n\t\t"
				+ "}";
		//						 bsArray.add(%s);
		content = codeType.getResultSet(field);
		Assert.assertEquals(expected, content);
	}
	
	@Test
	public void testResource() throws Exception {
		CtField field = clazz.getDeclaredField("rs");
		
		String expected = 
				"\t\t"
						+ "state.setString(1, org.itas.core.util.GameObjects.toString(getRs()));";
		String content = codeType.setStatement(field);
		Assert.assertEquals(expected, content);
		
		expected = 
				"\t\t"
				+ "{"
				+ "\n\t\t\t"
				+ "String data = result.getString(\"rs\");"
				+ "\n\t\t\t"
				+ "java.util.List dataStrList = org.itas.core.util.GameObjects.parseList(data);"
				+ "\n\t\t\t"
				+ "java.util.Set dataArray = new java.util.HashSet(16);"
				+ "\n\t\t\t"
				+ "for (Object value : dataStrList) {"
				+ "\n\t\t\t\t"
				+ "dataArray.add(org.itas.core.Pool.getResource((String)value));"
				+ "\n\t\t\t"
				+ "}"
				+ "\n\t\t\t"
				+ "setRs(dataArray);"
				+ "\n\t\t"
				+ "}";
		//						 bsArray.add(%s);
		content = codeType.getResultSet(field);
		Assert.assertEquals(expected, content);
	}
	
	@Test
	public void testJavaBase() throws Exception {
		CtField field = clazz.getDeclaredField("as");
		
		String expected = 
				"\t\t"
						+ "state.setString(1, org.itas.core.util.GameObjects.toString(getAs()));";
		String content = codeType.setStatement(field);
		Assert.assertEquals(expected, content);
		
		expected = 
				"\t\t"
				+ "{"
				+ "\n\t\t\t"
				+ "String data = result.getString(\"as\");"
				+ "\n\t\t\t"
				+ "java.util.List dataStrList = org.itas.core.util.GameObjects.parseList(data);"
				+ "\n\t\t\t"
				+ "java.util.Set dataArray = new java.util.HashSet(8);"
				+ "\n\t\t\t"
				+ "for (Object value : dataStrList) {"
				+ "\n\t\t\t\t"
				+ "dataArray.add(java.lang.Integer.valueOf((String)value));"
				+ "\n\t\t\t"
				+ "}"
				+ "\n\t\t\t"
				+ "setAs(dataArray);"
				+ "\n\t\t"
				+ "}";
		//						 bsArray.add(%s);
		content = codeType.getResultSet(field);
		Assert.assertEquals(expected, content);
	}
	
	@Test
	public void testEnum() throws Exception {
		CtField field = clazz.getDeclaredField("es");
		
		String expected = 
						"\t\t"
						+ "state.setString(1, org.itas.core.util.GameObjects.toString(getEs()));";
		String content = codeType.setStatement(field);
		Assert.assertEquals(expected, content);
		
		expected = 
				"\t\t"
				+ "{"
				+ "\n\t\t\t"
				+ "String data = result.getString(\"es\");"
				+ "\n\t\t\t"
				+ "java.util.List dataStrList = org.itas.core.util.GameObjects.parseList(data);"
				+ "\n\t\t\t"
				+ "java.util.Set dataArray = new java.util.HashSet(8);"
				+ "\n\t\t\t"
				+ "for (Object value : dataStrList) {"
				+ "\n\t\t\t\t"
				+ "dataArray.add(org.itas.core.util.Utils.EnumUtils.parse(org.itas.core.EnumByte.class, java.lang.Byte.valueOf((String)value)));"
				+ "\n\t\t\t"
				+ "}"
				+ "\n\t\t\t"
				+ "setEs(dataArray);"
				+ "\n\t\t"
				+ "}";
				//						 bsArray.add(%s);
		content = codeType.getResultSet(field);
		Assert.assertEquals(expected, content);
	}
	
	
	
	class Model {
		
		@Clazz(LinkedHashSet.class)
		private Set<Simple<TestMode>> bs;

		@Size(16)
		private Set<TestRes> rs;

		private Set<Integer> as;
		
		private Set<EnumByte> es;

		public Set<Simple<TestMode>> getBs() {
			return bs;
		}

		public void setBs(Set<Simple<TestMode>> bs) {
			this.bs = bs;
		}

		public Set<TestRes> getRs() {
			return rs;
		}

		public void setRs(Set<TestRes> rs) {
			this.rs = rs;
		}

		public Set<Integer> getAs() {
			return as;
		}

		public void setAs(Set<Integer> as) {
			this.as = as;
		}

		public Set<EnumByte> getEs() {
			return es;
		}

		public void setEs(Set<EnumByte> es) {
			this.es = es;
		}
		
	}
	
	public class TestRes extends Resource {

		protected TestRes(String Id) {
			super(Id);
		}
		
	}
	
	class TestMode extends GameObject {

		protected TestMode(String Id) {
			super(Id);
		}

		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			
		}

		@Override
		public void readExternal(ObjectInput in) throws IOException,
				ClassNotFoundException {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected String PRIFEX() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected <T extends GameObject> T autoInstance(String Id) {
			// TODO Auto-generated method stub
			return null;
		}

	}
	
}