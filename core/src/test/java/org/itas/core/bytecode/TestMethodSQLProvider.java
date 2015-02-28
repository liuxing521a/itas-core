package org.itas.core.bytecode;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import junit.framework.Assert;
import net.itas.core.annotation.SQLEntity;

import org.itas.core.GameObject;
import org.itas.core.Index;
import org.itas.core.Unique;
import org.itas.core.bytecode.MethodSQLProvider.SQLAlterProvider;
import org.itas.core.bytecode.MethodSQLProvider.SQLCreateProvider;
import org.itas.core.bytecode.MethodSQLProvider.SQLDeleteProvider;
import org.itas.core.bytecode.MethodSQLProvider.SQLInsertProvider;
import org.itas.core.bytecode.MethodSQLProvider.SQLSelectProvider;
import org.itas.core.bytecode.MethodSQLProvider.SQLUpdateProvider;
import org.junit.Before;
import org.junit.Test;

public class TestMethodSQLProvider {

	
	private CtClass ctClass;
	
	@Before
	public void setUP() throws NotFoundException, Exception {
		ctClass = ClassPool.getDefault().get(TestModel.class.getName());
	}
	
	@Test
	public void testSQLCreate() throws Exception {
		SQLCreateProvider sqlCreate = new SQLCreateProvider();
		
		CtField ctField = ctClass.getDeclaredField("name");
		sqlCreate.begin(ctClass);
		sqlCreate.append(ctField);
		sqlCreate.end();
		
		String expected = 
				"CREATE TABLE IF NOT EXISTS `model`("
				+ "\n\t"
				+ "`name` VARCHAR(36) NOT NULL DEFAULT ''"
				+ "\n"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		Assert.assertEquals(expected, sqlCreate.toString());
		
		sqlCreate = new SQLCreateProvider();
		sqlCreate.begin(ctClass);
		sqlCreate.append(ctClass.getDeclaredField("identy"));
		sqlCreate.end();
		expected = 
				"CREATE TABLE IF NOT EXISTS `model`("
				+ "\n\t"
				+ "`identy` VARCHAR(36) NOT NULL DEFAULT '',"
				+ "\n\t"
				+ "UNIQUE KEY `identy` (`identy`)"
				+ "\n"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		Assert.assertEquals(expected, sqlCreate.toString());
		
		sqlCreate = new SQLCreateProvider();
		ctField = ctClass.getDeclaredField("address");
		sqlCreate.begin(ctClass);
		sqlCreate.append(ctField);
		sqlCreate.end();
		expected = 
				"CREATE TABLE IF NOT EXISTS `model`("
				+ "\n\t"
				+ "`address` VARCHAR(36) NOT NULL DEFAULT '',"
				+ "\n\t"
				+ "KEY `address` (`address`)"
				+ "\n"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		Assert.assertEquals(expected, sqlCreate.toString());
		
		CtMethod method = sqlCreate.toMethod();
		ctClass.addMethod(method);
	}
	
	@Test
	public void testSqlAlter() throws Exception {
		SQLAlterProvider sqlAlter = new SQLAlterProvider();
		sqlAlter.setExitsColumns(Collections.emptySet());
		
		CtField ctField = ctClass.getDeclaredField("name");
		
		sqlAlter.begin(ctClass);
		sqlAlter.append(ctField);
		sqlAlter.end();
		
		String expected = "ALTER TABLE `model` ADD `name` VARCHAR(36) NOT NULL DEFAULT '';\n";
		String actual = sqlAlter.toString();
		Assert.assertEquals(expected, actual);
		
		CtMethod method = sqlAlter.toMethod();
		ctClass.addMethod(method);
	}
	
	@Test
	public void testSQLSelect() throws NotFoundException, ClassNotFoundException, CannotCompileException, IOException {
		SQLSelectProvider sqlSelect = new SQLSelectProvider();
		
		CtField ctField = ctClass.getDeclaredField("name");
		
		sqlSelect.begin(ctClass);
		sqlSelect.append(ctField);
		sqlSelect.end();
		
		String expected = "SELECT `name` FROM `model` WHERE Id = ?;";
		String actual = sqlSelect.toString();
		Assert.assertEquals(expected, actual);
		
		CtMethod method = sqlSelect.toMethod();
		ctClass.addMethod(method);
	}
	
	@Test
	public void testSQLInsert() throws Exception {
		SQLInsertProvider insert = new SQLInsertProvider();
		
		
		insert.begin(ctClass);
		insert.append(ctClass.getDeclaredField("name"));
		insert.end();
		
		String expected = "INSERT INTO `model` (`name`) VALUES (?);";
		String actual = insert.toString();
		Assert.assertEquals(expected, actual);
		
		CtMethod method = insert.toMethod();
		ctClass.addMethod(method);
	}
	
	@Test
	public void testSQLUpdate() throws Exception {
		SQLUpdateProvider update = new SQLUpdateProvider();
		
		
		update.begin(ctClass);
		update.append(ctClass.getDeclaredField("name"));
		update.end();
		
		String expected = "UPDATE `model` SET `name` = ? WHERE Id = ?;";
		String actual = update.toString();
		Assert.assertEquals(expected, actual);
		
		CtMethod method = update.toMethod();
		ctClass.addMethod(method);
	}
	
	@Test
	public void testSQLDelete() throws Exception {
		SQLDeleteProvider delete = new SQLDeleteProvider();
		
		
		delete.begin(ctClass);
		delete.end();
		
		String expected = "DELETE FROM `model` WHERE Id = ?;";
		String actual = delete.toString();
		Assert.assertEquals(expected, actual);
		
		CtMethod method = delete.toMethod();
		ctClass.addMethod(method);
	}
	
	@SQLEntity("model")
	class TestModel extends GameObject {

		protected TestModel(String Id) {
			super(Id);
		}
		
		private String name;
		
		@Unique
		private String identy;
		
		@Index
		private String address;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIdenty() {
			return identy;
		}

		public void setIdenty(String identy) {
			this.identy = identy;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
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