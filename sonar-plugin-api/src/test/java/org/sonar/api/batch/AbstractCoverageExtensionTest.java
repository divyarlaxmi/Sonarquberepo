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
package org.sonar.api.batch;

import org.junit.Test;
import org.sonar.api.resources.Project;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractCoverageExtensionTest {

  @Test
  public void doNotExecuteIfStaticAnalysis() {
    Project project = mock(Project.class);
    when(project.getAnalysisType()).thenReturn(Project.AnalysisType.STATIC);
    FakeCoverageSensor sensor = new FakeCoverageSensor();

    assertThat(sensor.shouldExecuteOnProject(project), is(false));
  }

  protected static class FakeCoverageSensor extends AbstractCoverageExtension {

  }
}
