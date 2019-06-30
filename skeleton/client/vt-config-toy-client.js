const Rx = require('rxjs');
const WebSocket = require('ws');

var observable;

/*	NOTE:
	1. a client package may be 'require' or 'import' into different files, so it needs to set related values as global.sth
	2. since built-in types in js are immutable, the config must be an object
	3. only support single value bind for now
	4. only support one level of a config object
	5. feature for saving and loading configuration locally
*/

/**
 * @description login 
 * @date 2019-06-29
 * @param {*} ip		Server's ip address
 * @param {*} port		Server's port
 * @param {*} strategy  I think we can use websocket, polling, long polling, HTTP persistent connection, keep-alive 
						or sth like webHook, watcher to implement the communication feature.
						Here I use websocket and use reactive programming by rxjs. 
						I do this mainly for a push pattern when data changed:
						Explanation：
						1. the ws receive the message which is the new config
						2. push the data into observable
						3. values who subscribe the observable will be notified which means they will change immediately

 * @param {*} auth		I guess client can log in through token, cookie, username/password or just a secret we provide
						And, since it is a toy for a skeleton, I have no interest to implement them all. So I assume that a 							client that sends 'vt' after connecting is the trusted client and the connection will not be closed then

 * @param {*} secret 	The data to transfer when log in.
 */
exports.login = function(ip, port, strategy, auth, secret) {
	const ws = new WebSocket('ws://' + ip + ':' + port); 
	ws.on('open', function open() {
  		ws.send(secret);
	});

	observable = Rx.Observable.create(function (observer) {
		ws.on('message', function incoming(data) {
			observer.next(data);
		});	
	});
}

/**
 * @description bind the value
 * @date 2019-06-29
 * @param {*} val 		the config val
 * @param {*} param 	config param, like config.testconfig
 * @param {*} defVal 	the default val
 */
exports.config = function(val, param, defVal) {
	// here the client need to request the value immediately
	val[param] = defVal;
	console.log("binded");
	observable.subscribe({
		next: x => val[param] = x
	});
}



