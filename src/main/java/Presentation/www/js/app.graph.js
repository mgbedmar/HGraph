/*
         settings: {

-            minNodeSize: 1,

-            maxNodeSize: 4,

+            minNodeSize: 0.5,

+            maxNodeSize: 3,

             minEdgeSize: 0.2,

             maxEdgeSize: 0.5,

-            eventsEnabled: true,

-            minRatio: 10, // How far can we zoom out?

-            maxRatio: 20, // How far can we zoom in?

+            eventsEnabled: false,

+            minRatio: 0, // How far can we zoom out?

+            maxRatio: 0, // How far can we zoom in?

             defaultLabelColor: "#000",

-            defaultLabelSize: 14,

+            defaultLabelSize: 15,

             defaultLabelBGColor: "#ddd",

             defaultHoverLabelBGColor: "#002147",

             defaultLabelHoverColor: "#fff",

-            labelThreshold: 10,

+            labelThreshold: 18,

             defaultEdgeType: "curve",

-            hoverFontStyle: "bold",

             fontStyle: "bold",

             activeFontStyle: "bold"

         }
         */

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
            labelThreshold: 11.5,
            defaultEdgeType: "curve",
            autoRescale:true,
            edgeLabels:true,
            enableHovering:false, //etiquetes: posades no funciona be
            zoomMin:0.001, //no va
            zoomMax:2
        },
        relativeSize:0.5,
        edgeLabels:true,
        nooverlap:false

    };

    //For graph layout
    var _radius = 0.001;
    var _angle = 0;

    var _pos = {
        author: {x:0, y:0},
        paper: {x:0, y:1},
        term: {x:0, y:2},
        conf: {x:0, y:3},
        global: {x:0, y:4}
    };
    var _cachedges={
        "author":[],
        "paper":[],
        "conf":[]
    };
    var _sqrt;

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
            _radius += incrRad; //valors que mes o menys van: 0.000001, 0.01, 0, 0.05
        }

        return pos;
    }

    function _getNextSmallPosition(sqrt, type) {

        _pos[type].x++;

        /*if (_pos[type].x > sqrt+1) {
            _pos[type].x = 1;
            _pos[type].y = _pos.global.y;
            _pos.global.y++;
        }*/
        return _pos[type];
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
    app.graph.drawGraph = function(nodes, edges, baseType, cbend, edgeType){
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
                    //TODO: define ratios
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
                for (var i = 0; i < edges[type].size() && i < app.settings.maxEdges; i++)
                {

                    if(exists[type][String(edges[type].get(i)[1])] && exists[baseType][String(edges[type].get(i)[0])]){
                        _cachedges[type].push({
                            id: String(edges[type].get(i)[0])+"-"+type+"-"+String(edges[type].get(i)[1]),
                            source: String(edges[type].get(i)[0]+"-"+baseType),
                            target: String(edges[type].get(i)[1])+"-"+type,
                            //TODO:define edge colors
                            color: _typeColorEdge[typeColor]
                        });
                        j++;
                        //app.HGraph.log(String(edges[type].get(i)[0])+"-"+type+"-"+String(edges[type].get(i)[1]));
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
            if(edgeType)
                g.edges = _cachedges[edgeType];
            else
            {
                if(_cachedges["author"].length)
                    g.edges = _cachedges["author"];
                if(_cachedges["conf"].length)
                    g.edges = g.edges.concat(_cachedges["conf"]);
                if(_cachedges["term"].length)
                    g.edges = g.edges.concat(_cachedges["term"]);
                if(_cachedges["paper"].length)
                    g.edges = g.edges.concat(_cachedges["paper"]);
            }
            _sarr[0] = new sigma({
                container: 'graph-container',
                graph:g,
                settings: _settings.graph,
                clone:false
            });
            if(edgeType)
                _sarr[0].camera.ratio=0.3;

            _sarr[0].refresh();
            document.getElementById("graph-container").style.opacity=0;

            setTimeout(function(){
                _sarr[0].refresh();
                document.getElementById("graph-container").style.opacity=1;
                if(cbend) cbend();
            },500);


        });

    };

    app.graph.selectEdges = function(type){
        var edges = _sarr[0].graph.edges();
        for(var i = 0; i < edges.length; i++)
        {
            _sarr[0].graph.dropEdge(edges[i].id);
        }

        _sarr[0].graph.read({edges:_cachedges[type]});
        _sarr[0].refresh();

    };
    //Draws a bigraph representing a table. Each connected component must be < maxNodes
    app.graph.drawTableBasedGraph = function(nodes, edges, cb){
        //TODO
    };

 

 
    app.graph.addNode = function(id, label, type){
        var pos;
        var index =_sarr.length-1;
        if(_largeGraph)
        {
            pos = _getNextPosition();
            index--;
        }
        else
        {
            _sqrt = Math.sqrt(_sqrt+1);
            pos = _getNextSmallPosition(_sqrt, type)
        }

        _sarr[index].graph.addNode({
            id: id+"-"+type,
            label: label,
            x: pos.x,
            y: pos.y,
            size:1,
            color: _typeColor[type]
        });

        _sarr[_sarr.length-1].refresh();

    };

    app.graph.addEdge = function(srcId, typeA, paperId){
        _sarr[0].graph.addEdge({
            id: paperId+"-"+typeA+"-"+srcId,
            source: paperId+"-paper",
            target: srcId+"-"+typeA,
            type:"curve",
            color:"black"
        });

        _sarr[0].refresh();
    };

    app.graph.removeNode = function(id, type){

        for(var i = 0; i < _sarr.length; i++)
        {
            try {
                _sarr[i].graph.dropNode(id+"-"+type);
                _updateSize(_sarr[i].graph.nodes());
                _sarr[i].refresh();
            }
            catch(err)
            {
                //app.HGraph.log("no:"+err);
            }
        }
    };

    app.graph.removeEdge = function(destId, destType, paperId){

        try {
            _sarr[0].graph.dropEdge(paperId+"-"+destType+"-"+destId);
            _updateSize(_sarr[0].graph.nodes());
            _sarr[0].refresh();
        }
        catch(err)
        {
            //app.HGraph.log("no:"+err);
        }

    };

}).call(window);
