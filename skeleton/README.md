# skeleton

An extremely simple config skeleton by WebSocket implementation( may not suit ) and kubernetes deployment.

Well, I met some troubles when accessing the mogoDB server and I cannot understand some code with no docs in the project, so I code some simple module by Node.js for  convenient deployment.

## Basic

Since all the modules is nearly a placeholder for deployment, I only implement the basic feature.

## How to test

Deploy or run `customer` locally with `node server.js` (node env required)

- GET method `http://23.100.97.156:9080/config`  to get value
- PUT method `http://23.100.97.156:9080/config?testconfig=newvalue`  to update value, and the customer will receive 'newvalue'

## Service

RESTful server response to frontend's 'GET' and 'PUT' requests

WebSocket server to communicate with a client

Request to auth for authorization

## Auth

Auth's module does not exist in this project, so I don't know what your auth exactly do.

I guess to auth the customer when they try to connect?

So I implement a simple auth that If the secret is 'VT' return true.

## Customer

Customer is the demo customer app, although the change is immediately passed to the client. These changes need to be viewed when demonstrating to the audience. I use polling with 5 seconds' interval for presentation.

## Client

Client module is not in this project either. We should not let service to register their configuration through hard-code, we need a client to encapsulate related code and publish to their Language's marketplace.

I implement a Node.js environment's client and publish on [npm](https://www.npmjs.com/package/vt-config-toy-client)

> more in comment

## Frontend

There are some bugs in frontend, I have touble to get env values through a SPA.

So just test with postman(GET and PUT)

## Deploy

The skeleton is deployed in a kubernetes cluster with 2 nodes.

I have config the logging, tracing and monitoring features through istio, however, I have not exposed them to public which means the UI can only be accessed in my local env, I will expose them later.
