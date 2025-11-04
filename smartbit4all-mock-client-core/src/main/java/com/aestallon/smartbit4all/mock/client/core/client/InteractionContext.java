package com.aestallon.smartbit4all.mock.client.core.client;

import java.util.ArrayList;
import java.util.List;

public record InteractionContext(List<String> interactions) {

  public InteractionContext(String interaction) {
    this(new ArrayList<>(List.of(interaction)));
  }

  InteractionContext push(String interaction) {
    interactions.add(interaction);
    return this;
  }

  @Override
  public String toString() {
    final var sb = new StringBuilder("INTERACTION: ").append(interactions.getFirst());
    for (int i = 1; i < interactions.size(); i++) {
      sb.append("\n").append(i).append(". ").append(interactions.get(i));
    }
    return sb.toString();
  }
}
