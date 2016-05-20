(function(){
    'use strict';

    if (typeof app === 'undefined' || typeof app.graph === 'undefined')
        throw 'Error de dependenciaes';

    //Private
    var _pageIds = {
        welcome: "#welcomePage",
        loadGraph: "#loadGraphPage",
        main: "#mainPage",
        loading: "#loading"
    };
    function _hide(selector, cb){
        document.querySelector(selector).classList.remove("show");
        setTimeout(function(){
            document.querySelector(selector).classList.remove("active");
            if(cb) cb();
        }, 200);
    }

    function _show(selector){
        document.querySelector(selector).classList.add("active");
        setTimeout(function(){
            document.querySelector(selector).classList.add("show");
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
        _show(_pageIds.loading);
        _hide(_pageIds.loadGraph, function(){
            _show(_pageIds.main);
            app.graph.drawGraph(function(){
                _hide(_pageIds.loading);
            });
        });
    };

    app.nav.welcomeGoToNewGraph = function(){
        _show(_pageIds.loading);
        _hide(_pageIds.welcome, function(){
            _show(_pageIds.main);
            app.graph.drawGraph(function(){
                _hide(_pageIds.loading);
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


}).call(window);














