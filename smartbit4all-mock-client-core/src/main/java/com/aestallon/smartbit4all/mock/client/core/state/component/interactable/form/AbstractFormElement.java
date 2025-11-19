package com.aestallon.smartbit4all.mock.client.core.state.component.interactable.form;

import org.smartbit4all.api.view.bean.UiActionRequest;
import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Form;

public abstract class AbstractFormElement<T> implements FormElement {

  protected final Form form;
  protected final String[] dataPath;
  protected final boolean immediateAction;

  protected AbstractFormElement(Form form, String[] dataPath, boolean immediateAction) {
    this.form = form;
    this.dataPath = dataPath;
    this.immediateAction = immediateAction;
  }

  protected void set(T t) {
    form.view().setData(dataPath, t);
    if (immediateAction) {
      performImmediateAction(t);
    }
  }
  
  protected void performImmediateAction(T t) {
    final var view = form.view();
    final String activity = "Setting value of form element " + view +
                            " --> " + form +
                            " --> " + this +
                            " to " + t;
    form.client().performAction(
        new InteractionContext(activity),
        view.id(),
        new UiActionRequest()
            .code(String.join(".", dataPath))
            .putParamsItem("item", t)
            .putParamsItem("model", view.componentModel().getData()));
  }

}
