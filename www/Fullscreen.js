//  cordova-plugin-fullscreen
//  Copyright © 2016 filfat Studios AB
//  Repo: https://github.com/filfat-Studios-AB/cordova-plugin-fullscreen
var exec = require('cordova/exec');

cordova.commandProxy.add("SpinnerDialog", {
    /* On */
    on: function(successCallback, errorCallback) {
        var view = Windows.UI.ViewManagement.ApplicationView.getForCurrentView();
        view.tryEnterFullScreenMode();
        successCallback();
    },
    
    /* Off */
    off: function(successCallback, errorCallback) {
        var view = Windows.UI.ViewManagement.ApplicationView.getForCurrentView();
        view.tryEnterFullScreenMode();
        successCallback();
    },
});
