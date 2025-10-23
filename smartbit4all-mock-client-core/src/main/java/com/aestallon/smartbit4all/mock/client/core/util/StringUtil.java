package com.aestallon.smartbit4all.mock.client.core.util;

import com.google.common.base.Strings;

public final class StringUtil {

  private static final int INDENT_DEFAULT = 2;

  private StringUtil() {}

  public static String toIndentedString(String s) {
    return toIndentedString(s, INDENT_DEFAULT);
  }
  
  public static String toIndentedString(String s, int indent) {
    if (Strings.isNullOrEmpty(s)) {
      return s;
    }

    return s.replaceAll("\\n", "\n" + Strings.repeat(" ", indent));
  }
}
