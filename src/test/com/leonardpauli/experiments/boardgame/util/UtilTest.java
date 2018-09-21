package com.leonardpauli.experiments.boardgame.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilTest {

  @Test
  void objectToString() {

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
    try {
      return Util.objectToString(this);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
