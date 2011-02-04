/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2011 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.squid.api;

public class SourceClass extends SourceCode {

  /**
   * This is used only for Java for now, but can be used for other languages. So maybe we should push it down to SourceCode.
   */
  private boolean suppressWarnings = false;

  public SourceClass(String key) {
    super(key);
  }

  public SourceClass(String key, String className) {
    super(key, className);
  }

  public void setSuppressWarnings(boolean suppressWarnings) {
    this.suppressWarnings = suppressWarnings;
  }

  public boolean isSuppressWarnings() {
    return suppressWarnings;
  }
}
