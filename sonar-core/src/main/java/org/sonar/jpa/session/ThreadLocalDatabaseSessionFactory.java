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
package org.sonar.jpa.session;

import org.sonar.api.database.DatabaseSession;

public class ThreadLocalDatabaseSessionFactory implements DatabaseSessionFactory {

  private final ThreadLocal<JpaDatabaseSession> threadSession = new ThreadLocal<JpaDatabaseSession>();
  private final DatabaseConnector connector;

  public ThreadLocalDatabaseSessionFactory(DatabaseConnector connector) {
    this.connector = connector;
  }

  public DatabaseSession getSession() {
    JpaDatabaseSession session = threadSession.get();
    if (session == null) {
      session = new JpaDatabaseSession(connector);
      session.start();
      threadSession.set(session);
    }
    return session;
  }

  public void clear() {
    JpaDatabaseSession session = threadSession.get();
    if (session != null) {
      session.stop();
    }
    threadSession.set(null);
  }

  public void stop() {
    clear();
  }
}
