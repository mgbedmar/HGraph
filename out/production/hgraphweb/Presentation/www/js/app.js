
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
                log: function(msg){
                    console.log(msg);
                }
            };
        }

    },1);




    app.init = function(){
        if(app.nav === undefined || app.graph === undefined){
            app.HGraph.log("Error a les dependencies");
        }

        document.querySelector("#welcomePage .buttons a[data-action=newGraph]")
            .addEventListener("click", app.nav.welcomeGoToNewGraph);
        document.querySelector("#welcomePage .buttons a[data-action=loadGraph]")
            .addEventListener("click", app.nav.welcomeGoToloadGraph);
        document.querySelector("#tornardiv")
            .addEventListener("click", app.nav.loadGoToWelcome);
        document.getElementById("divEditPL")
            .addEventListener("click", _editProjects);

        document.querySelector("#mainPage #queryMenu > div[data-action=openQueryMenu]")
            .addEventListener("click", app.nav.openQueryMenu);
        //document.querySelector("#mainPage a[data-action=welcome]").addEventListener("click", mainGoToWelcome);

        setTimeout(function(){
            app.nav.init();
        }, 500);
    };


}).call(window);




document.addEventListener("DOMContentLoaded", function(){
    app.init();
});


_editProjects = function() {
        var tr = document.querySelectorAll("#loadGraphPage .ion-close-round");

        for (var i = 0; i < tr.length; i++) {
            setTimeout(tr[i].classList.toggle("active"), 1000);
            tr[i].classList.toggle("active");
            tr[i].classList.toggle("show");

        }
    };
