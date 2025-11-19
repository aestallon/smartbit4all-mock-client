package com.aestallon.smartbit4all.mock.client.core.state.component.interactable.form;

import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Form;

public final class InputField extends AbstractFormElement<String> implements FormElement {

  public InputField(Form form, String[] dataPath, boolean immediateAction) {
    super(form, dataPath, immediateAction);
  }
  
  public void setText(String text) {
    set(text);
  }
  
  public void getText() {
    
  } 

}
