package com.cri.smart_home.enums

enum class Label {
    // Lighting Systems
    LIGHT,              // General lighting
    DIMMER,             // Dimmable lights
    RGB_LIGHT,          // Color-changing lights
    MOTION_LIGHT,       // Motion-activated lights

    // Climate & Environment Sensors
    TEMPERATURE_SENSOR,         // Temperature measurement
    HUMIDITY_SENSOR,           // Humidity measurement
    PRESSURE_SENSOR,           // Air pressure measurement
    CO2_SENSOR,               // Carbon dioxide levels
    AIR_QUALITY_SENSOR,       // Air quality measurement
    UV_SENSOR,                // UV radiation measurement
    LIGHT_SENSOR,             // Ambient light measurement
    RAIN_SENSOR,              // Rain detection
    WIND_SENSOR,              // Wind speed/direction

    // Climate Control
    AC,                 // Air conditioning
    HEATER,             // Heating system
    THERMOSTAT,         // Temperature control
    FAN,                // Fans
    HUMIDIFIER,         // Air humidifier
    DEHUMIDIFIER,       // Air dehumidifier
    AIR_PURIFIER,       // Air purification system

    // Security & Access
    DOOR_LOCK,          // Smart door locks
    WINDOW_LOCK,        // Window security
    GARAGE_DOOR,        // Garage door control
    GATE,               // Gate control
    SECURITY_CAMERA,    // Security cameras
    DOORBELL,           // Smart doorbell
    MOTION_SENSOR,      // Motion detection

    // Appliances
    REFRIGERATOR,       // Smart fridge
    DISHWASHER,         // Dishwasher
    WASHING_MACHINE,    // Washing machine
    DRYER,              // Clothes dryer
    OVEN,               // Smart oven

    // Window & Blinds
    CURTAIN,            // Smart curtains
    BLIND,              // Smart blinds
    WINDOW,             // Smart windows

    // Entertainment
    TV,                 // Television
    SPEAKER,            // Smart speakers
    MEDIA_PLAYER,       // Media systems

    // Other Systems
    IRRIGATION,         // Garden irrigation system
    POOL_SYSTEM,        // Swimming pool controls
    VACUUM_ROBOT,       // Robot vacuum cleaner
    ENERGY_METER,       // Energy consumption monitoring
    WATER_LEAK_SENSOR,  // Water leak detection
    SMOKE_DETECTOR,     // Smoke detection
    GAS_DETECTOR,       // Gas leak detection

    // Power Management
    SMART_PLUG,         // Smart power outlets
    POWER_STRIP,        // Smart power strips
    SOLAR_PANEL,        // Solar panel system
    BATTERY_SYSTEM,     // Energy storage system

    // Weather Station Components
    WEATHER_STATION,    // Complete weather station
    BAROMETER,          // Atmospheric pressure measurement
    ANEMOMETER,         // Wind speed measurement
    RAIN_GAUGE,         // Rainfall measurement

    // Water Management
    WATER_METER,        // Water consumption monitoring
    WATER_QUALITY_SENSOR, // Water quality measurement
    WATER_TEMPERATURE_SENSOR, // Water temperature measurement

    // Soil Monitoring
    SOIL_MOISTURE_SENSOR,    // Soil moisture measurement
    SOIL_PH_SENSOR,         // Soil pH measurement
    SOIL_TEMPERATURE_SENSOR  // Soil temperature measurement
}