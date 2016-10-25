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

    // register extension
    project.extensions.create('eclipseconfig', EclipseConfigExtension, project)

    EditorConfig editorConfig = new EditorConfig()

    applyCommonConfiguration(project, editorConfig)
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

    // JDT UI properties

    // remove properties on clean
    project.tasks.cleanEclipse.doLast {
      project.delete("${project.projectDir}/.settings/org.eclipse.jdt.ui.prefs")
    }

    // apply JDT UI settings
    project.tasks.eclipse.doLast {
      File jdtUIPrefs = project.file("${project.projectDir}/.settings/org.eclipse.jdt.ui.prefs")

      Map properties = new LinkedHashMap()
      if (!jdtUIPrefs.exists()) {
        properties['eclipse.preferences.version'] = '1'
      }

      // code templates
      File codeTemplatesFile = project.eclipseconfig.codeTemplates as File
      if (codeTemplatesFile.exists()) {
        def codeTemplates = codeTemplatesFile.text.replace('\n', '\\n').replace('\r', '').replaceAll(/=/, '\\\\=')
        // Enable using comment templates by default
        properties['org.eclipse.jdt.ui.javadoc'] = 'true'
        // Custom templates
        properties['org.eclipse.jdt.ui.text.custom_code_templates'] = codeTemplates
      }

      boolean saveActions = false
      boolean saveAdditionalActions = false

      // trailing whitespace
      if (settings['trim_trailing_whitespace']) {
        if ('true' == settings['trim_trailing_whitespace']) {
          saveActions = true // need save actions enabled
          saveAdditionalActions = true
          properties['sp_cleanup.remove_trailing_whitespaces'] = 'true'
          properties['sp_cleanup.remove_trailing_whitespaces_all'] = 'true'
          properties['sp_cleanup.remove_trailing_whitespaces_ignore_empty'] = 'false'
        }
        else {
          properties['sp_cleanup.remove_trailing_whitespaces'] = 'false'
        }
      }

      // enable save actions if needed (otherwise leave as-is)
      if (saveActions) {
        properties['editor_save_participant_org.eclipse.jdt.ui.postsavelistener.cleanup'] = 'true'
      }
      if (saveAdditionalActions) {
        properties['sp_cleanup.on_save_use_additional_actions'] = 'true'
      }

      // add custom properties (may override determined settings)
      properties.putAll(project.eclipseconfig.jdtUIProperties)

      Util.mergeProperties(jdtUIPrefs, properties)
    }
  }

  void applyCommonConfiguration(Project project, EditorConfig editorConfig) {
    File dummyFile = project.file('Dummy.java')

    def settings = toMap(editorConfig.getProperties(dummyFile.absolutePath))

    // remove properties on clean
    project.tasks.cleanEclipse.doLast {
      project.delete("${project.projectDir}/.settings/org.eclipse.core.resources.prefs")
      project.delete("${project.projectDir}/.settings/org.eclipse.core.runtime.prefs")
    }

    // apply resources settings
    project.tasks.eclipse.doLast {
      File prefs = project.file("${project.projectDir}/.settings/org.eclipse.core.resources.prefs")

      Map properties = new LinkedHashMap()
      if (!prefs.exists()) {
        properties['eclipse.preferences.version'] = '1'
      }

      // charset
      if (settings['charset']) {
        def charset
        // translate charset setting
        switch (settings['charset']) {
        case 'latin1':
          charset = 'ISO-8859-1'
          break
        case 'utf-8':
          charset = 'UTF-8'
          break
        case 'utf-16be':
          charset = 'UTF-16BE'
          break
        case 'utf-16le':
          charset = 'UTF-16LE'
          break
        }

        if (charset) {
          properties['encoding/<project>'] = charset
        }
        else {
          project.logger.error("Unsupported charset ${settings['charset']}")
        }
      }

      Util.mergeProperties(prefs, properties)
    }

    // apply runtime settings
    project.tasks.eclipse.doLast {
      File prefs = project.file("${project.projectDir}/.settings/org.eclipse.core.runtime.prefs")

      Map properties = new LinkedHashMap()
      if (!prefs.exists()) {
        properties['eclipse.preferences.version'] = '1'
      }

      // line ending
      if (settings['end_of_line']) {
        def lineEnding
        // translate line ending setting
        switch (settings['end_of_line']) {
        case 'lf':
          lineEnding = '\\n'
          break
        case 'crlf':
          lineEnding = '\\r\\n'
          break
        case 'cr':
          lineEnding = '\\r'
          break
        }

        if (lineEnding) {
          properties['line.separator'] = lineEnding
        }
        else {
          project.logger.error("Unsupported line ending ${settings['end_of_line']}")
        }
      }

      Util.mergeProperties(prefs, properties)
    }
  }
}
