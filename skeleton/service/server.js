const WebSocket = require('ws');
const express = require('express');
const http = require('http');
const request = require('request');

const app = express();
app.use(express.json());
app.use(express.urlencoded());
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

const authServer = 'http://127.0.0.1:8092/auth';
const port = 8090;

// a simple one2one publisher-subscriber pattern
function Publisher() {
    this.subscriber = null;
}

Publisher.prototype.bind = function(sb) {
    this.subscriber = sb;
};

Publisher.prototype.isBinded = function() {
    return this.subscriber !== null;
};

Publisher.prototype.notify = function(msg) {
    this.subscriber.send(msg);
};


var pb = new Publisher();

wss.on('connection', function connection(ws) {

    ws.on('message', function incoming(message) {
        request.post( 
            authServer, 
            {
                json: {
                    secret: message
                }
            }, 
            (error, res, body) => {
                if( res.statusCode == 201){
                    //subscribe
                    pb.bind(ws);
                } else {
                    ws.send("no authorization");
                    ws.terminate();
                }
            }
        );
    });
});

var config = {
    "testconfig": "init"
}

// GET config
app.get('/config', (req, res) => {
    res.send(config);
});

// PUT config
app.put('/config', (req, res) => {
    var newConfig = req.query;
    config = newConfig;
    if(pb.isBinded()){
        // if config is changed, notify the client through websocket channel immediately
        // currently, only support hard-code's testconfig parameter, however, it can be replace with JSON.stringify() and JSON.parse() to achieve object support.
        // or you can just implement multi WebSocket channel with schedule strategy for a better performance
        pb.notify(newConfig["testconfig"]);
    }
    res.send(config,201);
});

server.listen(port);