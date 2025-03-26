package com.cri.smart_home.enums

enum class ControlType {
    SWITCH,         // On/Off control
    SLIDER,         // Range control (e.g., dimmer, temperature)
    RGB_PICKER,     // Color selection
    BUTTON_GROUP,   // Multiple button options
    NUMERIC_INPUT,  // Number input
    TEXT_DISPLAY,   // Read-only text display
    DROPDOWN,       // Selection from options
    SCHEDULE        // Time-based scheduling
}