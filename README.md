# Velocity UI for Eclipse

This project leverages the [Eclipse platform](http://www.eclipse.org/) by adding support for the template/scripting
engine [Apache Velocity](http://jakarta.apache.org/velocity/).

It uses the Velocity binaries (currently v1.7.0) to provide a Velocity-aware text editor (associated to files with extensions `*.vsl` or `*.vm`) with outline view.

![Velocity Editor](/../images/screenshots/Editor.png?raw=true")
	

## Editor Features

* Syntax-highlight for [Velocity Template Language (VTL)](http://velocity.apache.org/engine/devel/vtl-reference-guide.html)
* Outline view with hierarchy of VTL directives and Velocimacro references
* Content assist for VTL directives ([Screenshot](../images/screenshots/ContentAssistDirectives.png?raw=true)) and references ([Screenshot](../images/screenshots/ContentAssistReferences.png?raw=true)) -> auto-activation after the character '#' or '$' or manually via keyboard shortcut `Ctrl+Space`
* Automatic template validation while typing (the first syntax error is show in task list and the according line is marked in annotation column) -> [Screenshot](../images/screenshots/SyntaxError.png?raw=true)
* Annotation hover with definition of references ([Screenshot](../images/screenshots/AnnotationHover.png?raw=true)) and template syntax errors ([Screenshot](../images/screenshots/SyntaxErrorAnnotation.png?raw=true))
* Go to definition of variable or Velocimacro reference under current cursor position (via navigate + context menu or keyboard shortcut `F3`)
* Use Workbench's navigation history to return to previous location (keyboard shortcut 'Alt + Arrow Left').
* Comment and uncomment blocks (via `Ctrl+/` and `Ctrl+\`)
* Preference pages for definition of Velocity counter name and user-defined Velocity directives ([Screenshot](../images/screenshots/PreferencesUserDirectives.png?raw=true))
* Preference pages for color settings of the editor's syntax highlighting ([Screenshot](../images/screenshots/PreferencesSyntaxHighlighting.png?raw=true))
* Preference pages for Velocimacro library ([Screenshot](../images/screenshots/PreferencesVelocimacroLibrary.png?raw=true))


## Installation

Online updates are available on [https://raw.githubusercontent.com/vaulttec/iveloedit/updatesite/](https://raw.githubusercontent.com/vaulttec/iveloedit/updatesite/).
Define a site bookmark in Eclipse's Update Manager view ([Screenshot](../images/screenshots/UpdateSiteBookmark.png?raw=true)), navigate to the update site via this bookmark ([Screenshot](../images/screenshots/UpdateSitePreview.png?raw=true)) and install the Velocity UI feature.
