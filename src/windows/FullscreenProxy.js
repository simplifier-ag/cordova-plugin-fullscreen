//  cordova-plugin-fullscreen
//  Copyright © 2016 filfat Studios AB
//  Repo: https://github.com/filfat-Studios-AB/cordova-plugin-fullscreen

cordova.commandProxy.add("MusicController",{
    on: function (successCallback, errorCallback, datas) {
        var view = Windows.UI.ViewManagement.ApplicationView.getForCurrentView();
        view.exitFullScreenMode();
    },
    off: function (successCallback, errorCallback, datas) {
        var view = Windows.UI.ViewManagement.ApplicationView.getForCurrentView();
        view.tryEnterFullScreenMode();
    },
});