
var app = app || {};

//Init HGraph
(function(){
    'use strict';
    //Public
    app.settings={
        //Disables some features on graphs with nodes > maxNodes (large graphs)
        maxNodes:1500
    };
    app.const  = {
        pageIds : {
            welcome: "welcomePage",
            loadGraph: "loadGraphPage",
            main: "mainPage",
            popup: "popupPage",
            popupContent: "popupContent"
        },
        autoInputIds:{
            veins: "autoVeins",
            ref1: "autoref1",
            ref2: "autoref2",
            oToN: "auto1ToN"
        },
        nodeTypes:{
            author: "author",
            conf: "conf",
            paper: "paper",
            term: "term"
        },
        //Adds a delay before showing the main screen
        dramaticWait: 500,
        //Page transition duration
        transitionDelay: 200
    };


    //Init app (Ugly race condition)
    setTimeout(function(){
        if(typeof app.HGraph === 'undefined')
        {
            app.HGraph = {
                getProjects: function(){
                    return ["stub1", "stub2", "sutb3"]
                },
                getNodesOfType: function(type){
                    return {
                        size: function(){ return 200 },
                        get: function(i){ return [type+i, "Soc el "+type+i] }
                    };
                },
                getEdgesOfType: function(type){
                    return {
                        size: function(){ return 10 },
                        get: function(i){ return [type+i, "Soc el "+type+i] }
                    };
                },
                loadProject: function(project){
                    return;
                },
                isProjectSelected: function(){
                    return !document.querySelector("#welcomePage").classList.contains("active");
                },
                log: function(msg){
                    console.log(msg);
                }
            };
        }

    },1);

    function _addMultiEvent(selector, event, fn){
        var elements = document.querySelectorAll(selector);
        for(var i = 0; i < elements.length; i++)
        {
            elements[i].addEventListener(event, fn);
        }
    }



    app.debounce = function(fn, delay){
        var timer = null;
        return function () {
            var context = this, args = arguments;
            clearTimeout(timer);
            timer = setTimeout(function () {
                fn.apply(context, args);
            }, delay);
        };
    };

    app.init = function(){
        if(app.events === undefined || app.graph === undefined){
            app.HGraph.log("Error a les dependencies");
        }

        document.querySelector("#welcomePage .buttons a[data-action=newGraph]")
            .addEventListener("click", app.events.welcomeGoToNewGraph);
        document.querySelector("#welcomePage .buttons a[data-action=loadGraph]")
            .addEventListener("click", app.events.welcomeGoToloadGraph);
        document.querySelector("#tornardiv")
            .addEventListener("click", app.events.loadGoToWelcome);
        document.getElementById("divEditPL")
            .addEventListener("click", app.events.editProjects);

        document.querySelector("#mainPage #queryMenu > div[data-action=openQueryMenu]")
            .addEventListener("click", app.events.openQueryMenu);
        _addMultiEvent("#mainPage #queryMenu > div > ul > li > h1", "click", app.events.selectQueryMenuOption);
        _addMultiEvent("#mainPage #toolsMenu > div > ul > li > h1", "click", app.events.selectToolsMenuOption);
        _addMultiEvent("#mainPage ul.typeSelector > li", "click", app.events.selectTypeOption);
        document.querySelector("#mainPage #queryMenu > div > ul > li[data-action=completeGraph]")
            .addEventListener("click", app.events.drawCompleteGraph);


        document.querySelector("#mainPage #toolsMenu > div[data-action=openToolsMenu]")
            .addEventListener("click", app.events.openToolsMenu);
        //document.querySelector("#mainPage a[data-action=welcome]").addEventListener("click", mainGoToWelcome);

        setTimeout(function(){
            app.events.init();
        }, app.const.dramaticWait);
    };


}).call(window);




document.addEventListener("DOMContentLoaded", function(){
    app.init();
});



