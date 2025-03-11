# webflux-sse
### What is this?
This is a simple app using [Spring Webflux](https://docs.spring.io/spring-framework/reference/web/webflux.html) and Java 21 (Why 21? Fancy (✦ ‿ ✦)) to implement Server Sent Event (SSE) using simple publish and listener for chat.
![Diagram PNG](/src/main/resources/diagram.png)

### Prerequisites
Prepare your IDE / Text Editor, Java, Gradle, Mongodb

### How to?
1) Run this app first using your favorite IDE or whatever you want.
2) Listen the chat events `curl http://localhost:8080/chat`
3) Drop your chat e.g. `curl -X POST -H "Content-Type: application/json" -d '{"username": "zkcus", "message": "feelsoldman"}' http://localhost:8080/chat`
4) Boom, supposed to be not an error. ;)
![Example GIF](https://s8.ezgif.com/tmp/ezgif-83ae1f596c984b.gif)

### What to be improved?
Every aspect of these codes. 