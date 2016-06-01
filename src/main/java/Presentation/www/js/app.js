
var app = app || {};

//Init HGraph
(function(){
    'use strict';
    //private

    //Public
    app.modified = false;
    app.newProject = true;
    app.settings={
        maxNodes:3000,
        maxRecNodes:5000,
        maxEdges:4000,
        maxRecEdges:7000,
        maxRows: 100,
        maxRecRows:300,
        hideEdges: false,
        labelThreshold: 10,
        marginRatio:{
            "author": 0.5,
            "paper": 0.1,
            "term": 0.5,
            "conf": 0.8
        }
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
            edge1: "autoedge1",
            edge2: "autoedge2",
            node: "autonode",
            remedge1: "autofont",
            remedge2: "autodesti",
            veins: "autoVeins",
            ref1: "autoref1",
            ref2: "autoref2",
            ref3: "autoref3",
            oToN: "auto1ToN",
            oToo: "auto1To1-1",
            oToo2: "auto1To1-2",
            select: "autoSelect",
            filter: "autoFilterNames"
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
        transitionDelay: 200,

        defaultSize: 5
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
                        size: function(){ return 2000 },
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
                addNode: function(label, type){
                    return Date.now();
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

    function _addInfoEvent() {
        document.getElementById("info1a1").addEventListener("click", function() { app.events.showInfo(
            "Consulta 1 a 1", "Introdueix dos nodes per obtenir la seva mesura de HeteSim.", "OK")});
        document.getElementById("info1aN").addEventListener("click", function() { app.events.showInfo(
            "Consulta 1 a N", "Introdueix un node i un tipus per obtenir un llistat de les relacions del "+
            "node amb el tipus.", "OK")});
        document.getElementById("infoNaN").addEventListener("click", function() { app.events.showInfo(
            "Consulta N a N", "Introdueix dos tipus per obtenir una taula amb les seves relacions.", "OK")});
        document.getElementById("inforef").addEventListener("click", function() { app.events.showInfo(
            "Consulta amb referència", "Introdueix una parella de nodes de referència i un tercer node (del mateix "+
            "tipus que el primer) per obtenir un llistat de nodes que siguin tan mútuament rellevants amb el tercer "+
            "com ho són els dos primers entre sí.", "OK")});

    }

    function _initShortcuts(){

        document.addEventListener("keypress", function(e){
            if(e.ctrlKey && e.keyCode == 18) //ctrl+R
                app.events.reload();
            if(e.ctrlKey && e.keyCode == 19) //ctrl+S
                app.events.save();
            if(e.ctrlKey && e.shiftKey && e.keyCode == 83) //ctrl+shift+S
                app.events.saveAs();
            if(e.ctrlKey && e.keyCode == 13) //ctrl+M
                app.events.showHelp();
            if(e.ctrlKey && e.keyCode == 15) //ctrl+O
                app.events.showSettings();
            if(e.ctrlKey && e.keyCode == 17) //ctrl+Q
                app.events.openQueryMenu();
            if(e.ctrlKey && e.keyCode == 20) //ctrl+T
                app.events.openToolsMenu();

        });
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
        document.getElementById("divImportPL")
            .addEventListener("click", app.events.showImportProject);
        document.getElementById("divEditPL")
            .addEventListener("click", app.events.editProjects);

        document.querySelector("#mainPage #queryMenu > div[data-action=openQueryMenu]")
            .addEventListener("click", app.events.openQueryMenu);
        _addMultiEvent("#mainPage #queryMenu > div > ul > li > h1", "click", app.events.selectQueryMenuOption);
        _addMultiEvent("#mainPage #toolsMenu > div > ul > li > h1", "click", app.events.selectToolsMenuOption);
        _addMultiEvent("#mainPage ul.typeSelector > li", "click", app.events.selectTypeOption);
        document.querySelector("#mainPage #queryMenu > div > ul > li[data-action=completeGraph] ul[data-action=filterEdges]")
            .addEventListener("click", app.events.filterEdges);
        document.getElementById("seeTipus").addEventListener("click", app.events.queryType);
        document.getElementById("seeVeins").addEventListener("click", app.events.queryNeighbours);
        _addInfoEvent();
        document.getElementById("ok1to1").addEventListener("click", app.events.query1to1);
        document.getElementById("ok1toN").addEventListener("click", app.events.query1toN);
        document.getElementById("okNtoN").addEventListener("click", app.events.queryNtoN);
        document.getElementById("okref").addEventListener("click", app.events.queryByReference);
        document.getElementById("tableTipus").addEventListener("click", app.events.queryByTypeTable);
        document.getElementById("tableVeins").addEventListener("click", app.events.queryNeighboursTable);

        document.querySelector("#mainPage #toolsMenu #addNode")
            .addEventListener("click", app.events.addNode);
        document.querySelector("#mainPage #toolsMenu #addEdge")
            .addEventListener("click", app.events.addEdge);
        document.querySelector("#mainPage #toolsMenu #removeNode")
            .addEventListener("click", app.events.removeNode);
        document.querySelector("#mainPage #toolsMenu #removeEdge")
            .addEventListener("click", app.events.removeEdge);
        document.querySelector("#mainPage #toolsMenu > div[data-action=openToolsMenu]")
            .addEventListener("click", app.events.openToolsMenu);

        document.querySelector("#mainPage #mainMenu div[data-action=openMainMenu]")
            .addEventListener("click", app.events.openMainMenu);
        document.querySelector("#mainPage #mainMenu ul li[data-action=mainToHome]")
            .addEventListener("click", app.events.mainToHome);
        document.querySelector("#mainPage #mainMenu ul li[data-action=save]")
            .addEventListener("click", app.events.save);
        document.querySelector("#mainPage #mainMenu ul li[data-action=saveAS]")
            .addEventListener("click", app.events.saveAs);
        document.querySelector("#mainPage #mainMenu ul li[data-action=showSettings]")
            .addEventListener("click", app.events.showSettings);
        document.querySelector("#mainPage #mainMenu ul li[data-action=showHelp]")
            .addEventListener("click", app.events.showHelp);

        document.getElementById("noEdges").addEventListener("click", app.events.noEdges);
        document.getElementById("allEdges").addEventListener("click", app.events.allEdges);


        _initShortcuts();
        setTimeout(function(){
            app.events.init();
        }, app.const.dramaticWait);
    };


}).call(window);




document.addEventListener("DOMContentLoaded", function(){
    app.init();
});



