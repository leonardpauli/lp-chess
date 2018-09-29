package com.leonardpauli.experiments.boardgame.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Util {
  public static String objectToString(Object o) {
    StringBuilder sb = new StringBuilder();
    sb.append(o.getClass().getSimpleName());

    // TODO: input list sb.append("()");
    // TODO: recursive prevention + depth limit

    Field[] fields = o.getClass().getFields();
    if (fields.length > 0) {
      sb.append("{");
      int i = 0;
      boolean shouldNotAddComma = true;
      for (Field f : fields) {
        if (!shouldNotAddComma) sb.append(", ");
        shouldNotAddComma = false;
        try {
          if (f.getType().equals(boolean.class)) {
            if (f.getBoolean(o)) sb.append(f.getName());
            else shouldNotAddComma = true;
          } else {
            sb.append(f.getName() + ": ");
            Object val = f.get(o);

            if (val != null && val.getClass().isArray()) {
              appendIterable(sb, Arrays.asList((Object[]) val));
            } else if (val instanceof ArrayList) {
              appendIterable(sb, (ArrayList) val);
            } else if (val instanceof Optional) {
              if (!((Optional) val).isPresent()) sb.append("Empty");
              else sb.append(((Optional) val).get());
            } else {
              sb.append(val);
            }
          }

        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }

        i++;
      }
      sb.append("}");
    }

    return sb.toString();
  }

  private static void appendIterable(StringBuilder sb, Iterable xs) {
    sb.append("[");
    boolean isFirstInner = true;
    for (Object x : xs) {
      if (!isFirstInner) sb.append(", ");
      sb.append(x);
      isFirstInner = false;
    }
    sb.append("]");
  }
}
