/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE file at the root of the source
 * tree and available online at
 *
 * https://github.com/keeps/roda
 */
package org.roda.core.common.notifications;

import org.roda.core.data.exceptions.RODAException;
import org.roda.core.data.v2.notifications.Notification;

public interface NotificationProcessor {
  public Notification processNotification(Notification notification) throws RODAException;
}