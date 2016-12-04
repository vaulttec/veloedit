# Velocity Editor for Eclipse

This project leverages the [Eclipse platform](http://www.eclipse.org/) by adding support for the template/scripting
engine [Apache Velocity](http://velocity.apache.org).

It uses the parser of the [Velocity engine](http://velocity.apache.org/engine/) (currently v1.7.0) to provide a Velocity-aware text editor with outline view.

![Velocity Editor](/../images/screenshots/Editor.png?raw=true")

The editor is associated to files with the extensions `*.vsl` or `*.vm`. This is defined in a content type available from `Preferences > General > Content Type > Text > Velocity Template`. Additional file name extensions can be specified here as well.


## Editor Features

* Syntax-highlight for [Velocity Template Language (VTL)](http://velocity.apache.org/engine/devel/vtl-reference-guide.html)
* Outline view with hierarchy of VTL directives and Velocimacro references
* Content assist for VTL directives ([Screenshot](../images/screenshots/ContentAssistDirectives.png?raw=true)) and references ([Screenshot](../images/screenshots/ContentAssistReferences.png?raw=true)) -> auto-activation after the character `#` or `$` or manually via keyboard shortcut `Ctrl+Space`
* Automatic template validation while typing (the first syntax error is shown in task list and the according line is marked in annotation column) -> [Screenshot](../images/screenshots/SyntaxError.png?raw=true)
* Annotation hover with definition of references ([Screenshot](../images/screenshots/AnnotationHover.png?raw=true)) and template syntax errors ([Screenshot](../images/screenshots/SyntaxErrorAnnotation.png?raw=true))
* Go to definition of variable or Velocimacro reference under current cursor position (via navigate + context menu or keyboard shortcut `F3`)
* Use Workbench's navigation history to return to previous location (keyboard shortcut `Alt + Arrow Left`).
* Comment and uncomment blocks (via `Ctrl+/` and `Ctrl+\`)
* Preference pages for definition of Velocity counter name and user-defined Velocity directives ([Screenshot](../images/screenshots/PreferencesUserDirectives.png?raw=true))
* Preference pages for color settings of the editor's syntax highlighting ([Screenshot](../images/screenshots/PreferencesSyntaxHighlighting.png?raw=true))
* Preference pages for Velocimacro library ([Screenshot](../images/screenshots/PreferencesVelocimacroLibrary.png?raw=true))


## Installation

Online updates are available from [https://raw.githubusercontent.com/vaulttec/veloedit/updatesite/](https://raw.githubusercontent.com/vaulttec/veloedit/updatesite/).

To install the plugin use this URL in Eclipse's Update Manager (`Help > Install New Software`) or drag the following badge to your running Eclipse workspace (requires [Eclipse Marketplace Plugin](http://www.eclipse.org/mpc/)):

<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=2985127" class="drag" title="Drag to your running Eclipse workspace to install veloedit"><img class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png" alt="Drag to your running Eclipse workspace to install veloedit" /></a>
