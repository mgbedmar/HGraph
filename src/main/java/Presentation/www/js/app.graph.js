(function() {
    'use strict';

    if (typeof app === 'undefined' || app.HGraph === 'undefined')
        throw 'Error de dependencies';

    //Private
    //Array of sigma instances
    var _sarr;
    //True if graph.nodes > maxNodes
    var _largeGraph;
    var _settings = {
        graph:{
            //minNodeSize: 1,
            maxNodeSize: 4,
            minEdgeSize: 0.2,
            maxEdgeSize: 0.5,
            eventsEnabled: true,
            labelThreshold: app.settings.labelThreshold,
            defaultEdgeType: "curve",
            autoRescale:true,
            edgeLabels:true,
            enableHovering:false,
            zoomMin:0.001,
            zoomMax:2
        }

    };

    //For graph layout
    var _radius = 0.001;
    var _angle = 0;

    var _cachedges={
        "author":[],
        "paper":[],
        "conf":[]
    };

    var _typeColor = {
        author: "steelblue",
        paper: "purple",
        conf: "forestgreen",
        term: "darkred"
    };
    var _typeColorEdge = {
        author: "steelblue",
        paper: "purple",
        conf: "forestgreen",
        term: "darkred"
    };

    function _updateSize(nodes) {
        for(var i = 0; i < nodes.length; i++) {
            var idType = nodes[i].id.split("-");
            nodes[i].size = app.HGraph.queryNeighboursSize(idType[0], idType[1]);
        }
    }


    function _getNextPosition(incrRad, ratAngle) {
        var pos = {x: Math.cos(2*Math.PI*_angle)*_radius, y: Math.sin(Math.PI*2*_angle)*_radius};
        _angle = _angle + ratAngle/(_radius); //inversament proporcional
        if (_angle > 1) {
            _angle = _angle-1;
            _radius += incrRad; //valors adequats per grafs grans: 0.000001, 0.01, 0, 0.05
        }

        return pos;
    }

    function _clearGraphs(){
        if (typeof _sarr != 'undefined')
        {
            for (var i = 0; i < _sarr.length; i++) {
                //this gets rid of all the ndoes and edges
                _sarr[i].graph.clear();
                _sarr[i].refresh();
                _sarr[i].kill();

            }

        }
        _sarr =[];
    }

    function _clearEdges(){
        var edges = _sarr[0].graph.edges();
        for(var i = 0; i < edges.length; i++)
        {
            _sarr[0].graph.dropEdge(edges[i].id);
        }
    }

    //Public
    app.graph = {};

    app.graph.init = function(large){
        _clearGraphs();
        _largeGraph = large;
    };

    app.graph.isLarge = function(){
        return _largeGraph;
    };

    //Draws a graph. id edgeType is specified, then draws only edges of type edgeType
    app.graph.drawGraph = function(nodes, edges, baseType, cbend){
        var g={nodes:[], edges:[]};
        _cachedges = {
            "author":[],
            "conf":[],
            "term":[],
            "paper":[]
        };
        _clearGraphs();
        var pos;
        //For each node
        _radius = 0.004;
        _angle = 0;
        var exists = {
            "paper":{},
            "author":{},
            "conf":{},
            "term":{}
        };
        var j = 0;
        for (var type in nodes) {
            //Check if type is a property of nodes
            if (nodes.hasOwnProperty(type)) {
                if(typeof nodes[type] === 'undefined') continue;

                for (var i = 0; i < nodes[type].size() && j < app.settings.maxNodes; i++)
                {
                    pos = _getNextPosition(0.004, 0.0005);

                    var random = Math.random();
                    if(random < app.settings.marginRatio[type])
                    {
                        --i;
                        continue;
                    }

                    var id=String(nodes[type].get(i)[0]);
                    exists[type][id] = true;
                    g.nodes.push({
                        id: String(nodes[type].get(i)[0])+"-"+type,
                        label: String(nodes[type].get(i)[1]),
                        x: pos.x,
                        y: pos.y,
                        size: String(nodes[type].get(i)[2]),
                        color: _typeColor[type]
                    });
                    j++;
                }
            }
        }

        var ble = ["author", "term", "conf", "paper"];
        baseType = baseType || "paper";
        var a = 0;
        function calc(type, cb){
            var j = 0;
            if(edges[type])
            {
                var typeColor = type;
                if (type == "paper") typeColor = baseType;
                for (var i = 0; i < edges[type].size() && j < app.settings.maxEdges; i++)
                {
                    if(exists[type][String(edges[type].get(i)[1])] && exists[baseType][String(edges[type].get(i)[0])]){
                        _cachedges[type].push({
                            id: String(edges[type].get(i)[0])+"-"+type+"-"+String(edges[type].get(i)[1]),
                            source: String(edges[type].get(i)[0]+"-"+baseType),
                            target: String(edges[type].get(i)[1])+"-"+type,
                            color: _typeColorEdge[typeColor]
                        });
                        j++;
                    }
                }
            }
            setTimeout(function(){
                a++;

                if(a == 4)
                    cb();
                else
                    calc(ble[a], cb);

            }, 200);

        }
        calc(ble[0], function(){
            _sarr[0] = new sigma({
                graph:g,
                settings: _settings.graph,
                clone:false,
                renderers:[{
                    container: document.getElementById("graph-container"),
                    type: 'canvas',
                    settings:{
                        font: 'sans-serif',
                        labelThreshold: app.settings.labelThreshold,
                        hideEdgesOnMove: app.settings.hideEdges
                    }
                }]
            });

            if(_largeGraph)
                _sarr[0].camera.ratio=0.3;
            else
                _sarr[0].camera.ratio=0.05;


            setTimeout(function(){
                _sarr[0].refresh();
                if(cbend) cbend();
            },200);


        });

    };

    app.graph.selectEdges = function(type){
        _clearEdges();

        _sarr[0].graph.read({edges:_cachedges[type]});
        _sarr[0].refresh();

    };
    app.graph.allEdges = function(){
        var edges = [];
        _clearEdges();
        for(var type in _cachedges)
        {
            if(_cachedges.hasOwnProperty(type))
            {
                edges = edges.concat(_cachedges[type]);
            }
        }
        _sarr[0].graph.read({edges:edges});
        _sarr[0].refresh();
    };
    app.graph.noEdges = function(){
        _clearEdges();
        _sarr[0].refresh();
    };
 

 
    app.graph.addNode = function(id, label, type){
        var pos;
        pos = _getNextPosition(0.004, 0.0005);

        _sarr[0].graph.addNode({
            id: id+"-"+type,
            label: label,
            x: pos.x,
            y: pos.y,
            size:1,
            color: _typeColor[type]
        });

        _sarr[0].refresh();


    };

    app.graph.addEdge = function(srcId, typeA, paperId){
        var dropId =paperId+"-"+typeA+"-"+srcId;
        var edge = {
            id: dropId,
            source: paperId+"-paper",
            target: srcId+"-"+typeA,
            type:"curve",
            color:_typeColor[typeA]
        };
        _removeEdgeFromCache(dropId);
        _cachedges[typeA].push(edge);
        _sarr[0].graph.addEdge(edge);

        _sarr[0].refresh();
    };

    app.graph.removeNode = function(id, type){
        var nodeId = id+"-"+type;
        _removeNodeFromCache(nodeId);
        _sarr[0].graph.dropNode(nodeId);
        _updateSize(_sarr[0].graph.nodes());
        _sarr[0].refresh();
    };

    function _removeNodeFromCache(nodeId){
        for(var type in _cachedges)
        {
            if(_cachedges.hasOwnProperty(type))
            {
                for(var i = 0; i < _cachedges[type].length; i++)
                {
                    if(_cachedges[type][i].source == nodeId ||
                        _cachedges[type][i].target == nodeId){
                        _cachedges[type].splice(i, 1);
                    }
                }
            }
        }
    }

    function _removeEdgeFromCache(dropId){
        for(var type in _cachedges)
        {
            if(_cachedges.hasOwnProperty(type))
            {
                for(var i = 0; i < _cachedges[type].length; i++)
                {
                    if(_cachedges[type][i].id == dropId){
                        _cachedges[type].splice(i, 1);
                        return;
                    }
                }
            }
        }

    }
    app.graph.removeEdge = function(destId, destType, paperId){
        var dropId = paperId+"-"+destType+"-"+destId;
        _sarr[0].graph.dropEdge(dropId);

        _removeEdgeFromCache(dropId);
        _updateSize(_sarr[0].graph.nodes());
        _sarr[0].refresh();

    };

}).call(window);
