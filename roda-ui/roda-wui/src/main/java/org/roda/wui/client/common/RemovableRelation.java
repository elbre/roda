/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE file at the root of the source
 * tree and available online at
 *
 * https://github.com/keeps/roda
 */
package org.roda.wui.client.common;

import java.util.ArrayList;
import java.util.List;

import org.roda.core.data.v2.ri.RelationObjectType;
import org.roda.core.data.v2.ri.RepresentationInformationRelation;
import org.roda.wui.client.planning.ShowRepresentationInformation;
import org.roda.wui.common.client.tools.HistoryUtils;
import org.roda.wui.common.client.tools.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class RemovableRelation extends Composite implements HasHandlers {
  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  interface MyUiBinder extends UiBinder<Widget, RemovableRelation> {
  }

  @UiField
  FlowPanel link;

  @UiField(provided = true)
  Anchor removeDynamicTextBoxButton;

  RepresentationInformationRelation relation = null;

  public RemovableRelation() {
    this(null);
  }

  public RemovableRelation(final RepresentationInformationRelation riRelation) {
    this.relation = riRelation;
    removeDynamicTextBoxButton = new Anchor(SafeHtmlUtils.fromSafeConstant("<i class=\"fa fa-remove\"></i>"));
    initWidget(uiBinder.createAndBindUi(this));

    InlineHTML bullet = new InlineHTML("&#8226;");
    bullet.addStyleName("bullet");
    link.add(bullet);

    String title = riRelation.getTitle();
    if (StringUtils.isBlank(title)) {
      title = riRelation.getLink();
    }

    if (riRelation.getObjectType().equals(RelationObjectType.AIP)) {
      Anchor a = new Anchor(title,
        HistoryUtils.createHistoryHashLink(HistoryUtils.getHistoryBrowse(riRelation.getLink())), "_blank");
      link.add(a);
    } else if (riRelation.getObjectType().equals(RelationObjectType.REPRESENTATION_INFORMATION)) {
      List<String> history = new ArrayList<>();
      history.addAll(ShowRepresentationInformation.RESOLVER.getHistoryPath());
      history.add(riRelation.getLink());

      Anchor a = new Anchor(title, HistoryUtils.createHistoryHashLink(history), "_blank");
      link.add(a);
    } else if (riRelation.getObjectType().equals(RelationObjectType.WEB)) {
      link.add(new Anchor(title, riRelation.getLink(), "_blank"));
    } else if (riRelation.getObjectType().equals(RelationObjectType.TEXT)) {
      link.add(new InlineLabel(riRelation.getLink()));
    }
  }

  public RepresentationInformationRelation getValue() {
    return relation;
  }

  public void addRemoveClickHandler(ClickHandler clickHandler) {
    removeDynamicTextBoxButton.addClickHandler(clickHandler);
  }

  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }
}
