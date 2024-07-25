export var NavigationBarPluginEvents;
(function (NavigationBarPluginEvents) {
    /**
     * Called after the navigation bar is displayed
     */
    NavigationBarPluginEvents["SHOW"] = "onShow";
    /**
     * Called after navigation bar is hidden
     */
    NavigationBarPluginEvents["HIDE"] = "onHide";
    /**
     * Called after navigation bar color is changed
     */
    NavigationBarPluginEvents["COLOR_CHANGE"] = "onColorChange";
    /*
     * Called after navigation bar immersive view is changed
     */
    NavigationBarPluginEvents["IMMERSIVE_VIEW_CHANGE"] = "onImmersiveModeChange";
})(NavigationBarPluginEvents || (NavigationBarPluginEvents = {}));
//# sourceMappingURL=navigationbar.events.js.map