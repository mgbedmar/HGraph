(function(){
    'use strict';

    if (typeof app === 'undefined' || typeof app.graph === 'undefined')
        throw 'Error de dependencies';

    //Private
    var _autocompletes = [];
    var _popupShown = false;
    var _inputChoices = [];
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

    function _getNames(arrayList, type){
        var names = [];
        for(var i = 0; i < arrayList.size(); i++)
        {
            names.push([String(arrayList.get(i)[1]), String(arrayList.get(i)[0]), type]);
        }
        return names;
    }

    function _initAutoCompletes(nodes){
        var mChars;
        if (nodes.length < 300) mChars = 1;
        else mChars = 3;

        //For each type in nodes
        for (var key in app.const.autoInputIds) {

            //Check if type is a property of nodes
            if (app.const.autoInputIds.hasOwnProperty(key)) {

                _autocompletes.push(new autoComplete({
                    //TODO: specify inputs..
                    selector: "#"+app.const.autoInputIds[key],
                    minChars: mChars,
                    source: function(term, suggest){
                        term = term.toLowerCase();
                        var choices = nodes;
                        var matches = [];
                        for (var i=0; i<choices.length; i++)
                            if (~(choices[i][0]+' '+choices[i][1]).toLowerCase().indexOf(term)) matches.push(choices[i]);
                        suggest(matches);
                    },
                    renderItem: function (item, search){
                        search = search.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
                        var re = new RegExp("(" + search.split(' ').join('|') + ")", "gi");
                        return '<div class="autocomplete-suggestion" data-nom="'+item[0]+'" data-iden="'+item[1]+'" data-tipus="'+item[2]+
                                '" data-val="'+item[0]+'"> ' + item[0].replace(re, "<b>$1</b>") + '</div>';
                    },
                    onSelect: function(e, term, item){
                        var ob = [item.getAttribute('data-nom'), item.getAttribute('data-iden'), item.getAttribute('data-tipus')];
                        _inputChoices.push(ob);
                    }
                }));
            }
        }


    }

    function _initMain(){

        var large = false;
        var nodes=[];
        //If a graph is selected
        if(app.HGraph.isProjectSelected())
        {
            //Let's see how big it is
            var authorNodes = _getNames(app.HGraph.getNodesOfType(app.const.nodeTypes.author), app.const.nodeTypes.author);
            var termNodes = _getNames(app.HGraph.getNodesOfType(app.const.nodeTypes.term), app.const.nodeTypes.term);
            var paperNodes = _getNames(app.HGraph.getNodesOfType(app.const.nodeTypes.paper), app.const.nodeTypes.paper);
            var confNodes = _getNames(app.HGraph.getNodesOfType(app.const.nodeTypes.conf), app.const.nodeTypes.conf);
            nodes = authorNodes.concat(termNodes.concat(paperNodes.concat(confNodes)));
            //Is it larger than maxNodes?
            large = (nodes.length >= app.settings.maxNodes);

        }
        app.graph.init(large);
        if(!app.graph.isLarge())
        {
            document.querySelector("#queryMenu li[data-action=completeGraph]").click();
        }
        else
        {
            //TODO: disable small graphs features
            //_disableSmallGraphFeatures(); //TODO la comento perque no esta definida
            //Trigger draw queryType author
            //document.querySelector("#queryMenu li[data-action=queryType] typeSelector[data-type=author]").click();
            //TODO posar la linia de dalt quan estiguin els listeners corresponents
            _drawQueryType("author");
        }

        //Init autocompletes
        _initAutoCompletes(nodes);

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
                app.events.showAccept("Esborrar un projecte", "El projecte '"+e+"' s'esborrarà, vols continuar?",
                    "Esborra", function(){
                        app.HGraph.deleteProject(e);
                        _initLoadPage();
                    }, "Cancela");
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

    //QueryMenu functions
    function _drawCompleteGraph(){
        var nodeobj = {};
        nodeobj[app.const.nodeTypes.author] = app.HGraph.getNodesOfType(app.const.nodeTypes.author);
        nodeobj[app.const.nodeTypes.term]  = app.HGraph.getNodesOfType(app.const.nodeTypes.term);
        nodeobj[app.const.nodeTypes.paper]  = app.HGraph.getNodesOfType(app.const.nodeTypes.paper);
        nodeobj[app.const.nodeTypes.conf]  = app.HGraph.getNodesOfType(app.const.nodeTypes.conf);
        var edgeobj = {};
        edgeobj[app.const.nodeTypes.term]  = app.HGraph.getEdgesOfType(app.const.nodeTypes.term);
        edgeobj[app.const.nodeTypes.author]  = app.HGraph.getEdgesOfType(app.const.nodeTypes.author);
        edgeobj[app.const.nodeTypes.conf]  = app.HGraph.getEdgesOfType(app.const.nodeTypes.conf);
        app.graph.drawGraph(nodeobj, edgeobj);
    }

    function _drawQueryType (type){
        var nodes = app.HGraph.getNodesOfType(type);
        app.graph.drawNodesOnlyGraph(nodes);
    }

    function _selectMenuOption(element,menu){
        var nodes = document.querySelectorAll(menu +"> div > ul > li");
        for(var i = 0; i < nodes.length; i++)
        {
            if (nodes[i] != element.parentNode) {
                nodes[i].classList.remove("selected");
            }
        }

        element.parentNode.classList.toggle("selected");
        _inputChoices=[]; //reset
    }

    function _selectType(parentElement, type){
        var nodes = parentElement.children;
        for(var i = 0; i < nodes.length; i++)
        {
            if(nodes[i].dataset.type == type)
                nodes[i].classList.add("selected");
            else
                nodes[i].classList.remove("selected");
        };

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
            _initMain();
            app.events.hidePopup();
        });
    };

    app.events.welcomeGoToNewGraph = function(){
        app.events.showLoading();
        _hide(app.const.pageIds.welcome, function(){
            _show(app.const.pageIds.main);
            _initMain();
            app.events.hidePopup();
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
    //----Popups
    app.events.showPopup = function(element){
        _popupShown = true;
        document.getElementById(app.const.pageIds.popupContent).innerHTML = "";
        document.getElementById(app.const.pageIds.popupContent).appendChild(element);
        _show(app.const.pageIds.popup);
    };

    app.events.showLoading = function(){
        if(_popupShown) return;
        var div = document.createElement("div");
        div.classList.add("loading");
        div.classList.add("with-border");
        var img = document.createElement("img");
        img.src="img/loading.gif";
        div.appendChild(img);
        app.events.showPopup(div);
    };

    app.events.showInfo = function(title, msg, btnMsg, cb){
        app.HGraph.log("info");
        app.events.showAccept(title, msg, btnMsg, cb);
    };

    app.events.showAccept = function(t, msg, btnMsgOk, cbOk, btnMsgCancel, cbCancel){
        if(_popupShown) return;
        var div = document.createElement("div");
        div.classList.add("accept");
        div.classList.add("with-border");
        var title = document.createElement("h1");
        title.innerHTML = t || "Informació";
        var text = document.createElement("span");
        text.innerHTML = msg;
        if (msg.length > 120) {
            div.classList.add("large");
        }
        else if (msg.length > 80) {
            div.classList.add("big");
        }
        var divbtns = document.createElement("div");
        divbtns.classList.add("divbtns");
        var okbtn = document.createElement("a");
        okbtn.innerHTML = btnMsgOk;
        okbtn.addEventListener("click", function(){
            app.events.hidePopup();
            if(cbOk) cbOk();
        });
        if (typeof btnMsgCancel != 'undefined') {
            app.HGraph.log(typeof btnMsgCancel);
            var cancelbtn = document.createElement("a");
            cancelbtn.innerHTML = btnMsgCancel;
            cancelbtn.addEventListener("click", function(){
                app.events.hidePopup();
                if(cbCancel) cbCancel();
            });
            divbtns.appendChild(cancelbtn);
        }
        divbtns.appendChild(okbtn);
        div.appendChild(title);
        div.appendChild(text);
        div.appendChild(divbtns);
        app.events.showPopup(div);
    };

    app.events.hidePopup = function(){
        app.HGraph.log("hidepopup");
        _popupShown = false;
        _hide(app.const.pageIds.popup);
    };
    //----/popups

    app.events.editProjects = function() { //TODO transicio
        var tr = document.querySelectorAll("#loadGraphPage .ion-close-round");

        for (var i = 0; i < tr.length; i++) {
            tr[i].classList.toggle("show");
        }
    };

    //----Query menu
    app.events.openQueryMenu = function(){
        document.querySelector("#"+app.const.pageIds.main + " #queryMenu").classList.toggle("open");
    };
    app.events.selectQueryMenuOption = function(e){
        _selectMenuOption(e.currentTarget, "#queryMenu");
    };
    app.events.selectTypeOption = function(e){
        _selectType(e.target.parentNode, e.currentTarget.dataset.type);
    };
    app.events.drawCompleteGraph = function(){
        app.events.showLoading();
        _drawCompleteGraph();
        app.events.hidePopup();
    };
    //e.target is the type button pressed
    app.events.changeQueryType = function(e){

        _drawQueryType(e.currentTarget.dataset.type);
    };

    //TODO
    app.events.queryNeighbours = function(nodeid){
        app.events.showLoading();
        var nodes = app.HGraph.getNodesOfType(type);
        app.graph.drawNodesOnlyGraph(nodes);
    };
    //----/QueryMenu

    app.events.query1to1 = function() {
        if (_inputChoices.length === 2) {
            //TODO: el resultat el coloca java
            app.HGraph.query1to1(_inputChoices[0][1], _inputChoices[0][2]+"23",
                                             _inputChoices[1][1], _inputChoices[1][2]);
            app.events.showLoading();
/*
            hm = String(hm);
            var result = [];
            var c = {source: _inputChoices[0][0],
                     target:  _inputChoices[1][0],
                     hetesim:hm
            };

            result.push(c);
            app.events.showLoading();
            app.graph.drawQuery1to1(result);
            _inputChoices = [];
            app.events.hidePopup();
            */
        }
        else {
            _inputChoices = [];
            app.events.showInfo("mira...", "no ha anat be", "ok");

        }
    };

    app.events.takeQuery1To1Result = function() {
        var hm = String(app.HGraph.getQuery1To1Result());
        var result = [];
        var c = {source: _inputChoices[0][0],
                 target:  _inputChoices[1][0],
                 hetesim:hm
        };

        result.push(c);
        app.graph.drawQuery1to1(result);
        _inputChoices = [];
        app.events.hidePopup();

    }

    app.events.openToolsMenu = function(){
            document.querySelector("#"+app.const.pageIds.main + " #toolsMenu").classList.toggle("open");
    };

    app.events.selectToolsMenuOption = function(e){
            _selectMenuOption(e.currentTarget, "#toolsMenu");
    };

}).call(window);














