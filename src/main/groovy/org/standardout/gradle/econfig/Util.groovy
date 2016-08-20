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

import org.gradle.api.Project

class Util {

  /**
   * Merges a given properties map into a properties file, retaining order and comments in the file.
   * @param propertiesFile The properties file
   * @param properties     The properties to merge into the file
   */
  static void mergeProperties(File propertiesFile, Map properties) {
    if (!properties) {
      return
    }

    def lines
    if (propertiesFile.exists()) {
      lines = propertiesFile.readLines()
    }
    else {
      lines = []
    }

    Map append = new LinkedHashMap(properties)

    // replace properties
    lines = lines.collect { line ->
      Set remaining = new HashSet(append.keySet())

      remaining.findResult(line) { property ->
        if (line.startsWith(property + '=')) {
          // replace property
          property + '=' + append.remove(property)
        }
        else {
          null
        }
      }
    }

    if (append) {
      // add any remaining properties
      append.each { property, value ->
        lines << property + '=' + value
      }
    }

    propertiesFile.withPrintWriter { writer ->
      lines.each {
        writer.println(it)
      }
    }
  }

}
