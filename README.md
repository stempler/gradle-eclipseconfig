gradle-eclipseconfig
====================

Simple plugin for configuring basic editor settings for your Gradle generated Eclipse project based on EditorConfig.
It builds on the [Gradle Eclipse plugin](https://docs.gradle.org/current/userguide/eclipse_plugin.html) and adapts the settings of the project generated with `gradle eclipse`.


Usage
-----

### Gradle 2.1 and higher

***There seems to be some trouble right now when that makes the plugin configuration fail when using this notation. Please use the notation for all Gradle versions if you experience any problems with this one.***

```groovy
plugins {
  id "org.standardout.eclipseconfig" version "1.0.0"
}
```

### All Gradle versions

```groovy
buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    classpath 'org.standardout:gradle-eclipseconfig:1.0.0'
  }
}

apply plugin: 'org.standardout.eclipseconfig'
```

Applying the *eclipseconfig* plugin will implicitly also apply the Gradle built-in *eclipse* plugin (if it is not applied yet), as *eclipseconfig* extends its tasks.

### Getting started

For configuring the plugin you should use an `.editorconfig` file. See the following sections for more information and configuration options apart from [EditorConfig](http://editorconfig.org/).

Here is a small example on how the `.editorconfig` might look like:

```
root = true

[*]
charset = utf-8
trim_trailing_whitespace = true
indent_style = space
indent_size = 2
```

To generate the Eclipse project use the `eclipse` task of the Gradle Eclipse plugin:

```
gradle eclipse
```

If you want to make sure that the project contains only the generated content and settings, also run `cleanEclipse`:

```
gradle cleanEclipse eclipse
```


Configuration
-------------

### EditorConfig

Settings for editors are defined using an `.editorconfig` file. See the [EditorConfig website](http://editorconfig.org/) for more information. EditorConfig allows for configuring indentation, trailing whitespace, line endings, encoding and other properties for the files to be edited.
A subset of these configuration options is supported by *gradle-eclipseconfig*.

Using this configuration format you can easily apply the settings for editing your project files to other editors.
There are [EditorConfig plugins](http://editorconfig.org/#download) for many applications available, for some (like IntelliJ IDEA) support is already built in.

There is also an [EditorConfig Eclipse plugin](https://github.com/ncjones/editorconfig-eclipse#readme), which you might want to check out - you can also use it in conjunction with *gradle-eclipseconfig*.
The main differences of the two are:
- *gradle-eclipseconfig* adapts the generated Eclipse project, while the Eclipse plugin applies the settings when an editor is opened (and thus can apply different settings depending on the location in the project)
- *gradle-eclipseconfig* doesn't require any plugins being installed in Eclipse
- which EditorConfig properties are supported (see also below)

#### Supported properties

- `indent_style` (Java/JDT)
- `indent_size` (Java/JDT)
- `tab_width` (Java/JDT)
- `trim_trailing_whitespace` (Java/JDT)
- `charset`
- `end_of_line`

#### Limitations

*gradle-eclipseconfig* works a bit diferently than the usual EditorConfig plugin. As it adapts the project settings, it can't provide individual settings for files being opened. Instead it applies a general configuration on the whole project.

The project settings that are set right now are related to the Eclipse JDT (Java Development Tools) - so the settings apply to developing with Java, but apply for instance also for the *Groovy Eclipse* plugin.


### Code templates

Eclipse projects allow code templates to be configured. If you provide a `codetemplates.xml` file in your project directory, *gradle-eclipseconfig* will pick it up and use it to configure the Eclipse project with your code templates.

The easiest way to create such a file is by exporting it from Eclipse. You can do so either in the workspace settings or in the settings of a project (*Java Code Style*/*Code Templates*).

If you are having problems with the code templates not being applied, check if your templates are marked as enabled (`enabled="true"`) and are *not* marked deleted (should be `deleted="false"`).

You can also use code templates from a custom location. This allows you for instance to use a single template in a multi-project setup. Tell *gradle-eclipseconfig* where to locate the file, for example like this:

```groovy
eclipseconfig {
  codeTemplates = rootProject.file('codetemplates.xml')
}
```


### Custom settings

Eclipse project configuration is done via a couple of XML and Properties files.
In addition to the predefined configuration options you can also configure properties on your own.

To learn what kind of properties you can set, the easiest approach is usually to change the project settings in Eclipse via the UI, and then inspect the configuration files.

#### JDT UI properties

You can configure JDT UI properties that are not directly supported by *gradle-eclipseconfig*, by specifying them explicitly like this:

```groovy
eclipseconfig {
  jdtUI { properties ->
    // set properties for the file org.eclipse.jdt.ui.prefs

    // make private fields final on save, if possible
    properties.'sp_cleanup.make_variable_declarations_final' = true
    properties.'sp_cleanup.make_private_fields_final' = true
  }
}
```

In the JDT UI settings for instance the Eclipse Editor save actions are configured.
So if you want to do more than just removing trailing white space on save, you can add the respective properties here.

#### JDT Core properties

The Gradle Eclipse plugin already offers the possibility to adapt the JDT properties, for example:

```groovy
eclipse {
  jdt {
    file {
      withProperties { properties ->
        // set properties for the file org.eclipse.jdt.core.prefs
        properties['org.eclipse.jdt.core.compiler.debug.lineNumber'] = 'generate'
      }
    }
  }
}
```
