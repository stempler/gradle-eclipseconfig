/*
 * Copyright 2016 Simon Templer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.standardout.gradle.econfig

import org.gradle.api.Project;

/**
 * Extension object for EclipseConfig plugin.
 *
 * @author Simon Templer
 */
class EclipseConfigExtension {

  /**
   * @param project the project the extension is applied to
   */
  public EclipseConfigExtension(Project project) {
    super();
    this.project = project;
  }

  // public API

  final def jdtUI(Closure config) {
    Closure cl = config.clone()
    cl(jdtUIProperties)
  }

  /**
   * Location of Eclipse code templates file.
   */
  def codeTemplates = project.file('codetemplates.xml')

  // internal

  private final Project project

  final Map jdtUIProperties = [:]

}
