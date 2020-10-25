package com.purbon.kafka.topology.actions.access.builders;

import com.purbon.kafka.topology.BindingsBuilderProvider;
import com.purbon.kafka.topology.actions.BaseAccessControlAction;
import com.purbon.kafka.topology.model.users.Consumer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildBindingsForConsumer extends BaseAccessControlAction {

  private final String fullTopicName;
  private final List<Consumer> consumers;
  private final BindingsBuilderProvider builderProvider;
  private boolean prefixed;

  public BuildBindingsForConsumer(
      BindingsBuilderProvider builderProvider, List<Consumer> consumers, String fullTopicName) {
    this(builderProvider, consumers, fullTopicName, false);
  }

  public BuildBindingsForConsumer(
      BindingsBuilderProvider builderProvider,
      List<Consumer> consumers,
      String fullTopicName,
      boolean prefixed) {
    super();
    this.consumers = consumers;
    this.fullTopicName = fullTopicName;
    this.builderProvider = builderProvider;
    this.prefixed = prefixed;
  }

  @Override
  protected void execute() {
    bindings = builderProvider.buildBindingsForConsumers(consumers, fullTopicName, prefixed);
  }

  @Override
  protected Map<String, Object> props() {
    List<String> principals =
        consumers.stream().map(c -> c.getPrincipal()).collect(Collectors.toList());
    Map<String, Object> map = new HashMap<>();
    map.put("Operation", getClass().getName());
    map.put("Principals", principals);
    map.put("Topic", fullTopicName);
    return map;
  }
}
