gradle-eclipseconfig
====================

Simple plugin for configuring basic editor settings for your Gradle generated Eclipse project based on EditorConfig.
It builds on the [Gradle Eclipse plugin](https://docs.gradle.org/current/userguide/eclipse_plugin.html) and adapts the settings of the project generated with `gradle eclipse`.

Usage
-----

**TODO:** This section will be added once the first version has been published.


Configuration
-------------

### EditorConfig

Settings for editors are defined using an `.editorconfig` file. See the [EditorConfig website](http://editorconfig.org/) for more information. EditorConfig allows configuring indentation, trailing whitespace, line endings, encoding and other properties for the files to be edited.
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

##### Planned

- `charset`

#### Limitations

*gradle-eclipseconfig* works a bit diferently than the usual EditorConfig plugin. As it adapts the project settings, it can't provide individual settings for files being opened. Instead it applies a general configuration on the whole project.

The project settings that are set right now are related to the Eclipse JDT (Java Development Tools) - so the settings apply to developing with Java, but apply for instance also for the *Groovy Eclipse* plugin.


### Code templates

Eclipse projects allow to configure code templates. If you provide a `codetemplates.xml` file in your project directory, *gradle-eclipseconfig* will pick it up and use it to configure the Eclipse project with your code templates.

The easiest way to create such a file is by exporting it from Eclipse. You can do so either in the workspace settings or the settings of a project (*Java Code Style*/*Code Templates*).

If you are having problems with the code templates not being applied, check if your templates are marked as enabled (`enabled="true"`) and are *not* marked deleted (should be `deleted="false"`).
