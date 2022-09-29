package com.mintrocket.data.feature_toggling.features.toggles

enum class LocalFeatureToggle(val key: String, val title: String) {
    REMOTE_CONFIG(
        "remote_config_feature",
        "Remote Config Feature Toggle"
    ),
    IN_APP_UPDATE(
        "android_feature_in_app_update",
        "In app update feature"
    ),
    IN_APP_UPDATE_INSTALL_IMMEDIATE(
        "android_feature_in_app_update_install_immediate",
        "In app update install immediate"
    ),
    IMAGE_ONE(
        "android_feature_image_one",
        "Image one feature"
    ),
    SHOW_BUTTON(
        "android_feature_show_button",
        "Show button feature"
    )
}