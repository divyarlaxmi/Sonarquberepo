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
package org.sonar.db.webhook;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sonar.api.utils.System2;
import org.sonar.db.DbClient;
import org.sonar.db.DbSession;
import org.sonar.db.DbTester;
import org.sonar.db.audit.AuditPersister;
import org.sonar.db.audit.model.WebhookNewValue;
import org.sonar.db.component.ComponentDbTester;
import org.sonar.db.project.ProjectDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WebhookDaoWithPersisterTest {
  private AuditPersister auditPersister = mock(AuditPersister.class);

  @Rule
  public final DbTester dbTester = DbTester.create(System2.INSTANCE, auditPersister);

  private final DbClient dbClient = dbTester.getDbClient();
  private final DbSession dbSession = dbTester.getSession();
  private final WebhookDao underTest = dbClient.webhookDao();
  private final WebhookDbTester webhookDbTester = dbTester.webhooks();
  private final ComponentDbTester componentDbTester = dbTester.components();

  private final ArgumentCaptor<WebhookNewValue> newValueCaptor = ArgumentCaptor.forClass(WebhookNewValue.class);

  @Test
  public void insertGlobalWebhookIsPersisted() {
    WebhookDto dto = new WebhookDto()
      .setUuid("UUID_1")
      .setName("NAME_1")
      .setUrl("URL_1")
      .setSecret("a_secret");

    underTest.insert(dbSession, dto, null);

    verify(auditPersister).addWebhook(eq(dbSession), newValueCaptor.capture());
    WebhookNewValue newValue = newValueCaptor.getValue();
    assertThat(newValue)
      .extracting(WebhookNewValue::getWebhookUuid, WebhookNewValue::getName)
      .containsExactly(dto.getUuid(), dto.getName());
    assertThat(newValue.toString()).doesNotContain("url");
  }

  @Test
  public void insertProjectWebhookIsPersisted() {
    WebhookDto dto = new WebhookDto()
      .setUuid("UUID_1")
      .setName("NAME_1")
      .setUrl("URL_1")
      .setProjectUuid("UUID_2")
      .setSecret("a_secret");

    underTest.insert(dbSession, dto, "project_name");

    verify(auditPersister).addWebhook(eq(dbSession), newValueCaptor.capture());
    WebhookNewValue newValue = newValueCaptor.getValue();
    assertThat(newValue)
      .extracting(WebhookNewValue::getWebhookUuid, WebhookNewValue::getName, WebhookNewValue::getProjectUuid,
        WebhookNewValue::getProjectName)
      .containsExactly(dto.getUuid(), dto.getName(), dto.getProjectUuid(), "project_name");
    assertThat(newValue.toString()).doesNotContain("url");
  }

  @Test
  public void updateWebhookIsPersisted() {
    WebhookDto dto = webhookDbTester.insertGlobalWebhook();
    dto = dto
      .setName("a-fancy-webhook")
      .setUrl("http://www.fancy-webhook.io")
      .setSecret(null);

    underTest.update(dbSession, dto, null);

    verify(auditPersister).updateWebhook(eq(dbSession), newValueCaptor.capture());
    WebhookNewValue newValue = newValueCaptor.getValue();
    assertThat(newValue)
      .extracting(WebhookNewValue::getWebhookUuid, WebhookNewValue::getName, WebhookNewValue::getUrl)
      .containsExactly(dto.getUuid(), dto.getName(), dto.getUrl());
    assertThat(newValue.toString()).doesNotContain("projectUuid");
  }

  @Test
  public void deleteProjectWebhooksIsPersisted() {
    ProjectDto projectDto = componentDbTester.insertPrivateProjectDto();
    webhookDbTester.insertWebhook(projectDto);

    underTest.deleteByProject(dbSession, projectDto);

    verify(auditPersister).deleteWebhook(eq(dbSession), newValueCaptor.capture());
    WebhookNewValue newValue = newValueCaptor.getValue();
    assertThat(newValue)
      .extracting(WebhookNewValue::getProjectUuid, WebhookNewValue::getProjectName)
      .containsExactly(projectDto.getUuid(), projectDto.getName());
    assertThat(newValue.toString()).doesNotContain("webhookUuid");
  }

  @Test
  public void deleteWebhookIsPersisted() {
    WebhookDto dto = webhookDbTester.insertGlobalWebhook();

    underTest.delete(dbSession, dto.getUuid(), dto.getName());

    verify(auditPersister).deleteWebhook(eq(dbSession), newValueCaptor.capture());
    WebhookNewValue newValue = newValueCaptor.getValue();
    assertThat(newValue)
      .extracting(WebhookNewValue::getWebhookUuid, WebhookNewValue::getName)
      .containsExactly(dto.getUuid(), dto.getName());
    assertThat(newValue.toString()).doesNotContain("url");
  }
}
