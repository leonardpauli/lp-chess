package com.leonardpauli.experiments.boardgame.util;

import java.lang.reflect.Field;

public class Util {
  public static String objectToString(Object o) throws IllegalAccessException {
    StringBuilder sb = new StringBuilder();
    sb.append(o.getClass().getSimpleName());

    // TODO: input list sb.append("()");

    Field[] fields = o.getClass().getFields();
    if (fields.length > 0) {
      sb.append("{");
      int i = 0;
      for (Field f : fields) {
        boolean isFirst = i == 0;
        if (!isFirst) sb.append(", ");
        sb.append(f.getName() + ": " + f.get(o));
        i++;
      }
      sb.append("}");
    }

    return sb.toString();
  }
}
