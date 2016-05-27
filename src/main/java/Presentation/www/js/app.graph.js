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
            enableHovering:true, //etiquetes: posades no funciona be
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

    function _applySettings(s){
        //Deprecated

        /*
        if(typeof _settings.relativeSize !== 'undefined')
            sigma.plugins.relativeSize(s, _settings.relativeSize);
*/
        /*
        if(typeof _settings.nooverlap !== 'undefined' && _settings.nooverlap)
        {
            // Configure the noverlap layout:
            var noverlapListener = s.configNoverlap({
                nodeMargin: 0.05,
                scaleNodes: 1,
                gridSize: 100,
                speed:10
            });
            // Bind the events:
            noverlapListener.bind('start stop interpolate', function(e) {
                console.log(e.type);
                if(e.type === 'start') {
                    console.time('noverlap');
                }
                if(e.type === 'stop') {
                    console.timeEnd('noverlap');

                }
            });
            s.startNoverlap();
        }
        */

    }

    function _updateSize(nodes) {
        for(var i = 0; i < nodes.length; i++) {
            var idType = nodes[i].id.split("-");
            nodes[i].size = app.HGraph.queryNeighboursSize(idType[0], idType[1]);
        }
    }

    /* nodes{
               typeN:ArrayList<String[3]>
           }*/
    function _createGraph(nodes, edges){
        var g = {nodes:[], edges:[]};
        var totalSize = nodes.author.size()+nodes.paper.size()+nodes.term.size()+nodes.conf.size();

        var pos = {
            x: 0,
            y: 0
        };

        //For each type in nodes
        for (var type in nodes)
        {

            var tot = Math.sqrt(totalSize);
            //Check if type is a property of nodes
            if (nodes.hasOwnProperty(type))
            {
                //TODO: calculate radius and position and color of types
                //Add nodes of type to the graph

                for (var i = 0; i < nodes[type].size(); i++)
                {
                    //var pos = _getNextPosition(1./totalSize, 1/(totalSize));

                    g.nodes.push({
                        id: String(nodes[type].get(i)[0])+"-"+type,
                        label: String(nodes[type].get(i)[1]),
                        x: pos.x,
                        y: pos.y,
                        size: String(nodes[type].get(i)[2])
                    });


                    pos.x++;
                    if (pos.x-tot > 0) {
                        pos.x = 0;
                        pos.y++;
                    }
                }

            }
        }

        for (type in edges) {
            if (edges.hasOwnProperty(type))
            {
                for (var i = 0; i < edges[type].size(); i++)
                {
                    g.edges.push({
                        id: String(edges[type].get(i)[0])+"-"+type+"-"+String(edges[type].get(i)[1]),
                        source: String(edges[type].get(i)[0]+"-paper"),
                        target: String(edges[type].get(i)[1])+"-"+type
                    })
                }
            }
        }

        return g;
    }

    //TODO
    function _getCircleRandomPos(radius, gap){
        var t = 2*Math.PI*Math.random();
        var u = Math.random()*(radius+10)+Math.random()*(radius+10)+gap;
        var r = u;
        if(u>1)
            r = 2-u;
        return {x: Math.cos(t)*r, y: Math.sin(t)*r};
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


    //Public
    app.graph = {};

    app.graph.init = function(large){
        _largeGraph = large;
    };

    app.graph.isLarge = function(){
        return _largeGraph;
    };


    //Draws a graph with a big number of nodes but without edges, nodes = JavaArrayList
    app.graph.drawNodesOnlyGraph = function(nodes, type){
        var g={nodes:[]};
        var i = 0;
        if(typeof _sarr === 'undefined')
            _sarr = [];
        var s;
        var pos_centre = {x:0, y:0}
        var pos;
        var colors = ["#FF0000", "#00FF00", "#0000FF"];
        //For each node
        var c = 0;
        var inizoom = 0.3;
        _radius = 0.004;
        _angle = 0;
        while(i < nodes.size())
        {

            //TODO: rings
            //var pos = _getCircleRandomPos(i*40,i*20);
            pos = _getNextPosition(0.004, 0.0005);

            g.nodes.push({
                id: String(nodes.get(i)[0])+"-"+type,
                label: String(nodes.get(i)[1]),
                x: pos.x,
                y: pos.y,
                size: String(nodes.get(i)[2]),
                color: colors[c]
            });
            //If graph is % maxNodes, start a new one
            if(i % app.settings.maxNodes == 0)
            {
                c = (c+1) %3;

                s=new sigma({
                    container: 'graph-container',
                    graph:g,
                    settings:_settings.graph
                });

                //s.camera.ratio = inizoom;
                _applySettings(s);
                s.refresh();
                _sarr.push(s);
                g = {nodes:[]};
            }
            i++;
        }
        //If nodes remaining, start a new graph //TODO aixo esta fet? -si
        if(g.nodes.length > 0)
        {
            s=new sigma({
                container: 'graph-container',
                graph:g,
                settings:_settings.graph
            });

            //s.camera.ratio = inizoom;
            _applySettings(s);
            s.refresh();


            _sarr.push(s);
        }

        s = new sigma({
            container: 'graph-container',
            graph:{nodes:[]},
            settings: _settings.graph
        });

        //s.camera.ratio = inizoom;
        s.refresh();

        s.camera.bind('coordinatesUpdated', app.debounce(function(){
            _sarr.forEach(function(si){
                si.camera.x = s.camera.x;
                si.camera.y = s.camera.y;
                si.camera.ratio = s.camera.ratio;
                si.refresh();
            })
        }, 10));
        

    };
    //Draws a bigraph representing a table. Each connected component must be < maxNodes
    app.graph.drawTableBasedGraph = function(nodes, edges, cb){
        //TODO
    };

    //Draws a normal graph, nodes = {author:JavaArrayList, paper:...}
    app.graph.drawGraph = function(nodes, edges){
        var g;
        if (typeof edges === 'undefined') g = nodes;
        else g = _createGraph(nodes, edges);

        if (typeof _sarr != 'undefined')
        {
            for (var i = 0; i < _sarr.length; ++i) {
                //this gets rid of all the ndoes and edges
                _sarr[i].graph.clear();
                _sarr[i].refresh();
            }
        }

        var customSettings = _settings.graph;
        customSettings.maxNodeSize = 10;
        customSettings.minNodeSize = 6;

        _sarr = [new sigma({
            graph: g,
            settings: customSettings
        })];


        _sarr[0].addRenderer({
            container: 'graph-container',
            type:'canvas',
            settings: {
                batchEdgesDrawing: false
            }
        });
        _sarr[0].refresh();

        _sarr[0].camera.ratio = 1.7;
        _sarr[0].refresh();
        _applySettings(_sarr[0]);
    };

    //result es un [] amb un sol element {source, target, hetesim}
    app.graph.drawQuery1to1 = function(result) {
        var g = {
            nodes: [],
            edges: []
        };
        g.nodes.push({
            id: result[0].source+"1",
            label: result[0].source,
            x: -1,
            y: -1,
            //TODO colors corresponents al tipus... problema: el resultat no dona el tipus, cal passarlo
        });

        g.nodes.push({
            id: result[0].target+"2",
            label: result[0].target,
            x: 1,
            y: 1,
            //TODO colors corresponents al tipus... problema: el resultat no dona el tipus, cal passarlo
        });
        g.edges.push({
            id:"1",
            source:result[0].source+"1",
            target:result[0].target+"2",
            label:result[0].hetesim
        });

        app.graph.drawGraph(g);

    }

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
            pos = _getCircleRandomPos(10,10);
        }

        _sarr[index].graph.addNode({
            id: id+"-"+type,
            label: label,
            x: pos.x,
            y: pos.y,
            size:1
            //TODO: color depenent del type
        });

        _sarr[_sarr.length-1].refresh();

    };

    app.graph.addEdge = function(srcId, typeA, paperId){
        _sarr[0].graph.addEdge({
            id: paperId+"-"+typeA+"-"+srcId,
            source: paperId+"-paper",
            target: srcId+"-"+typeA,
            type:"curve"
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
