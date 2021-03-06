/*
 * SonarQube
 * Copyright (C) 2009-2021 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.db.audit.model;

import static com.google.common.base.Strings.isNullOrEmpty;

public interface NewValue {

  default void addField(StringBuilder sb, String field, String value, boolean isString) {
    if (!isNullOrEmpty(value)) {
      sb.append(field);
      addQuote(sb, isString);
      sb.append(value);
      addQuote(sb, isString);
      sb.append(", ");
    }
  }

  default void endString(StringBuilder sb) {
    int length = sb.length();
    if(sb.length() > 1) {
      sb.delete(length - 2, length - 1);
    }
    sb.append("}");
  }

  private static void addQuote(StringBuilder sb, boolean isString) {
    if(isString) {
      sb.append("\"");
    }
  }
}
