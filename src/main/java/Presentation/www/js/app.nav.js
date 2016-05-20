(function(){
    'use strict';

    if (typeof app === 'undefined' || typeof app.graph === 'undefined')
        throw 'Error de dependenciaes';

    //Private
    var _pageIds = {
        welcome: "welcomePage",
        loadGraph: "loadGraphPage",
        main: "mainPage",
        popup: "popupPage",
        popupContent: "popupContent"
    };
    function _hide(selector, cb){
        document.getElementById(selector).classList.remove("show");
        setTimeout(function(){
            document.getElementById(selector).classList.remove("active");
            if(cb) cb();
        }, 200);
    }

    function _show(selector){
        document.getElementById(selector).classList.add("active");
        setTimeout(function(){
            document.getElementById(selector).classList.add("show");
        }, 1);
    }

    function _initLoadPage(){
        var myList = document.getElementById('projectList');
        myList.innerHTML = '';
        var projects = app.HGraph.getProjects();
        projects.forEach(function(e){
            var child = document.createElement("li");
            child.innerHTML = e;
            child.addEventListener("click", function() {
                app.HGraph.loadProject(e);
                app.nav.loadGoToMain();
            });
            //TODO que vagi a la pagina correcta
            myList.appendChild(child);
        });
    }

    //Public
    app.nav = {};

    app.nav.init = function(){
        _show(_pageIds.welcome);
    };

    app.nav.loadGoToWelcome = function(){
        _hide(_pageIds.loadGraph, function(){
            _show(_pageIds.welcome);
        });
    };

    app.nav.mainGoToWelcome = function(){
        _hide(_pageIds.main, function(){
            _show(_pageIds.welcome);
        });
    };

    app.nav.loadGoToMain = function() {
        app.nav.showLoading();
        _hide(_pageIds.loadGraph, function(){
            _show(_pageIds.main);
            app.graph.drawGraph(function(){
                app.nav.hidePopup();
            });
        });
    };

    app.nav.welcomeGoToNewGraph = function(){
        app.nav.showLoading();
        _hide(_pageIds.welcome, function(){
            _show(_pageIds.main);
            app.graph.drawGraph(function(){
                app.nav.hidePopup();
            });
        });
    };

    app.nav.welcomeGoToloadGraph = function(){
        _hide(_pageIds.welcome, function(){
            _initLoadPage();
            _show(_pageIds.loadGraph);
        });
    };

    app.nav.loadGoToWelcome = function(){
        _hide(_pageIds.loadGraph, function(){
            _show(_pageIds.welcome);
        });
    };

    app.nav.showPopup = function(element, cb){
        document.getElementById(_pageIds.popupContent).innerHTML = "";
        document.getElementById(_pageIds.popupContent).appendChild(element);
        _show(_pageIds.popup);
        if(cb) cb();
    };

    app.nav.showLoading = function(){
        var div = document.createElement(div);
        div.innerHTML = "Loading...";
        app.nav.showPopup(div);
    };

    app.nav.hidePopup = function(){
        _hide(_pageIds.popup);
    };


}).call(window);














