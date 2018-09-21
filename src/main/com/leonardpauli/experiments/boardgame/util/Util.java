package com.leonardpauli.experiments.boardgame.util;

import java.lang.reflect.Field;

public class Util {
  public static String objectToString(Object o) {
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
        try {
          sb.append(f.getName() + ": " + f.get(o));
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
        i++;
      }
      sb.append("}");
    }

    return sb.toString();
  }
}
