/*
 * Copyright 2000-2007 JetBrains s.r.o.
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
package com.intellij.javaee.model;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.javaee.model.common.ejb.EjbWithHome;
import com.intellij.javaee.model.common.ejb.EnterpriseBean;
import com.intellij.javaee.model.common.ejb.MessageDrivenBean;
import com.intellij.javaee.model.common.ejb.SessionBean;
import com.intellij.javaee.model.enums.MethodIntf;
import com.intellij.javaee.model.xml.ejb.Method;
import com.intellij.javaee.model.xml.ejb.MethodParams;
import com.intellij.psi.PsiClass;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.GenericValue;

/**
 * @author peter
 */
public class EjbMethodResolveConverter extends com.intellij.util.xml.converters.AbstractMethodResolveConverter<Method> {

  public EjbMethodResolveConverter() {
    super(Method.class);
  }

  @Nonnull
  protected Collection<PsiClass> getPsiClasses(final Method parent, final ConvertContext context) {
    return getTargetClasses(parent.getEnterpriseBean().getValue(), parent.getMethodIntf().getValue(), true);
  }

  public static Collection<PsiClass> getTargetClasses(@Nullable final EnterpriseBean ejb,
                                                      @Nullable final MethodIntf methodIntf,
                                                      final boolean includeEjbClass) {
    final Collection<PsiClass> targetClasses = new HashSet<>();
    boolean allInterfaces = false;
    MethodIntf curMethodIntf = methodIntf;
    if (methodIntf == null) {
      allInterfaces = true;
      curMethodIntf = MethodIntf.HOME; // first one in the switch
      if (includeEjbClass && ejb != null) {
        targetClasses.add(ejb.getEjbClass().getValue());
      }
    }
    switch (curMethodIntf) {
      case HOME:
        if (ejb instanceof EjbWithHome) {
          ContainerUtil.addIfNotNull(targetClasses, ((EjbWithHome)ejb).getHome().getValue());
        }
        if (!allInterfaces) break;
      case LOCAL_HOME:
        if (ejb instanceof EjbWithHome) {
          ContainerUtil.addIfNotNull(targetClasses, ((EjbWithHome)ejb).getLocalHome().getValue());
        }
        if (!allInterfaces) break;
      case REMOTE:
        if (ejb instanceof EjbWithHome) {
          ContainerUtil.addIfNotNull(targetClasses, ((EjbWithHome)ejb).getRemote().getValue());
        }
        if (ejb instanceof SessionBean) {
          for (GenericValue<PsiClass> value : ((SessionBean)ejb).getBusinessRemotes()) {
            ContainerUtil.addIfNotNull(targetClasses, value.getValue());
          }
        }
        if (!allInterfaces) break;
      case LOCAL:
        if (ejb instanceof EjbWithHome) {
          ContainerUtil.addIfNotNull(targetClasses, ((EjbWithHome)ejb).getLocal().getValue());
        }
        if (ejb instanceof SessionBean) {
          for (GenericValue<PsiClass> value : ((SessionBean)ejb).getBusinessLocals()) {
            ContainerUtil.addIfNotNull(targetClasses, value.getValue());
          }
        }
        else if (ejb instanceof MessageDrivenBean) {
          ContainerUtil.addIfNotNull(targetClasses, ((MessageDrivenBean)ejb).getMessageListenerInterface().getValue());
        }
        if (!allInterfaces) break;
      case SERVICE_ENDPOINT:
        if (ejb instanceof SessionBean) {
          ContainerUtil.addIfNotNull(targetClasses, ((SessionBean)ejb).getServiceEndpoint().getValue());
        }
        if (!allInterfaces) break;
      case MESSAGE_ENDPOINT:
        if (ejb instanceof MessageDrivenBean) {
          ContainerUtil.addIfNotNull(targetClasses, ((MessageDrivenBean)ejb).getMessageListenerInterface().getValue());
        }
        if (!allInterfaces) break;
      case TIMER:
        if (!allInterfaces && ejb != null) {
          ContainerUtil.addIfNotNull(targetClasses, ejb.getEjbClass().getValue());
        }
        break;
    }
    return targetClasses;
  }
  

  @Nonnull
  protected MethodParams getMethodParams(@Nonnull final Method parent) {
    return parent.getMethodParams();
  }
}
