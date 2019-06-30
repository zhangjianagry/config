const cc = require('./vt-config-toy-client');

cc.login('127.0.0.1', '8090', 'webSocket', 'secret', 'vt');

var testValue = {
};

cc.config(testValue, 'testconfig', 'default');

setInterval(() => {
    console.log('current  value:' + testValue.testconfig);
}, 2000);