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
package org.sonar.api.platform;

import org.sonar.api.BatchComponent;
import org.sonar.api.ServerComponent;

/**
 * @since 2.2
 */
public enum Environment implements BatchComponent, ServerComponent {

  /*
   * When will GRADLE, ANT, ECLIPSE, INTELLIJ_IDEA be added to this list ? :-)
   */
  SERVER, MAVEN3, MAVEN2, ANT;

  public boolean isServer() {
    return this==SERVER;
  }

  public boolean isMaven2Batch() {
    return this==MAVEN2;
  }

  public boolean isMaven3Batch() {
    return this==MAVEN3;
  }

  public boolean isBatch() {
    return isMaven2Batch() || isMaven3Batch();
  }
}
