package com.purbon.kafka.topology.actions.access.builders;

import com.purbon.kafka.topology.BindingsBuilderProvider;
import com.purbon.kafka.topology.model.users.platform.ControlCenterInstance;

public class BuildBindingsForControlCenter implements AclBindingsOrErrorBuilder {

  private final BindingsBuilderProvider builderProvider;
  private final ControlCenterInstance controlCenter;

  public BuildBindingsForControlCenter(
      BindingsBuilderProvider builderProvider, ControlCenterInstance controlCenter) {
    this.builderProvider = builderProvider;
    this.controlCenter = controlCenter;
  }

  @Override
  public AclBindingsOrError getAclBindingsOrError() {
    return AclBindingsOrError.forAclBindings(
        builderProvider.buildBindingsForControlCenter(
            controlCenter.getPrincipal(), controlCenter.getAppId()));
  }
}
