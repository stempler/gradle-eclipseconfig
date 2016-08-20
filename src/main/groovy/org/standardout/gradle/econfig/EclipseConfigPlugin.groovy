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

import org.editorconfig.core.EditorConfig
import org.gradle.api.Plugin
import org.gradle.api.Project

class EclipseConfigPlugin implements Plugin<Project> {
  void apply(Project project) {
    // ensure eclipse plugin is applied
    project.apply(plugin: 'eclipse')

    EditorConfig editorConfig = new EditorConfig()

    applyJavaConfiguration(project, editorConfig)
  }

  private Map toMap(List<EditorConfig.OutPair> properties) {
    def result = [:]
    properties.each {
      result[it.key] = it.val
    }
    result
  }

  void applyJavaConfiguration(Project project, EditorConfig editorConfig) {
    File dummyFile = project.file('Dummy.java')

    def settings = toMap(editorConfig.getProperties(dummyFile.absolutePath))

    // JDT properties
    project.eclipse {
      jdt {
        file {
          withProperties { properties ->
            if (settings['indent_style']) {
              //TODO verify if both tab and space are correct settings here
              properties['org.eclipse.jdt.core.formatter.tabulation.char'] = settings['indent_style']
            }
            if (settings['indent_size']) {
              properties['org.eclipse.jdt.core.formatter.indentation.size'] = settings['indent_size']
            }
            if (settings['tab_width'] || settings['indent_size']) {
              properties['org.eclipse.jdt.core.formatter.tabulation.size'] = settings['tab_width'] ?: settings['indent_size']
            }
          }
        }
      }
    }
  }
}
