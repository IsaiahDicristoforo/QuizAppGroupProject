var currentQuestionId  = 0

let playerName = ""

$(document).ready(function(){


    $("#joinGame").click(function (){
        connect();
    })

});

function connect() {
    var socket = new SockJS('/chat');
    let stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {

        stompClient.subscribe('/game1/messages/' + $("#gameCode").text() , function(messageOutput) {
             newPlayerName = JSON.parse(messageOutput.body)["playerName"];
            $("#playerList").append("<li class=\"list-group-item d-flex justify-content-between align-items-center\">" + newPlayerName +  "<span class=\"badge bg-primary rounded-pill\">0</span></li>")

        });


        playerName = $("#playerUserNameSelection").val()

        stompClient.send("/app/chat", {}, JSON.stringify({'playerName': playerName , 'gameId': $("#gameCode").text()  }));

        var newSocket = new SockJS('/chat1');
        let newStomClient = Stomp.over(newSocket);

        newStomClient.connect({}, function(frame) {

            newStomClient.subscribe('/game1/newQuestion/' + $("#gameCode").text() , function(messageOutput) {
                let newQuestionDetails = JSON.parse(messageOutput.body)
                currentQuestionId = newQuestionDetails.questionId
                createGrid(newQuestionDetails.wordleLength, newQuestionDetails.totalGuesses)
                $("#timerText").text(newQuestionDetails.wordleTimeLimit)
                clearInterval(interval)
                interval  = setInterval(tickTimer, 1000)
                totalAllowedGuesses = newQuestionDetails.totalGuesses
            });
        });
    });
}

