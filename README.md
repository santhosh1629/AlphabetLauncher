# Alphabet Launcher

A minimal Android launcher that provides fast app navigation using an A–Z alphabet bar and gesture-based search.

## Features

- Live current time and date
- Displays 7 favourite apps on the home screen
- A–Z alphabet navigation bar
- Interactive curved alphabet animation
- Selected letter highlight
- Filters apps by starting letter
- Bottom-to-top swipe to open Search
- Search apps by name
- Automatically opens the keyboard in Search
- Tap an app to launch it
- Apps are loaded and cached to avoid querying PackageManager during every touch event

## Architecture

The project follows the MVVM architecture with separate components for UI, application data, and business logic.

## Curve Animation

The A–Z alphabet bar is implemented using a custom Android View.

When the user touches or drags along the alphabet bar:

1. The current finger Y position is recorded.
2. The distance between the finger and each letter is calculated.
3. An exponential function is used to calculate the bend amount.
4. Letters closer to the finger move farther to the left.
5. Letters farther from the finger move less.
6. When the finger is released, the curve returns to its normal position.

The main calculation is based on the distance between the touch position and each letter.

## Features

- Live current time and date
- 7 favourite apps on the home screen
- A–Z alphabet navigation
- Curved alphabet animation
- Letter-based app filtering
- Gesture-based search
- App search by name
- Automatic keyboard on search
- Tap to launch applications

## Technologies Used

- Kotlin
- XML
- MVVM
- RecyclerView
- View Binding
- Kotlin Coroutines
- StateFlow
- Android PackageManager
     v
PackageManager
