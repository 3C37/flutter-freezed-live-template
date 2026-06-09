# Flutter Freezed Live Template

[![JetBrains Marketplace Version](https://img.shields.io/jetbrains/plugin/v/com.dhh.flutter_freezed_live_template?style=flat-square)](https://plugins.jetbrains.com/plugin/com.dhh.flutter_freezed_live_template)
[![JetBrains Marketplace Downloads](https://img.shields.io/jetbrains/plugin/d/com.dhh.flutter_freezed_live_template?style=flat-square)](https://plugins.jetbrains.com/plugin/com.dhh.flutter_freezed_live_template)

This plugin provides convenient **Live Templates** and **File Nesting** for Flutter/Dart projects that use [freezed](https://pub.dev/packages/freezed), [freezed_annotation](https://pub.dev/packages/freezed_annotation), and [json_serializable](https://pub.dev/packages/json_serializable).

The templates are aligned with **Freezed 3.x primary constructor syntax**.

## Features

### Live Templates

* **`frzd`**: Creates a Freezed 3.x data class structure with the required `.freezed.dart` `part` directive.
* **`frzdmodel`**: Creates a Freezed 3.x JSON-serializable model structure with:
  * `.freezed.dart` and `.g.dart` `part` directives.
  * A `fromJson` factory constructor for `json_serializable`.
  * An `initial()` factory helper.

The templates are available at the top level of `.dart` files.

### File Nesting

The plugin groups generated files under their source Dart file in the Project View:

* `example.freezed.dart` under `example.dart`
* `example.g.dart` under `example.dart`

File nesting rules are enabled once on project startup after installation. If you later disable file nesting manually, the plugin preserves your choice.

## Installation

1. Go to `Settings/Preferences` > `Plugins`.
2. Select the `Marketplace` tab.
3. Search for `Flutter Freezed Live Template`.
4. Click `Install`.
5. Restart your IDE if prompted.

## Usage

Type the template abbreviation at the top level of a Dart file and press `Tab` or `Enter`.

### `frzd` Template

Type `frzd` and press `Tab`:

```dart
import 'package:freezed_annotation/freezed_annotation.dart';

part 'your_file_name.freezed.dart';

@freezed
abstract class YourClassName with _$YourClassName {
  const YourClassName._();

  const factory YourClassName({
    // Add your fields here
  }) = _YourClassName;

  factory YourClassName.initial() => const YourClassName();
}
```

### `frzdmodel` Template

Type `frzdmodel` and press `Tab`:

```dart
import 'package:freezed_annotation/freezed_annotation.dart';

part 'your_file_name.freezed.dart';
part 'your_file_name.g.dart';

@freezed
abstract class YourClassName with _$YourClassName {
  const YourClassName._();

  const factory YourClassName({
    // Add your fields here
  }) = _YourClassName;

  factory YourClassName.initial() => const YourClassName();

  factory YourClassName.fromJson(Map<String, Object?> json) => _$YourClassNameFromJson(json);
}
```

## Compatibility

Version `1.0.6` updates templates for Freezed 3.x and migrates stale IDE live template settings from older plugin versions. It also removes unnecessary mandatory Android, Flutter, and Dart plugin dependencies so the plugin can verify cleanly against IntelliJ IDEA 2026.1.

The plugin only depends on IntelliJ Platform modules and does not require the Flutter/Dart plugins to be installed for JetBrains Marketplace verification.
