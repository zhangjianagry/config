FROM node:12-slim

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY server.js ./

EXPOSE 9080

CMD [ "node", "server.js" ]
