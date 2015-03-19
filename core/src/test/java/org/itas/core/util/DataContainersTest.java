package org.itas.core.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.itas.util.Pair;
import org.junit.Test;

public class DataContainersTest implements DataContainers {

  @Test
  public void testToStringList() {
      System.out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
	List<String> list = Arrays.asList("刘振", "李文行", "刘宇轩", "刘羽桐");
	
	Assert.assertEquals("[4]刘振|李文行|刘宇轩|刘羽桐|", toString(list));
  }
  
  @Test
  public void testToStringArray() {
    String[] arrays = {"刘振", "李文行", "刘宇轩", "刘羽桐"};
	
	Assert.assertEquals("[4]刘振|李文行|刘宇轩|刘羽桐|", toString(arrays));
  }
  
  @Test
  public void testToStringDoubleArray() {
    String[][] arrays = {{"刘振", "李文行", "刘宇轩", "刘羽桐"},
    		{"刘振", "李文行", "刘宇轩", "刘羽桐"}};
	
	Assert.assertEquals("[2,4]刘振|李文行|刘宇轩|刘羽桐|刘振|李文行|刘宇轩|刘羽桐|", toString(arrays));
  }
  
  @Test
  public void testToStringMap() {
	Map<String, String> map = new LinkedHashMap<>();
	map.put("1", "刘振");
	map.put("2", "李文行");
	map.put("3", "刘宇轩");
	map.put("4", "刘羽桐");
	
	Assert.assertEquals("[4]1,刘振|2,李文行|3,刘宇轩|4,刘羽桐|", toString(map));
  }
  
  @Test
  public void testParseArray() {
	String[] expected = {"刘振", "李文行", "刘宇轩", "刘羽桐"};
		
	String[] actual = parseArray("[4]刘振|李文行|刘宇轩|刘羽桐|");
	for (int i = 0; i < expected.length; i++) {
	  Assert.assertEquals(expected[i], actual[i]);
	}
  }
  
  @Test
  public void testParseDoubleArray() {
	String[][] expected = {
		{"刘振", "李文行", "刘宇轩", "刘羽桐"},
		{"刘振", "李文行", "刘宇轩", "刘羽桐"}
	};
		
	String[][] actual = parseDoubleArray("[2,4]刘振|李文行|刘宇轩|刘羽桐|刘振|李文行|刘宇轩|刘羽桐|");
	for (int i = 0; i < expected.length; i++) {
	  for (int j = 0; j < expected[0].length; j++) {
		Assert.assertEquals(expected[i][j], actual[i][j]);
	  }
	}
  }
  
  @Test @SuppressWarnings("unchecked")
  public void testParsePair() {
	Pair<String, String>[] expected = new Pair[]{
	  new Pair<>("1", "刘振"),
	  new Pair<>("2", "李文行"),
	  new Pair<>("3", "刘宇轩"),
	  new Pair<>("4", "刘羽桐"),
	};
	
	
	Pair<String, String>[] actual = parsePair("[4]1,刘振|2,李文行|3,刘宇轩|4,刘羽桐|");
	for (int i = 0; i < actual.length; i++) {
	  Assert.assertEquals(expected[i].getKey(), actual[i].getKey());
	  Assert.assertEquals(expected[i].getValue(), actual[i].getValue());
	}
	
  }
	
}
