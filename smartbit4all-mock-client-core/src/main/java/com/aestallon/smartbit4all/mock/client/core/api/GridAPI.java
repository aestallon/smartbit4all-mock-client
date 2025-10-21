package com.aestallon.smartbit4all.mock.client.core.api;

import java.util.UUID;
import org.smartbit4all.api.grid.bean.GridModel;
import org.smartbit4all.api.grid.bean.GridSelectionChange;
import org.smartbit4all.api.grid.bean.GridUpdateData;
import org.smartbit4all.api.view.bean.ViewContextChange;

public interface GridAPI {

  GridModel load(UUID uuid, String gridIdentifier);
  
  void setPage(UUID uuid, String gridId, String offsetStr, String limitStr);
  
  void update(UUID uuid, String gridId, GridUpdateData gridUpdateData);
  
  Object expand(UUID uuid, String gridId, String rowId);
  
  ViewContextChange select(UUID uuid, String gridId, String rowId, boolean selected);
  
  ViewContextChange selectRows(UUID uuid, String gridId, GridSelectionChange gridSelectionChange);
  
  ViewContextChange selectAll(UUID uuid, String gridId, boolean selected);
  
}
