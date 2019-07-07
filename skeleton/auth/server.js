const express = require('express');
const app = express();

const port = 8090;

app.use(express.json());
app.use(express.urlencoded());

app.post('/auth', (req, res) => {
    var secret = req.body.secret;
    
    if(secret === 'vt'){
        res.status(201).send('ok');
    } else {
        res.status(403).send('no authorization');
    }
});

app.get('/test', (req, res)=> {
    console.log("get message");
    
    res.status(201).send('ok')
});

app.listen(port);