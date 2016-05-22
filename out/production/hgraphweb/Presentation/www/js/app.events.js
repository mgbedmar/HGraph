(function(){
    'use strict';

    if (typeof app === 'undefined' || typeof app.graph === 'undefined')
        throw 'Error de dependenciaes';

    //Private
    function _hide(selector, cb){
        document.getElementById(selector).classList.remove("show");
        setTimeout(function(){
            document.getElementById(selector).classList.remove("active");
            if(cb) cb();
        }, app.const.transitionDelay);
    }

    function _show(selector){
        document.getElementById(selector).classList.add("active");
        setTimeout(function(){
            document.getElementById(selector).classList.add("show");
        }, 1);
    }

    //Aixo probablement hauria de ser una funcio init dins de app.graph
    function _initMain(cb){

        var large = false;
        //If a graph is selected
        if(app.HGraph.isProjectSelected())
        {
            //Let's see how big it is
            var authorNodes = app.HGraph.getNodesOfType(app.const.nodeTypes.author);
            var termNodes = app.HGraph.getNodesOfType(app.const.nodeTypes.term);
            var paperNodes = app.HGraph.getNodesOfType(app.const.nodeTypes.paper);
            var confNodes = app.HGraph.getNodesOfType(app.const.nodeTypes.conf);
            var size = authorNodes.size() + termNodes.size()+ confNodes.size()+
                paperNodes.size();
            //Is it larger than maxNodes?
            large = (size >= app.settings.maxNodes);

        }
        app.graph.init(large);
        if(!app.graph.isLarge())
        {
            app.events.completeGraph(cb);
        }
        else
        {
            //TODO: disable features for small graphs
            app.events.queryType("author", cb);
        }

    }
    function _initLoadPage(){
        var myList = document.getElementById('projectList');
        myList.innerHTML = '';
        var projects = app.HGraph.getProjects();
        projects.forEach(function(e){
            var child = document.createElement("li");
            /* Crea una estructura del tipus
            <li><div>text</div><div><i class="icon ion-close-round"></i></div></li> */
            var div1, div2, ic;
            div1 = document.createElement("div");
            div1.innerHTML = e;
            div2 = document.createElement("div");
            ic = document.createElement("i");
            ic.className = "icon ion-close-round";
            div2.appendChild(ic);
            div2.addEventListener("click", function() {
                //TODO show popup and delete
                app.events.showAccept("Esborrar un projecte", "El projecte '"+e+"' s'esborrarà, vols continuar?",
                    "Esborra", "Cancela", function(){
                        app.HGraph.deleteProject(e);
                    });
                event.stopPropagation(); //per no executar el anar a graf
            });
            child.appendChild(div1);
            child.appendChild(div2);
            child.addEventListener("click", function() {
                app.HGraph.loadProject(e);
                app.events.loadGoToMain();
            });
            //TODO que vagi a la pagina correcta
            myList.appendChild(child);
        });
    }


    //Public
    app.events = {};

    app.events.init = function(){
        _show(app.const.pageIds.welcome);
    };

    app.events.loadGoToWelcome = function(){
        _hide(app.const.pageIds.loadGraph, function(){
            _show(app.const.pageIds.welcome);
        });
    };

    app.events.mainGoToWelcome = function(){
        _hide(app.const.pageIds.main, function(){
            _show(app.const.pageIds.welcome);
        });
    };

    app.events.loadGoToMain = function() {
        app.events.showLoading();
        _hide(app.const.pageIds.loadGraph, function(){
            _show(app.const.pageIds.main);
            _initMain(function(){
                app.events.hidePopup();
            });
        });
    };

    app.events.welcomeGoToNewGraph = function(){
        app.events.showLoading();
        _hide(app.const.pageIds.welcome, function(){
            _show(app.const.pageIds.main);
            _initMain(function(){
                app.events.hidePopup();
            });
        });
    };

    app.events.welcomeGoToloadGraph = function(){
        _hide(app.const.pageIds.welcome, function(){
            _initLoadPage();
            _show(app.const.pageIds.loadGraph);
        });
    };

    app.events.loadGoToWelcome = function(){
        _hide(app.const.pageIds.loadGraph, function(){
            _show(app.const.pageIds.welcome);
        });
    };

    app.events.showPopup = function(element, cb){
        document.getElementById(app.const.pageIds.popupContent).innerHTML = "";
        document.getElementById(app.const.pageIds.popupContent).appendChild(element);
        _show(app.const.pageIds.popup);
        if(cb) cb();
    };

    app.events.showLoading = function(){
        var div = document.createElement("div");
        div.classList.add("loading");
        var img = document.createElement("img");
        img.src="img/loading.gif";
        div.appendChild(img);
        app.events.showPopup(div);
    };

    app.events.showInfo = function(title, msg, btnMsg, cb){
        var div = document.createElement("div");
        div.classList.add("info");
        var title = document.createElement("h1");
        title.innerHTML = title || "Informació";
        var text = document.createElement("span");
        text.innerHTML = msg;
        var okbtn = document.createElement("a");
        okbtn.innerHTML = btnMsg;
        okbtn.addEventListener("click", function(){
            app.events.hidePopup();
            if(cb) cb();
        });
        div.appendChild(title);
        div.appendChild(text);
        div.appendChild(okbtn);
        app.events.showPopup(div);
    };

    app.events.showAccept = function(t, msg, btnMsgOk, btnMsgCancel, cbOk, cbCancel){
        var div = document.createElement("div");
        div.classList.add("accept");
        var title = document.createElement("h1");
        title.innerHTML = t || "Informació";
        var text = document.createElement("span");
        text.innerHTML = msg;
        var okbtn = document.createElement("a");
        okbtn.innerHTML = btnMsgOk;
        okbtn.addEventListener("click", function(){
            app.events.hidePopup();
            if(cbOk) cbOk();
        });
        var cancelbtn = document.createElement("a");
        cancelbtn.innerHTML = btnMsgCancel;
        cancelbtn.addEventListener("click", function(){
            app.events.hidePopup();
            if(cbCancel) cbCancel();
        });
        div.appendChild(title);
        div.appendChild(text);
        div.appendChild(okbtn);
        div.appendChild(cancelbtn);
        app.events.showPopup(div);
    };

    app.events.hidePopup = function(){
        _hide(app.const.pageIds.popup);
    };

    app.events.editProjects = function() { //TODO transicio
        var tr = document.querySelectorAll("#loadGraphPage .ion-close-round");

        for (var i = 0; i < tr.length; i++) {
            tr[i].classList.toggle("show");
        }
    };

    app.events.openQueryMenu = function(){
        document.querySelector("#"+app.const.pageIds.main + " #queryMenu").classList.toggle("open");
    };

    //TODO
    app.events.completeGraph = function(){
        app.graph.drawGraph();
    };

    app.events.queryType = function(type, cb){
        app.events.showLoading();
        var nodes = app.HGraph.getNodesOfType(type);
        app.graph.drawNodesOnlyGraph(nodes, function(){
            cb();
        });
    };

    //TODO
    app.events.queryNeighbours = function(nodeid){
        app.events.showLoading();
        var nodes = app.HGraph.getNodesOfType(type);
        app.graph.drawNodesOnlyGraph(nodes, function(){
            cb();
        });
    };


}).call(window);














