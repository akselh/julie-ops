package com.purbon.kafka.topology.actions.access.builders;

import com.purbon.kafka.topology.BindingsBuilderProvider;
import com.purbon.kafka.topology.model.users.platform.SchemaRegistryInstance;

public class SchemaRegistryAclBindingsBuilder implements AclBindingsOrErrorBuilder {

  private final BindingsBuilderProvider builderProvider;
  private final SchemaRegistryInstance schemaRegistry;

  public SchemaRegistryAclBindingsBuilder(
      BindingsBuilderProvider builderProvider, SchemaRegistryInstance schemaRegistry) {
    this.builderProvider = builderProvider;
    this.schemaRegistry = schemaRegistry;
  }

  @Override
  public AclBindingsOrError getAclBindingsOrError() {
    return AclBindingsOrError.forAclBindings(
        builderProvider.buildBindingsForSchemaRegistry(schemaRegistry));
  }
}
