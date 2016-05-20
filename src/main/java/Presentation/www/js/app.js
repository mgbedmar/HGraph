
var app = {};
//Init HGraph
(function(){
    'use strict';
    if(typeof window.HGraph === 'undefined')
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
            log: function(msg){
                console.log(msg);
            }
        };
    }
    else 
        app.HGraph = window.HGraph;
    
    app.init = function(){
        if(app.nav === undefined || app.graph === undefined){
            app.HGraph.log("Error a les dependencies");
        }

        document.querySelector("#welcomePage .buttons a[data-action=newGraph]")
            .addEventListener("click", app.nav.welcomeGoToNewGraph);
        document.querySelector("#welcomePage .buttons a[data-action=loadGraph]")
            .addEventListener("click", app.nav.welcomeGoToloadGraph);
        document.querySelector("#loadGraphPage a[data-action=welcome]")
            .addEventListener("click", app.nav.loadGoToWelcome);
        //document.querySelector("#mainPage a[data-action=welcome]").addEventListener("click", mainGoToWelcome);

        setTimeout(function(){
            app.nav.init();
        }, 500);
    };
}).call(window);




document.addEventListener("DOMContentLoaded", function(){
    app.init();
});


