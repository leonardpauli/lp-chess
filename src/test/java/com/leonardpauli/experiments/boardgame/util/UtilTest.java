package com.leonardpauli.experiments.boardgame.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilTest {

  @Test
  public void testObjectToString() {
    Person obj = new Person("Anna", 34);
    assertEquals("Person{name: Anna}", obj.toString());
  }
}

class Person {
  public String name;
  int age;

  Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }
}
