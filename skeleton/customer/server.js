const express = require('express');
const app = express();
const client = require('vt-config-toy-client');
const port = 3001;


//config object
var testValue = {
};

//login
client.login('23.100.97.156', '9080', 'webSocket', 'secret', 'vt');
//bind the value
client.config(testValue, 'testconfig', 'default');

app.get('/', (req, res) => {
    res.sendfile(__dirname + '/index.html');
});

app.get('/config', (req, res) => {
    res.send(testValue.testconfig);
})

app.listen(port);