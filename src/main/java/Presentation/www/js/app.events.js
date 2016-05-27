(function(){
    'use strict';

    if (typeof app === 'undefined' || typeof app.graph === 'undefined')
        throw 'Error de dependencies';

    //Private
    var _autocompletes = [];
    //Nodes for autocomplete
    //TODO actualitzar al esborrar, afegir. Controlar excepcions.
    var _nodes;
    var _popupShown = false;
    var _inputChoices = {
        source:'',
        target:'',
        ref:''
    };

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

    function _clearInputChoices() {
        _inputChoices.source = '';
        _inputChoices.target = '';
        _inputChoices.ref = '';
    }

    function _checkInputs(idInputSource, idInputTarget, idInputRef) {
        var ok = true;
        if (typeof idInputSource != 'undefined') {
            if (typeof _inputChoices.source === 'undefined' || _inputChoices.source === '') {
                _inputChoices.source = '';
                _attVisibleInput(idInputSource);
                ok = false;
            }
        }
        if (typeof idInputTarget != 'undefined') {
            if (typeof _inputChoices.target === 'undefined' || _inputChoices.target === '') {
                _inputChoices.target = '';
                _attVisibleInput(idInputTarget);
                ok = false;
            }
        }
        if (typeof idInputRef != 'undefined') {
            if (typeof _inputChoices.ref === 'undefined' || _inputChoices.ref === '') {
                _inputChoices.target = '';
                _attVisibleInput(idInputRef);
                ok = false;
            }
        }
        return ok;
    }


    var _attInput = function(inputId) {
        var el = document.getElementById(inputId);
        if (el.getAttribute("data-autoType") === "source") {
            _inputChoices.source = '';
        }
        else if (el.getAttribute("data-autoType") === "target") {
            _inputChoices.target = '';
        }
        else {
            _inputChoices.ref = '';
        }
    };

    function _attVisibleInput(inputId) {
        document.getElementById(inputId).classList.add("wrong");
    }

    function _nonAttInput(selector) {
        var els = document.querySelectorAll("[data-autoType='"+selector+"']");
        for (var i = 0; i < els.length; i++) {
            els[i].classList.remove("wrong");
        }
    }

    function _initAutoCompletes(nodes){
        var mChars;
        if (_nodes.length < 300) mChars = 1;
        else mChars = 3;
        var _renIt = function (item, search){
             search = search.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
             var re = new RegExp("(" + search.split(' ').join('|') + ")", "gi");
             return '<div class="autocomplete-suggestion" data-nom="'+item[0]+'" data-iden="'+item[1]+'" data-tipus="'+item[2]+
                     '" data-val="'+item[0]+'"> ' + item[0].replace(re, "<b>$1</b>") + '</div>';
        };


        //For each type in nodes
        for (var key in app.const.autoInputIds) {

            //Check if type is a property of nodes
            if (app.const.autoInputIds.hasOwnProperty(key)) {
                var element = document.getElementById(app.const.autoInputIds[key]);

                element.addEventListener("input", function(e) { _attInput(e.currentTarget.id)});
                /*document.getElementById(app.const.autoInputIds[key])
                    .addEventListener("input", function() { _attInput(app.const.autoInputIds[key])});*/

                var _autCom = {
                    selector: "#"+app.const.autoInputIds[key],
                    minChars: mChars,
                    source: function(term, suggest){
                        term = term.toLowerCase();
                        var matches = [];
                        for (var i=0; i<_nodes.length; i++)
                            if (~(_nodes[i][0]+' '+_nodes[i][1]).toLowerCase().indexOf(term)) matches.push(_nodes[i]);
                        suggest(matches);
                    },
                    renderItem: _renIt,
                    onSelect: ''
                };

                if (element.getAttribute("data-autoType") === "source") {
                    _autCom.onSelect = function(e, term, item){
                        var nod = {
                            id: item.dataset.iden,
                            name: item.dataset.nom,
                            type: item.dataset.tipus
                        };
                        _inputChoices.source = nod;
                        _nonAttInput("source");
                    }
                    _autocompletes.push(new autoComplete(_autCom));
                }

                else if (element.getAttribute("data-autoType") === "target") {
                    _autCom.onSelect = function(e, term, item){
                        var nod = {
                            id: item.dataset.iden,
                            name: item.dataset.nom,
                            type: item.dataset.tipus
                        };
                        _inputChoices.target = nod;
                        _nonAttInput("target");
                    }
                    _autocompletes.push(new autoComplete(_autCom));
                }

                else if (element.getAttribute("data-autoType") === "ref") {
                    _autCom.onSelect = function(e, term, item){
                        var nod = {
                            id: item.dataset.iden,
                            name: item.dataset.nom,
                            type: item.dataset.tipus
                        };
                        _inputChoices.ref = nod;
                        _nonAttInput("ref");
                    }
                    _autocompletes.push(new autoComplete(_autCom));
                }
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
        _nodes = nodes;
        _initAutoCompletes();

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

    function _selectMenuOption(element, currentMenu, theOtherMenu){
        var nodes = document.querySelectorAll(currentMenu +"> div > ul > li");
        var nodes2 = document.querySelectorAll(theOtherMenu +"> div > ul > li");

        for(var i = 0; i < nodes.length; i++)
        {
            if (nodes[i] != element.parentNode) {
                nodes[i].classList.remove("selected");
            }
        }

        for (var j = 0; j < nodes2.length; j++) {
            nodes2[j].classList.remove("selected");
        }

        element.parentNode.classList.toggle("selected");
        _clearInputChoices();
    }

    function _selectType(parentElement, type){
        parentElement.dataset.selection = type;
        var nodes = parentElement.children;
        for(var i = 0; i < nodes.length; i++)
        {
            if(nodes[i].dataset.type == type)
                nodes[i].classList.add("selected");
            else
                nodes[i].classList.remove("selected");
        };

    }
    function _clearTypeSelector(selector){
        document.querySelector(selector).dataset.selection = "";
        var children = document.querySelector(selector).children;
        for(var i = 0; i < children.length; i++)
        {
            children[i].classList.remove("selected");
        }
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

    app.events.hidePopup = function(cb){

        _hide(app.const.pageIds.popup, function(){
            _popupShown = false;
            if(cb) cb();
        });
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
        _selectMenuOption(e.currentTarget, "#queryMenu", "#toolsMenu");
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

        if (!_checkInputs("auto1To1-1", "auto1To1-2")) return;
        app.HGraph.query1to1(_inputChoices.source.id, _inputChoices.source.type,
                             _inputChoices.target.id, _inputChoices.target.type);
        app.events.showLoading();

    };

    app.events.takeQuery1To1Result = function() {
        var hm = String(app.HGraph.getQuery1To1Result());
        var result = [];
        var c = {source: _inputChoices.source.name,
                 target:  _inputChoices.target.name,
                 hetesim:hm
        };
        app.HGraph.log(hm);
        result.push(c);
        app.graph.drawQuery1to1(result);
        app.events.hidePopup();

    };



    //---Tools menu----
    app.events.openToolsMenu = function(){
            document.querySelector("#"+app.const.pageIds.main + " #toolsMenu").classList.toggle("open");
    };

    app.events.addNode = function(){
        var typeSelector = document.querySelector("#addNodeSection .typeSelector");
        var type =typeSelector.dataset.selection;
        var input = document.querySelector("#addNodeSection input");
        //TODO: nopopups?
        if(!type)
        {
            app.events.showInfo("Informació","Si us plau, selecciona un tipus", "D'acord");
            typeSelector.classList.add("wrong");
            return;
        }
        else
            typeSelector.classList.remove("wrong");

        if(!input.value)
        {
            app.events.showInfo("Informació","Si us plau, escriu un nom", "D'acord");
            input.classList.add("wrong");
            return;
        }
        else
            input.classList.remove("wrong");

        var id = app.HGraph.addNode(input.value, type);
        app.graph.addNode(id, input.value, type);
        _nodes.push([String(input.value),String(id), type]);

        _clearTypeSelector("#addNodeSection .typeSelector");
        input.value = "";
        _clearInputChoices();
    };

    app.events.addEdge = function(){
        var inputSrc = document.querySelector("#autoedge1");
        var inputDest = document.querySelector("#autoedge2");

        if(!_checkInputs("autoedge1", "autoedge2")) return;

        var paperId;
        var destId;
        var destType;
        if(_inputChoices.source.type == "paper"){
            paperId =_inputChoices.source.id;
            destId = _inputChoices.target.id;
            destType = _inputChoices.target.type;
        }
        else{
            paperId =_inputChoices.target.id;
            destId = _inputChoices.source.id;
            destType = _inputChoices.source.type;
        }

        var edgeAdded = app.HGraph.addEdge(_inputChoices.source.id, _inputChoices.source.type,
            _inputChoices.target.id, _inputChoices.target.type);

/*        if(edgeAdded){
            //TODO:Notify

            app.graph.addEdge(destId, destType, paperId);


        }*/

        inputSrc.value = '';
        inputDest.value = '';
        _clearInputChoices();


    };

    app.events.removeNode = function(){
        var input = document.querySelector("#autonode");

        if (!_checkInputs("autonode")) return;

        var nodeRemoved = app.HGraph.removeNode(_inputChoices.source.id, _inputChoices.source.type);
        if(nodeRemoved)
        {
            //TODO: notify
            app.graph.removeNode(_inputChoices.source.id, _inputChoices.source.type);
            var found = false;
            var i;
            for(i = 0; i < _nodes.length && !found; i++)
            {
                found = (_nodes[i][1] == _inputChoices.source.id && _nodes[i][2] == _inputChoices.source.type);
            }
            if(found)
                _nodes.splice(i, 1);
            app.HGraph.log("updated");
        }
        input.value = "";
        _clearInputChoices();
    };

    app.events.removeEdge = function(){
        var inputSrc = document.querySelector("#autofont");
        var inputDest = document.querySelector("#autodesti");

        if(!_checkInputs("autofont", "autodesti")) return;

        var paperId;
        var destId;
        var destType;
        if(_inputChoices.source.type == "paper"){
            paperId =_inputChoices.source.id;
            destId = _inputChoices.target.id;
            destType = _inputChoices.target.type;
        }
        else{
            paperId =_inputChoices.target.id;
            destId = _inputChoices.source.id;
            destType = _inputChoices.source.type;
        }
        var edgeRemoved = app.HGraph.removeEdge(_inputChoices.source.id, _inputChoices.source.type,
            _inputChoices.target.id, _inputChoices.target.type);
     /*   if(edgeRemoved)
        {
            //TODO:notify
            app.graph.removeEdge(destId, destType, paperId);
        }*/

        inputSrc.value = "";
        inputDest.value = "";
        _clearInputChoices();
    };

    app.events.selectToolsMenuOption = function(e){
        _selectMenuOption(e.currentTarget, "#toolsMenu", "#queryMenu");
    };
    //---/Tools menu---

}).call(window);














