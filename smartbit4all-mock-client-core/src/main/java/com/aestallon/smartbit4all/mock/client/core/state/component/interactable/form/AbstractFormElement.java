package com.aestallon.smartbit4all.mock.client.core.state.component.interactable.form;

import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Form;

public abstract class AbstractFormElement<T> implements FormElement {

  protected final Form form;
  protected final String[] dataPath;

  protected AbstractFormElement(Form form, String[] dataPath) {
    this.form = form;
    this.dataPath = dataPath;
  }
  
  public void set(T t) {
    form.view().setData(dataPath, t);
  }

}
