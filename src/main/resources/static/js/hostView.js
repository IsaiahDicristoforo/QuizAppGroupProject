
let currentQuestionNumber = 0;

$(document).ready(function(){

    connect()

    $("#joinGameLink").append(window.location.host + "/game/" + $("#hostScreenGameCode").text())
})


function connect() {
    var socket = new SockJS('/chat');
    let stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {

        $("#nextQuestion").click(function(){
            stompClient.send("/app/chat1", {}, JSON.stringify({ "gameId": $("#hostScreenGameCode").text()  }));
        })

        stompClient.subscribe('/game1/messages/' + $("#hostScreenGameCode").text() , function(messageOutput) {
            let playerName = JSON.parse(messageOutput.body)["playerName"];
            $(".playing").append("<li id=\"" + playerName +  "\" class=\"list-group-item d-flex justify-content-between align-items-center\">" + playerName +  "<span class=\"badge bg-primary rounded-pill\">0</span></li>")
        });
    });

    var socket2 = new SockJS('/playerUpdate');
    let stompClient2 = Stomp.over(socket2);

    stompClient2.connect({}, function(frame){
        stompClient2.subscribe('/game1/playerUpdate/'+$("#hostScreenGameCode").text(), function (messageOutput){
            let name = JSON.parse(messageOutput.body)["playerUsername"];
            $(".complete").append("<li class=\"list-group-item d-flex justify-content-between align-items-center\">" + name +  "<span class=\"badge bg-primary rounded-pill\">0</span></li>")
            $("#" + name).remove()

        })
    })
}
function nextQuestionClicked(){
    currentQuestionNumber += 1
    $("#questionCount").text("Question " + currentQuestionNumber + " / " + totalQuestions)
}


