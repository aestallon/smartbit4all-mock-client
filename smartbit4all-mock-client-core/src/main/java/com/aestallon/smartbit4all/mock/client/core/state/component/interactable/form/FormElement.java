package com.aestallon.smartbit4all.mock.client.core.state.component.interactable.form;

import org.smartbit4all.api.formdefinition.bean.SmartWidgetDefinition;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Form;

public interface FormElement {

  final class Factory {

    private final Form form;

    public Factory(Form form) { this.form = form; }

    FormElement create(SmartWidgetDefinition elementDef) {
      return null;
    }

  }

}
