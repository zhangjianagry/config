const express = require('express');
const app = express();
const port = 3000;
const ws = require('express-ws')(app);

var config = "before";

app.get('/', (req, res) => {
    res.sendfile(__dirname + '/index.html');
});

app.get('/config', (req, res) => {
    res.send(config);
})

setTimeout(() => {
    config = "after"
}, 6000);

app.listen(port);