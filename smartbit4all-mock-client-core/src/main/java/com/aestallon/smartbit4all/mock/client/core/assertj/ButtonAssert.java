package com.aestallon.smartbit4all.mock.client.core.assertj;

import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.smartbit4all.api.view.bean.UiActionButtonType;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.Button;

public class ButtonAssert extends AbstractComponentAssert<ButtonAssert, Button> {

  protected ButtonAssert(ComponentLocator<Button> locator) {
    super(locator);
  }

  @Override
  protected ButtonAssert self() {
    return this;
  }

  public ButtonAssert isEnabled() {
    return checkOnPresentOrFail(
        it -> {
          if (Boolean.TRUE.equals(it.action().getDisabled())) {
            Assertions.fail("Expected button to be enabled but it is disabled: " + it.action());
          }
        },
        "Cannot check enabled property of ");
  }

  public ButtonAssert isDisabled() {
    return checkOnPresentOrFail(
        it -> {
          if (!Boolean.TRUE.equals(it.action().getDisabled())) {
            Assertions.fail("Expected button to be disabled but it is enabled: " + it.action());
          }
        },
        "Cannot check disabled property of ");
  }

  public ButtonAssert hasLabel(String label) {
    return checkOnPresentOrFail(
        it -> {
          if (!Objects.equals(label, it.label())) {
            Assertions.fail(
                "Expected button to have label '" + label + "' but it has label '" + it.label()
                + "': " + it.action());
          }
        },
        "Cannot check label property of ");
  }

  public ButtonAssert hasCode(String code) {
    return checkOnPresentOrFail(
        it -> {
          if (!Objects.equals(code, it.code())) {
            Assertions.fail(
                "Expected button to have code '" + code + "' but it has code '" + it.code()
                + "': " + it.action());
          }
        },
        "Cannot check code property of ");
  }

  public ButtonAssert hasIdentifier(String identifier) {
    return checkOnPresentOrFail(
        it -> {
          if (!Objects.equals(identifier, it.action().getIdentifier())) {
            Assertions.fail(
                "Expected button to have identifier '" + identifier + "' but it has identifier '"
                + it.action().getIdentifier()
                + "': " + it.action());
          }
        },
        "Cannot check identifier property of ");
  }

  public ButtonAssert hasColour(String colour) {
    return checkOnPresentOrFail(
        it -> {
          if (!Objects.equals(colour, it.action().getDescriptor().getColor())) {
            Assertions.fail(
                "Expected button to have colour '" + colour + "' but it has colour '" + it.action()
                    .getIdentifier()
                + "': " + it.action());
          }
        },
        "Cannot check colour property of ");
  }

  public ButtonAssert hasType(UiActionButtonType type) {
    return checkOnPresentOrFail(
        it -> {
          final var descriptor = it.descriptor();
          if (descriptor == null) {
            Assertions.fail("Expected button to have descriptor but it does not: " + it.action());
            return;
          }
          
          if (!Objects.equals(type, descriptor.getType())) {
            Assertions.fail(
                "Expected button to have type '" + type + "' but it has type '"
                + descriptor.getType() + "': " + it.action());
          }
        },
        "Cannot check type property of ");
  }

}
