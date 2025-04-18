# Flutter Freezed Live Template

[![JetBrains Marketplace Version](https://img.shields.io/jetbrains/plugin/v/com.dhh.flutter_freezed_live_template?style=flat-square)](https://plugins.jetbrains.com/plugin/com.dhh.flutter_freezed_live_template)
[![JetBrains Marketplace Downloads](https://img.shields.io/jetbrains/plugin/d/com.dhh.flutter_freezed_live_template?style=flat-square)](https://plugins.jetbrains.com/plugin/com.dhh.flutter_freezed_live_template)

This plugin provides convenient **Live Templates** for quickly generating boilerplate code for data classes and models using the fantastic [freezed](https://pub.dev/packages/freezed) package, along with [freezed_annotation](https://pub.dev/packages/freezed_annotation) and [json_serializable](https://pub.dev/packages/json_serializable).

## Features

This plugin includes the following Live Templates:

*   **`frzd`**: Creates a basic Freezed class structure with the necessary `part` directive for `.freezed.dart`. Ideal for simple sealed classes or data classes without JSON serialization.
*   **`frzdmodel`**: Generates a more complete Freezed model structure including:
    *   `part` directives for both `.freezed.dart` and `.g.dart` (for `json_serializable`).
    *   A `fromJson` factory constructor ready for JSON deserialization.

## Installation

1.  Go to `Settings/Preferences` > `Plugins`.
2.  Select the `Marketplace` tab.
3.  Search for "Flutter Freezed Live Template".
4.  Click `Install`.
5.  Restart your IDE if prompted.

## Usage

Simply type the template abbreviation inside a Dart file and press `Tab` or `Enter`.

### `frzd` Template

Type `frzd` and press `Tab`:

```dart
import 'package:freezed_annotation/freezed_annotation.dart';

part 'your_file_name.freezed.dart';

@freezed
class YourClassName with _$YourClassName {
  const factory YourClassName({
    // Add your fields here
  }) = _YourClassName;
}
```

### `frzdmodel` Template

Type `frzdmodel` and press `Tab`:

```dart
import 'package:freezed_annotation/freezed_annotation.dart';

part 'your_file_name.freezed.dart';
part 'your_file_name.g.dart';

@freezed
class YourClassName with _$YourClassName {
  const factory YourClassName({
    // Add your fields here
  }) = _YourClassName;
  
  const YourClassName._();
  
  factory YourClassName.initial() => const YourClassName();
  
  factory YourClassName.fromJson(Map<String, dynamic> json) => _$YourClassNameFromJson(json);
}
```
