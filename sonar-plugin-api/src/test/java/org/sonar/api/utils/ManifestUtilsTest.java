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
package org.sonar.api.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLClassLoader;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class ManifestUtilsTest {

  @Rule
  public TemporaryFolder tempDir = new TemporaryFolder();

    @Test
  public void emptyManifest() throws Exception {
    Manifest mf = new Manifest();
    File jar = createJar(mf, "emptyManifest.jar");

    URLClassLoader classloader = new URLClassLoader(FileUtils.toURLs(new File[]{jar}));
    assertThat(ManifestUtils.getPropertyValues(classloader, "foo").size(), is(0));
  }

  @Test
  public void singleManifest() throws Exception {
    Manifest mf = new Manifest();
    mf.getMainAttributes().putValue("foo", "bar");
    mf.getMainAttributes().putValue("other", "value");
    File jar = createJar(mf, "singleManifest.jar");

    URLClassLoader classloader = new URLClassLoader(FileUtils.toURLs(new File[]{jar}));
    List<String> values = ManifestUtils.getPropertyValues(classloader, "foo");
    assertThat(values.size(), is(1));
    assertThat(values, hasItem("bar"));
  }

  @Test
  public void manyManifests() throws Exception {
    Manifest mf1 = new Manifest();
    mf1.getMainAttributes().putValue("foo", "bar");
    File jar1 = createJar(mf1, "manyManifests-one.jar");

    Manifest mf2 = new Manifest();
    mf2.getMainAttributes().putValue("foo", "otherbar");
    File jar2 = createJar(mf2, "manyManifests-two.jar");

    URLClassLoader classloader = new URLClassLoader(FileUtils.toURLs(new File[]{jar1, jar2}));
    List<String> values = ManifestUtils.getPropertyValues(classloader, "foo");
    assertThat(values.size(), is(2));
    assertThat(values, hasItems("bar", "otherbar"));
  }

  private File createJar(Manifest mf, String name) throws Exception {
    mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    File file = tempDir.newFile(name);
    OutputStream out = new JarOutputStream(new FileOutputStream(file), mf);
    out.flush();
    IOUtils.closeQuietly(out);
    return file;
  }
}
