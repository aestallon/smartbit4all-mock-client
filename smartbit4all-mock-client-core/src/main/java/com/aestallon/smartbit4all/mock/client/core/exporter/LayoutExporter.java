package com.aestallon.smartbit4all.mock.client.core.exporter;

import java.io.IOException;
import java.io.OutputStream;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.CompositeLayout;

public interface LayoutExporter {
  
  void export(CompositeLayout layout, OutputStream out) throws IOException;
  
}
