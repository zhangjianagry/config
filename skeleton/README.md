# skeleton

An extremely simple config skeleton by WebSocket implementation( may not suit ) and kubernetes deployment.
Well, I met some troubles when accessing the mogoDB server and I cannot understand some code with no docs in the project, so I code some simple module by Node.js for a convenient deployment.

## Basic

Here is the the skeleton.
Since all the module is nearly a placeholder for deployment, I only implement the basic feature.

## Service

RESTful server response to frontend's 'GET' and 'PUT' requests
WebSocket server to communicate with client
Request to auth for authorization

## Auth

Auth's module do not exist in this project, so I don't know what your auth exactly do.
I guess to auth the customer when they try to connect?

If the secret is 'VT' return true.

## Customer

Customer is the demo customer app, although the change is immediately passed to the client. These changes need to be viewed when demonstrating. I use polling with 5 seconds' interval for presentation.

## Client

Client module is not in this project either. We should not let service to register their configuration through hard-code, we need a client to encapsulating related code and publish to their Language's marketplace.

I implement a Node.js environment's client and publish on [npm](https://www.npmjs.com/package/vt-config-toy-client)

> more in comments
