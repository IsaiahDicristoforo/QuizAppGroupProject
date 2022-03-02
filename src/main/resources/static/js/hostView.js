
$(document).ready(function(){

    connect()


})


function connect() {
    var socket = new SockJS('/chat');
    let stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {

        $("#nextQuestion").click(function(){
            stompClient.send("/app/chat1", {}, JSON.stringify({ "gameId": $("#hostScreenGameCode").text()  }));
        })

    });


}
