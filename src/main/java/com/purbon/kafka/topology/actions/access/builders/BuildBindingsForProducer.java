package com.purbon.kafka.topology.actions.access.builders;

import com.purbon.kafka.topology.BindingsBuilderProvider;
import com.purbon.kafka.topology.model.users.Producer;
import java.util.List;

public class BuildBindingsForProducer implements AclBindingsOrErrorBuilder {

  private final BindingsBuilderProvider builderProvider;
  private final List<Producer> producers;
  private final String fullTopicName;
  private final boolean prefixed;

  public BuildBindingsForProducer(
      BindingsBuilderProvider builderProvider,
      List<Producer> producers,
      String fullTopicName,
      boolean prefixed) {
    this.builderProvider = builderProvider;
    this.producers = producers;
    this.fullTopicName = fullTopicName;
    this.prefixed = prefixed;
  }

  @Override
  public AclBindingsOrError getAclBindingsOrError() {
    return AclBindingsOrError.forAclBindings(
        builderProvider.buildBindingsForProducers(producers, fullTopicName, prefixed));
  }
}
