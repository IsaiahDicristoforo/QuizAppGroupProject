var currentQuestionId  = 0

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
            let playerName = JSON.parse(messageOutput.body)["playerName"];
            $("#playerList").append("<li class=\"list-group-item d-flex justify-content-between align-items-center\">" + playerName +  "<span class=\"badge bg-primary rounded-pill\">0</span></li>")

        });


        stompClient.send("/app/chat", {}, JSON.stringify({'playerName': $("#playerUserNameSelection").val(), 'gameId': $("#gameCode").text()  }));



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

            });
        });
    });
}

