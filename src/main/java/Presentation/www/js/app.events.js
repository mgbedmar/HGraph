(function(){
    'use strict';

    if (typeof app === 'undefined' || typeof app.graph === 'undefined')
        throw 'Error de dependencies';

    //Private
    var _autocompletes = [];
    var _partialGraphDrawn;
    //Nodes for autocomplete
    //TODO actualitzar al esborrar, afegir. Controlar excepcions.
    var _nodes;
    var _popupShown = false;
    var _inputChoices = {
        source:'',
        target:'',
        ref:'',
        result:''
    };

    var _regExpr = /\{[0-9]*\}/;

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

    function _clearUIState(){
        _autocompletes = [];
        _nodes = [];
        _popupShown = false;
        _inputChoices = {
            source:'',
            target:'',
            ref:''
        };
        var elements = document.querySelectorAll(".wrong, .selected, .open");
        for(var i = 0; i < elements.length; i++)
        {
            elements[i].classList.remove("wrong");
            elements[i].classList.remove("selected");
            elements[i].classList.remove("open");
        }

        elements = document.getElementsByTagName("input");
        for(var i = 0; i < elements.length; i++)
        {
            elements[i].value = "";
        }

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


    function _initAutoCompletes(isResult){
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

                element.addEventListener("input", function(e) {
                    _attInput(e.currentTarget.id);
                });

                element.addEventListener("keydown", function(e) {
                    if (e.keyCode === 9) { //tab
                        e.preventDefault();
                    }
                });

                var _autCom = {
                    selector: "#"+app.const.autoInputIds[key],
                    minChars: mChars,
                    cache: false,
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
                if (typeof isResult === 'undefined') {
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
                else if (isResult) {
                    var inp = document.querySelector("#popupContent input[data-autoId="+app.const.autoInputIds[key]+"]");
                    _autCom.selector = "#popupContent input[data-autoId="+app.const.autoInputIds[key]+"]";

                    

                    if (app.const.autoInputIds[key] === 'autoSelect') {
                        _autCom.onSelect = function(e, term, item){
                            var nod = {
                                id: item.dataset.iden,
                                name: item.dataset.nom,
                                type: item.dataset.tipus
                            };
                            _inputChoices.result = nod;
                            var li = document.createElement("li");
                            var divName = document.createElement("div");
                            divName.innerHTML = nod.name;
                            var divButton = document.createElement("div");
                            var iesp = document.createElement("i");
                            iesp.className = "icon ion-close-round closeEl";
                            divButton.appendChild(iesp);
                            li.appendChild(divName);
                            li.appendChild(divButton);


                            document.querySelector("#popupContent .selectNames").appendChild(li);

                            divButton.addEventListener("click", function(e) {
                                //TODO treure filtre
                                e.currentTarget.parentNode.parentNode.removeChild(e.currentTarget.parentNode);
                                e.stopPropagation();
                            });

                        };
                    }

                    else if (app.const.autoInputIds[key] === 'autoFilterNames') {
                        _autCom.onSelect = function(e, term, item){
                            var nod = {
                                id: item.dataset.iden,
                                name: item.dataset.nom,
                                type: item.dataset.tipus
                            };
                            _inputChoices.result = nod;
                            var li = document.createElement("li");
                            app.HGraph.log("li");
                            var divName = document.createElement("div");
                            divName.innerHTML = nod.name;
                            app.HGraph.log("divName "+divName.innerHTML);
                            var divButton = document.createElement("div");
                            var iesp = document.createElement("i");
                            iesp.className = "icon ion-close-round closeEl";
                            divButton.appendChild(iesp);
                            li.appendChild(divName);
                            li.appendChild(divButton);
                            document.querySelector("#popupContent .filterNames").appendChild(li);

                            divButton.addEventListener("click", function(e) {
                                //TODO treure filtre
                                e.currentTarget.parentNode.parentNode.removeChild(e.currentTarget.parentNode);
                                e.stopPropagation();
                            });
                        };
                    }
                    _autocompletes.push(new autoComplete(_autCom));
                }
            }
        }

    }

    function _addResultEvents(rp) {
        document.querySelector("#"+rp.id+" .closeResult").addEventListener("click", app.events.hidePopup);
        _initAutoCompletes(true);
    }

    function _initMain(cb){
        var large = false;
        var nodes=[];

        //If a graph is selected
        if(!app.newProject)
        {
            //Let's see how big it is
            var authorNodes = _getNames(app.HGraph.getNodesOfType(app.const.nodeTypes.author), app.const.nodeTypes.author);
            //app.HGraph.log(JSON.stringify(authorNodes));
            var termNodes = _getNames(app.HGraph.getNodesOfType(app.const.nodeTypes.term), app.const.nodeTypes.term);
            var paperNodes = _getNames(app.HGraph.getNodesOfType(app.const.nodeTypes.paper), app.const.nodeTypes.paper);
            var confNodes = _getNames(app.HGraph.getNodesOfType(app.const.nodeTypes.conf), app.const.nodeTypes.conf);
            nodes = authorNodes.concat(termNodes.concat(paperNodes.concat(confNodes)));
            //app.HGraph.log(JSON.stringify(nodes));
            //Is it larger than maxNodes?
            large = (nodes.length >= app.settings.maxNodes);
        }

        app.graph.init(large);
        //document.querySelector("#mainPage #queryMenu > div > ul > li[data-action=completeGraph]").click();
        
        
        if(app.graph.isLarge())
        {
            _drawPartialGraph("author", cb);
        }
        else
        {
            _drawCompleteGraph();
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
    function _drawPartialGraph(type, cb){
        var nodeobj = {};

        //TODO: node distribution?
        nodeobj[app.const.nodeTypes.conf]  = app.HGraph.getRelevantNodesOfType(app.const.nodeTypes.conf, 100);
        nodeobj[app.const.nodeTypes.term]  = app.HGraph.getRelevantNodesOfType(app.const.nodeTypes.term, 400);
        var authorCapacity = 1000+(500-nodeobj[app.const.nodeTypes.conf].size()-nodeobj[app.const.nodeTypes.term].size());
        nodeobj[app.const.nodeTypes.author] = app.HGraph.getRelevantNodesOfType(app.const.nodeTypes.author, authorCapacity);
        var paperCapacity =1500+(1500-nodeobj[app.const.nodeTypes.author].size());
        nodeobj[app.const.nodeTypes.paper]  = app.HGraph.getRelevantNodesOfType(app.const.nodeTypes.paper, paperCapacity);

        var edgeobj = {};
        edgeobj[app.const.nodeTypes.term]  = app.HGraph.getEdgesOfType(app.const.nodeTypes.term);
        edgeobj[app.const.nodeTypes.author]  = app.HGraph.getEdgesOfType(app.const.nodeTypes.author);
        edgeobj[app.const.nodeTypes.conf]  = app.HGraph.getEdgesOfType(app.const.nodeTypes.conf);

        app.graph.drawOneEdgeTypeGraph(type, nodeobj, edgeobj, cb);
        _partialGraphDrawn = true;
    }

    function _drawCompleteGraph(){
        var nodeobj = {};

        nodeobj[app.const.nodeTypes.conf]  = app.HGraph.getNodesOfType(app.const.nodeTypes.conf);
        nodeobj[app.const.nodeTypes.term]  = app.HGraph.getNodesOfType(app.const.nodeTypes.term);
        nodeobj[app.const.nodeTypes.author] = app.HGraph.getNodesOfType(app.const.nodeTypes.author);
        nodeobj[app.const.nodeTypes.paper]  = app.HGraph.getNodesOfType(app.const.nodeTypes.paper);

        var edgeobj = {};
        edgeobj[app.const.nodeTypes.term]  = app.HGraph.getEdgesOfType(app.const.nodeTypes.term);
        edgeobj[app.const.nodeTypes.author]  = app.HGraph.getEdgesOfType(app.const.nodeTypes.author);
        edgeobj[app.const.nodeTypes.conf]  = app.HGraph.getEdgesOfType(app.const.nodeTypes.conf);

        app.graph.drawGraph(nodeobj, edgeobj);
    }

    function _drawQueryType (type){
        app.events.showDrawing();
        var nodes = app.HGraph.getNodesOfType(type);
        app.graph.drawNodesOnlyGraph(nodes, type);
        _partialGraphDrawn = false;
        app.events.hidePopup();
    }

    function _selectMenuOption(element, currentMenu, theOtherMenu){
        var nodes = document.querySelectorAll(currentMenu +"> div > ul > li");
        var nodes2 = document.querySelectorAll(theOtherMenu +"> div > ul > li");

        for(var i = 0; i < nodes.length; i++)
        {
            if (nodes[i] != element.parentNode) {
                if (nodes[i].classList.contains("selected")) {
                    var inputs = nodes[i].querySelectorAll("input");
                    for (var k = 0; k < inputs.length; k++) {
                        inputs[k].value = '';
                        inputs[k].classList.remove("wrong");
                    }
                    nodes[i].classList.remove("selected");
                }
            }
        }

        for (var j = 0; j < nodes2.length; j++) {
            if (nodes2[j].classList.contains("selected")) {
                var inputs = nodes2[j].querySelectorAll("input");
                for (var k = 0; k < inputs.length; k++) {
                    inputs[k].value = '';
                    inputs[k].classList.remove("wrong");
                }
                nodes2[j].classList.remove("selected");
            }
        }

        element.parentNode.classList.toggle("selected");
        _clearInputChoices();
    }

    function _selectType(parentElement, type){
        //app.HGraph.log("_se");
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

    function _tableCell(index, text, row, title) {
        var cell, div;
        div = document.createElement("div");
        div.innerHTML = text;
        cell = row.insertCell(index);
        if (typeof title != 'undefined') cell.title = title;
        cell.appendChild(div);
    }

    function _selectTypeFromSelector(selector) {
        //app.HGraph.log("sel");
        var typeSelector = document.querySelector(selector);
        var type = typeSelector.dataset.selection;
//app.HGraph.log("sel");
        if(!type)
        {
            typeSelector.classList.add("wrong");
            return false; 
        }
        else {
            typeSelector.classList.remove("wrong");
            return type;
        }
    }

    //Public
    app.events = {};

    app.events.init = function(){
        _show(app.const.pageIds.welcome);
        /*var d = document.createElement("div");
        app.events.showPopup(d);*/
    };

    app.events.loadGoToWelcome = function(){
        _hide(app.const.pageIds.loadGraph, function(){
            _show(app.const.pageIds.welcome);
        });
    };

    app.events.loadGoToMain = function() {
        app.events.showDrawing();
        _hide(app.const.pageIds.loadGraph, function(){
            app.modified = false;
            app.newProject = false;
            _show(app.const.pageIds.main);
            _initMain(app.events.hidePopup);
        });
    };

    app.events.welcomeGoToNewGraph = function(){
        app.events.showDrawing();
        _hide(app.const.pageIds.welcome, function(){
            app.modified = false;
            app.newProject = true;
            _show(app.const.pageIds.main);
            _initMain(app.events.hidePopup);
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

    app.events.showDrawing = function(){
        var div = document.createElement("div");
        div.classList.add("with-border");
        div.style.padding="20px";
        div.innerHTML = "Dibuixant...";
        app.events.showPopup(div);
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

    app.events.showPrompt = function(t, msgRequest, okMsg, cancelMsg, cb){
        if(_popupShown) return;
        var div = document.createElement("div");
        div.classList.add("accept");
        div.classList.add("with-border");
        var title = document.createElement("h1");
        title.innerHTML = t || "Pregunta";
        var text = document.createElement("span");
        text.innerHTML = msgRequest;
        if (msgRequest.length > 120) {
            div.classList.add("large");
        }
        else if (msgRequest.length > 80) {
            div.classList.add("big");
        }
        var input = document.createElement("input");
        var divbtns = document.createElement("div");
        divbtns.classList.add("divbtns");
        var okbtn = document.createElement("a");
        okbtn.innerHTML = okMsg;
        okbtn.addEventListener("click", function(){
            app.events.hidePopup();
            if(cb) cb(input.value);
        });
        if (typeof cancelMsg != 'undefined') {
            var cancelbtn = document.createElement("a");
            cancelbtn.innerHTML = cancelMsg;
            cancelbtn.addEventListener("click", function(){
                app.events.hidePopup();
            });
            divbtns.appendChild(cancelbtn);
        }
        divbtns.appendChild(okbtn);
        div.appendChild(title);
        div.appendChild(text);
        div.appendChild(input);
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

    app.events.filterEdges = function(e){
        var type = _selectTypeFromSelector("#queryMenu li[data-action=completeGraph] ul[data-action=filterEdges]");
        if(!_partialGraphDrawn){
            app.events.showDrawing();
            _drawPartialGraph(type, function(){
                app.events.hidePopup();
            });
        }
        else
            app.graph.selectEdges(type);



    };
    //e.target is the type button pressed
    app.events.drawTypeGraph = function(e){

        _drawQueryType(e.currentTarget.dataset.type);
    };

    //TODO
    app.events.queryNeighbours = function(nodeid){
        app.events.showDrawing();
        var nodes = app.HGraph.getNodesOfType(type);

    };
    //----/QueryMenu

    app.events.query1to1 = function() {

        if (!_checkInputs("auto1To1-1", "auto1To1-2")) return;
        app.HGraph.query1to1(_inputChoices.source.id, _inputChoices.source.type,
                             _inputChoices.target.id, _inputChoices.target.type);
        app.events.showLoading();

    };

    app.events.query1toN = function() {
        var type = _selectTypeFromSelector("#query1toN .typeSelector");
        if (!_checkInputs("auto1ToN")) return;
        if (!type) return;
        app.HGraph.query1toN(_inputChoices.source.id, _inputChoices.source.type, type);
        app.events.showLoading();
    };

    app.events.queryNtoN = function() {
        var type1 = _selectTypeFromSelector("#primer");
        var type2 = _selectTypeFromSelector("#segon");
        if (!type1 || !type2) return;
        app.HGraph.queryNtoN(type1, type2);
        app.events.showLoading();
    };

    app.events.queryByReference = function() {
        if (!_checkInputs("autoref1", "autoref2", "autoref3")) return;
        app.HGraph.log("abans");
        app.HGraph.queryByReference(_inputChoices.source.id, _inputChoices.source.type,
                                    _inputChoices.target.id, _inputChoices.target.type,
                                    _inputChoices.ref.id, _inputChoices.ref.type);
        app.HGraph.log("despres");
        app.events.showLoading();
    };


    app.events.takeQuery1To1Result = function() {
        var hm = String(app.HGraph.getQuery1To1Result());

        var rPveritat = document.getElementById("resultPopup");
        var rP = rPveritat.cloneNode(true);
        document.getElementById("popupContent").innerHTML = '';
        rP.id = rP.id+1;
        document.getElementById("popupContent").appendChild(rP);
        var table = document.querySelector("#"+rP.id+" table");

        /* La fila */
        var row = table.insertRow(1);
        /* Columna de numeracio */
        var zeroCol = row.insertCell(0);
        var div = document.createElement("div");
        div.innerHTML = '1';
        zeroCol.appendChild(div);
        /* Columna de la font */
        var firstCol = row.insertCell(1);
        firstCol.title = _inputChoices.source.id;
        var div = document.createElement("div");
        div.innerHTML = _inputChoices.source.name;
        firstCol.appendChild(div);
        /* Columna del desti */
        var firstCol = row.insertCell(2);
        firstCol.title = _inputChoices.target.id;
        var div = document.createElement("div");
        div.innerHTML = _inputChoices.target.name;
        firstCol.appendChild(div);
        app.HGraph.log(table.innerHTML);
        /* Columna del hetesim */
        var firstCol = row.insertCell(3);
        var div = document.createElement("div");
        div.innerHTML = hm;
        firstCol.appendChild(div);

        _addResultEvents(rP);
        app.events.hidePopup(function() { app.events.showPopup(rP); });

    };

    app.events.takeQueryResult = function() {
        var result = app.HGraph.getQueryResult();

        var rPveritat = document.getElementById("resultPopup");
        var rP = rPveritat.cloneNode(true);
        document.getElementById("popupContent").innerHTML = '';
        rP.id = rP.id+1;
        document.getElementById("popupContent").appendChild(rP);
        var table = document.querySelector("#"+rP.id+" table");

        for (var i = 0; i < result.size(); i++) {
            var row = table.insertRow(i+1);

            _tableCell(0, String(result.get(i).get(0)), row);

            for (var j = 1; j <=2; ++j) {
                var act = String(result.get(i).get(j));
                var tit = String(_regExpr.exec(act));
                var nom = act.slice(0, -tit.length);
                tit = tit.slice(1, -1);
                _tableCell(j, nom, row, tit);
            }
            var hm = String(result.get(i).get(3));
            hm = hm.slice(0, 7-hm.length);
            _tableCell(3, hm, row);
        }

        _addResultEvents(rP);
        app.events.hidePopup(function() { app.events.showPopup(rP); });
    };

     //---Tools menu----
    app.events.openToolsMenu = function(){
            document.querySelector("#"+app.const.pageIds.main + " #toolsMenu").classList.toggle("open");
    };

    app.events.addNode = function(){
        var typeSelector = document.querySelector("#addNodeSection .typeSelector");
        var type = typeSelector.dataset.selection;
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
        if(id != null)
        {
            app.graph.addNode(id, input.value, type);
            app.events.notify("S'ha creat el node "+input.value);
            _nodes.push([String(input.value),String(id), type]);
            app.modified = true;
        }


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

       if(edgeAdded){
           app.events.notify("S'ha creat l'aresta ("+destId+", "+paperId+")");
            app.modified = true;
            app.graph.addEdge(destId, destType, paperId);

        }

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
            app.events.notify("S'ha esborrat el node ("+_inputChoices.source.id+")");
            app.modified = true;
            app.graph.removeNode(_inputChoices.source.id, _inputChoices.source.type);
            var found = false;
            var i;
            for(i = 0; i < _nodes.length && !found; i++)
            {
                found = (_nodes[i][1] == _inputChoices.source.id && _nodes[i][2] == _inputChoices.source.type);
            }
            if(found)
                _nodes.splice((i-1), 1);
            

            
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
        if(edgeRemoved)
        {
            app.modified = true;
            app.events.notify("S'ha esborrat l'aresta ("+destId+", "+paperId+")");
            app.graph.removeEdge(destId, destType, paperId);
        }

        inputSrc.value = "";
        inputDest.value = "";
        _clearInputChoices();
    };

    app.events.selectToolsMenuOption = function(e){
        _selectMenuOption(e.currentTarget, "#toolsMenu", "#queryMenu");
    };
    //---/Tools menu---

    //---Menu menu---
    app.events.openMainMenu = function(){
        document.getElementById("mainMenu").classList.toggle("open");
    };

    app.events.mainToHome = function(){
        function go(){
            _hide(app.const.pageIds.main, function(){
                _clearUIState();
                app.HGraph.unSelectProject();
                _show(app.const.pageIds.welcome);
            });
        }

        if(app.modified)
            app.events.showAccept("Sortir", "Si surts es perdràn els canvis que no has desat. Vols continuar?",
            "Sortir", go, "Cancela");
        else
            go();
    };
    
    app.events.save = function(){
        if(app.newProject){
            app.events.saveAs();
        }
        else{
            app.HGraph.save();
            app.events.notify("S'ha guardat el projecte");
            app.modified = false;
        }
    };

    app.events.saveAs = function(){
        app.events.showPrompt("Guardar el projecte", "Escriu el nom del projecte:", "Guardar", "Cancela", function(name){
            app.HGraph.saveAs(name);
            app.newProject = false;
            app.modified = false;
            app.events.notify("S'ha guardat el projecte amb el nom "+name);
        });

    };

    app.events.showSettings = function(){
        //TODO
    };

    app.events.showHelp = function(){
        //TODO
    };


    //---/Menu menu---

    app.events.notify = function(msg){
        var notification = document.createElement("div");
        notification.innerHTML = msg;
        var notifyBox = document.getElementById("notifyBox");
        if(notifyBox.childNodes.length)
            notifyBox.insertBefore(notification, notifyBox.childNodes[0]);
        else
            notifyBox.appendChild(notification);

        setTimeout(function(){
            notification.classList.add("hide");
            setTimeout(function(){
                notification.classList.add("collapse");
                setTimeout(function(){
                   notification.remove();
                }, 200);
            }, 200);
        }, 5000);
    };

}).call(window);














