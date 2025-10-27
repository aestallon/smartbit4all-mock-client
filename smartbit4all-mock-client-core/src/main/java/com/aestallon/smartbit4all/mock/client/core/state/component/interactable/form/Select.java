package com.aestallon.smartbit4all.mock.client.core.state.component.interactable.form;

import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Form;

/**
 * Represents a control that enables picking a single value from a list of options.
 * 
 * <p>
 * 
 */
public final class Select extends AbstractFormElement<String> {
  
  public Select(Form form, String[] dataPath) {
    super(form, dataPath);
  }
}
