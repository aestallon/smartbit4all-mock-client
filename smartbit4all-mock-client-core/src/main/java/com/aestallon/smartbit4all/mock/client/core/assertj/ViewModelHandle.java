package com.aestallon.smartbit4all.mock.client.core.assertj;

import org.assertj.core.api.AbstractObjectAssert;

public class ViewModelHandle<T> extends AbstractObjectAssert<ViewModelHandle<T>, T> {
  
  ViewModelHandle(T t) {
    super(t, ViewModelHandle.class);
  }

}
