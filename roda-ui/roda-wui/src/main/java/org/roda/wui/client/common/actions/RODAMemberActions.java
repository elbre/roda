package org.roda.wui.client.common.actions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.roda.core.data.v2.index.select.SelectedItems;
import org.roda.core.data.v2.user.RODAMember;
import org.roda.wui.client.common.actions.callbacks.ActionAsyncCallback;
import org.roda.wui.client.common.actions.model.ActionsBundle;
import org.roda.wui.client.common.actions.model.ActionsGroup;
import org.roda.wui.client.management.CreateGroup;
import org.roda.wui.client.management.CreateUser;
import org.roda.wui.client.management.UserManagementService;
import org.roda.wui.common.client.tools.HistoryUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import config.i18n.client.ClientMessages;

/**
 * @author Bruno Ferreira <bferreira@keep.pt>
 */
public class RODAMemberActions extends AbstractActionable<RODAMember> {
  private static final ClientMessages messages = GWT.create(ClientMessages.class);
  private static final RODAMemberActions GENERAL_INSTANCE = new RODAMemberActions();

  private static final Set<RODAMemberAction> POSSIBLE_ACTIONS_WITHOUT_MEMBER = new HashSet<>(
    Arrays.asList(RODAMemberAction.NEW_USER, RODAMemberAction.NEW_GROUP));

  private static final Set<RODAMemberAction> POSSIBLE_ACTIONS_ON_USER = new HashSet<>(
    Arrays.asList(RODAMemberAction.ACTIVATE, RODAMemberAction.DEACTIVATE, RODAMemberAction.REMOVE));

  private static final Set<RODAMemberAction> POSSIBLE_ACTIONS_ON_GROUP = new HashSet<>(
    Arrays.asList(RODAMemberAction.REMOVE));

  private static final Set<RODAMemberAction> POSSIBLE_ACTIONS_ON_MEMBERS = new HashSet<>(
    Arrays.asList(RODAMemberAction.ACTIVATE, RODAMemberAction.DEACTIVATE, RODAMemberAction.REMOVE));

  private RODAMemberActions() {
  }

  public enum RODAMemberAction implements Actionable.Action<RODAMember> {
    NEW_USER, NEW_GROUP, ACTIVATE, DEACTIVATE, REMOVE
  }

  public static RODAMemberActions get() {
    return GENERAL_INSTANCE;
  }

  @Override
  public boolean canAct(Action<RODAMember> action) {
    return POSSIBLE_ACTIONS_WITHOUT_MEMBER.contains(action);
  }

  @Override
  public boolean canAct(Action<RODAMember> action, RODAMember object) {
    if (object.isUser()) {
      return (action.equals(RODAMemberAction.DEACTIVATE) && object.isActive())
        || (action.equals(RODAMemberAction.ACTIVATE) && !object.isActive()) || (action.equals(RODAMemberAction.REMOVE));
    } else {
      return POSSIBLE_ACTIONS_ON_GROUP.contains(action);
    }
  }

  @Override
  public boolean canAct(Action<RODAMember> action, SelectedItems<RODAMember> objects) {
    return POSSIBLE_ACTIONS_ON_MEMBERS.contains(action);
  }

  @Override
  public void act(Action<RODAMember> action, AsyncCallback<ActionImpact> callback) {
    if (action.equals(RODAMemberAction.NEW_USER)) {
      createUser(callback);
    } else if (action.equals(RODAMemberAction.NEW_GROUP)) {
      createGroup(callback);
    } else {
      unsupportedAction(action, callback);
    }
  }

  @Override
  public void act(Action<RODAMember> action, RODAMember object, AsyncCallback<ActionImpact> callback) {
    if (action.equals(RODAMemberAction.ACTIVATE)) {
      activate(objectToSelectedItems(object, RODAMember.class), callback);
    } else if (action.equals(RODAMemberAction.DEACTIVATE)) {
      deactivate(objectToSelectedItems(object, RODAMember.class), callback);
    } else if (action.equals(RODAMemberAction.REMOVE)) {
      remove(objectToSelectedItems(object, RODAMember.class), callback);
    } else {
      unsupportedAction(action, callback);
    }
  }

  @Override
  public void act(Action<RODAMember> action, SelectedItems<RODAMember> objects, AsyncCallback<ActionImpact> callback) {
    if (action.equals(RODAMemberAction.ACTIVATE)) {
      activate(objects, callback);
    } else if (action.equals(RODAMemberAction.DEACTIVATE)) {
      deactivate(objects, callback);
    } else if (action.equals(RODAMemberAction.REMOVE)) {
      remove(objects, callback);
    } else {
      unsupportedAction(action, callback);
    }
  }

  private void activate(SelectedItems<RODAMember> objects, AsyncCallback<ActionImpact> callback) {
    UserManagementService.Util.getInstance().changeActiveRODAMembers(objects, true,
      new ActionAsyncCallback<Void>(callback) {
        @Override
        public void onSuccess(Void result) {
          doActionCallbackUpdated();
        }
      });
  }

  private void deactivate(SelectedItems<RODAMember> objects, AsyncCallback<ActionImpact> callback) {
    UserManagementService.Util.getInstance().changeActiveRODAMembers(objects, false,
      new ActionAsyncCallback<Void>(callback) {
        @Override
        public void onSuccess(Void result) {
          doActionCallbackUpdated();
        }
      });
  }

  private void remove(SelectedItems<RODAMember> objects, AsyncCallback<ActionImpact> callback) {
    UserManagementService.Util.getInstance().deleteRODAMembers(objects, new ActionAsyncCallback<Void>(callback) {
      @Override
      public void onSuccess(Void result) {
        doActionCallbackDestroyed();
      }
    });
  }

  private void createUser(AsyncCallback<ActionImpact> callback) {
    HistoryUtils.newHistory(CreateUser.RESOLVER.getHistoryPath());
    callback.onSuccess(ActionImpact.UPDATED);
  }

  private void createGroup(AsyncCallback<ActionImpact> callback) {
    HistoryUtils.newHistory(CreateGroup.RESOLVER.getHistoryPath());
    callback.onSuccess(ActionImpact.UPDATED);
  }

  @Override
  public ActionsBundle<RODAMember> createActionsBundle() {
    ActionsBundle<RODAMember> transferredResourcesActionsBundle = new ActionsBundle<>();

    // ACTIONS
    ActionsGroup<RODAMember> actionsGroup = new ActionsGroup<>(messages.sidebarActionsTitle());
    actionsGroup.addButton(messages.addUserButton(), RODAMemberAction.NEW_USER, ActionImpact.UPDATED, "btn-plus");
    actionsGroup.addButton(messages.addGroupButton(), RODAMemberAction.NEW_GROUP, ActionImpact.UPDATED, "btn-plus");

    actionsGroup.addButton(messages.editUserActivate(), RODAMemberAction.ACTIVATE, ActionImpact.UPDATED, "btn-check");
    actionsGroup.addButton(messages.editUserDeactivate(), RODAMemberAction.DEACTIVATE, ActionImpact.UPDATED,
      "btn-check");
    actionsGroup.addButton(messages.editUserRemove(), RODAMemberAction.REMOVE, ActionImpact.DESTROYED, "btn-ban");

    transferredResourcesActionsBundle.addGroup(actionsGroup);

    return transferredResourcesActionsBundle;
  }
}