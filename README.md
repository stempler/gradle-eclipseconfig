gradle-eclipseconfig
====================

Simple plugin for configuring basic editor settings for your Gradle generated Eclipse project based on EditorConfig.
It builds on the [Gradle Eclipse plugin](https://docs.gradle.org/current/userguide/eclipse_plugin.html) and adapts the settings of the project generated with `gradle eclipse`.

EditorConfig
------------

Settings for editors are defined using an `.editorconfig` file. See the [EditorConfig website](http://editorconfig.org/) for more information. EditorConfig allows configuring indentation, trailing whitespace, line endings, encoding and other properties for the files to be edited.
A subset of these configuration options is supported by *gradle-eclipseconfig*.

Using this configuration format you can easily apply the settings for editing your project files to other editors.
There are [EditorConfig plugins](http://editorconfig.org/#download) for many applications available, for some (like IntelliJ IDEA) support is already built in.

There is also an [EditorConfig Eclipse plugin](https://github.com/ncjones/editorconfig-eclipse#readme), which you might want to check out - you can also use it in conjunction with *gradle-eclipseconfig*.
The main differences of the two are:
- *gradle-eclipseconfig* adapts the generated Eclipse project, while the Eclipse plugin applies the settings when an editor is opened (and thus can apply different settings depending on the location in the project)
- *gradle-eclipseconfig* doesn't require any plugins being installed in Eclipse
- which EditorConfig properties are supported (see also below)

### Supported properties

- `indent_style` (Java/JDT)
- `indent_size` (Java/JDT)
- `tab_width` (Java/JDT)

#### Planned

- `trim_trailing_whitespace`
- `charset`

### Limitations

*gradle-eclipseconfig* works a bit diferently than the usual EditorConfig plugin. As it adapts the project settings, it can't provide individual settings for files being opened. Instead it applies a general configuration on the whole project.

Usage
-----

**TODO:** This section will be added once the first version has been published.
