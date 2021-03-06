/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.javaee.web.artifact;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.javaee.appServerIntegrations.ApplicationServerUrlMapping;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactType;
import consulo.javaee.module.extension.JavaWebModuleExtension;

/**
 * @author nik
 */
public abstract class WebArtifactUtil {

  public static WebArtifactUtil getInstance() {
    return ServiceManager.getService(WebArtifactUtil.class);
  }

  public abstract boolean isWebApplication(@Nonnull ArtifactType artifactType);

  /**
   * @deprecated use {@link ApplicationServerUrlMapping} instead
   */
  @Nullable
  public abstract String getContextRoot(@Nonnull Artifact earArtifact, @Nonnull JavaWebModuleExtension webFacet);

  @Nullable
  public abstract String getModuleWebUri(@Nonnull Artifact earArtifact, @Nonnull JavaWebModuleExtension webFacet);

  public abstract ArtifactType getExplodedWarArtifactType();

  public abstract ArtifactType getWarArtifactType();

  public abstract Collection<? extends Artifact> getWebArtifacts(@Nonnull Project project);

  public abstract void addLibrary(@Nonnull Library library, @Nonnull Artifact artifact, @Nonnull Project project);

  public abstract Collection<? extends ArtifactType> getWebArtifactTypes();
}
