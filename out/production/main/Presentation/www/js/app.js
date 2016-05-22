
var app = app || {};

//Init HGraph
(function(){
    'use strict';
    //Ugly race condition
    setTimeout(function(){
        if(typeof app.HGraph === 'undefined')
        {
            app.HGraph = {
                getProjects: function(){
                    return ["stub1", "stub2", "sutb3"]
                },
                getNodesOfType: function(type){
                    if(type == "paper")
                    {
                        return [
                            ["0", "patata"],
                            ["1", "zanahoria"],
                            ["2", "berenjena"],
                            ["3", "tomate"]
                        ];
                    }
                },
                loadProject: function(project){
                    return;
                },
                log: function(msg){
                    console.log(msg);
                }
            };
        }

    },1);


    //Private
    var _largeGraph = false;

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
        //document.querySelector("#mainPage a[data-action=welcome]").addEventListener("click", mainGoToWelcome);

        setTimeout(function(){
            app.events.init();
        }, app.const.dramaticWait);
    };

    app.loadProject = function(project) {
        app.HGraph.loadProject(project);
        var authorNodes = app.HGraph.getNodesOfType(app.const.nodeTypes.author);
        var termNodes = app.HGraph.getNodesOfType(app.const.nodeTypes.term);
        var paperNodes = app.HGraph.getNodesOfType(app.const.nodeTypes.paper);
        var confNodes = app.HGraph.getNodesOfType(app.const.nodeTypes.conf);
        var size = authorNodes.size() + termNodes.size()+ confNodes.size()+
                paperNodes.size();
        if(size >= app.settings.maxNodes)
        {
            app._largeGraph = true;
            var nodeobj = {};
            nodeobj[app.const.nodeTypes.author] =authorNodes;
            app.graph.setGraph(nodeobj);
        }
        else
        {
            var nodeobj = {};
            nodeobj[app.const.nodeTypes.author] =authorNodes;
            nodeobj[app.const.nodeTypes.term] =termNodes;
            nodeobj[app.const.nodeTypes.paper] =paperNodes;
            nodeobj[app.const.nodeTypes.conf] =confNodes;
            var edgeobj = {};
            edgeobj[app.const.nodeTypes.author] =authorNodes;
            edgeobj[app.const.nodeTypes.term] =termNodes;
            edgeobj[app.const.nodeTypes.conf] =confNodes;
            app.graph.setGraph(nodeobj, edgeobj);
        }
    };


}).call(window);




document.addEventListener("DOMContentLoaded", function(){
    app.init();
});



