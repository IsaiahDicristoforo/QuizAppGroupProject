var currentQuestionId  = 0

var wordleLength = 0;

let playerName = ""

$(document).ready(function(){

    $("#sabotageDiv").hide()
    $("#powerupDiv").hide()

    $("#joinGame").click(function (){
        connect();
    })

});

function connect() {
    var socket = new SockJS('/chat');
    let stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {

        stompClient.subscribe('/game1/messages/' + $("#gameCode").text() , function(messageOutput) {
            let newPlayerName = JSON.parse(messageOutput.body)["playerName"];
            $("#leaderboardTableBody").append("<tr><th scope=\"row\">0</th>    " +
                "<td>" + newPlayerName + "</td>" +
                "<td><span class=\"badge bg-primary rounded-pill\">0</span></td></tr>")
        });


        playerName = $("#playerUserNameSelection").val()

        stompClient.send("/app/chat", {}, JSON.stringify({'playerName': playerName , 'gameId': $("#gameCode").text()  }));

        var newSocket = new SockJS('/chat1');
        let newStomClient = Stomp.over(newSocket);

        newStomClient.connect({}, function(frame) {


            newStomClient.subscribe("/game1/gameOver/3", function(messageOutput){
                finalStandings = messageOutput.body
               endGame()

            })

            newStomClient.subscribe('/game1/newQuestion/' + $("#gameCode").text() , function(messageOutput) {
                let newQuestionDetails = JSON.parse(messageOutput.body)
                currentQuestionId = newQuestionDetails.questionId
                createGrid(newQuestionDetails.wordleLength, newQuestionDetails.totalGuesses)
                $("#timerText").text(newQuestionDetails.wordleTimeLimit)
                clearInterval(interval)
                interval  = setInterval(tickTimer, 1000)
                totalAllowedGuesses = newQuestionDetails.totalGuesses

                $("#sabotageDiv").show()
                $("#powerupDiv").show()

                wordleLength = newQuestionDetails.wordleLength;
                wordLength = wordleLength

            });


        });

        var newSocket3 = new SockJS('/chat1');
        let newStomClient3 = Stomp.over(newSocket3);

        newStomClient3.connect({}, function(frame) {

            newStomClient3.subscribe("/game1/roundOver/" + $("#gameCode").text(), function(messageOutput){

                anime.remove($("#wordleGridContainer").get())

                $("#wordleGridContainer").css("transform", "")
                $("#wordleGridContainer").css("opacity", "100%")


                $("#sabotageDiv").hide()
                $("#powerupDiv").hide()

                displayLeaderboard(JSON.parse(messageOutput.body))

            });


        })
        var sabotageSocket = new SockJS('/playerSabotage');
        let sabotageClient = Stomp.over(sabotageSocket);

        sabotageClient.connect({}, function(frame){
            let subscribeText = '/game1/' + $("#gameCode").text() + '/' + playerName + '/sabotage/'
            sabotageClient.subscribe(subscribeText, function (messageOutput){
                let output = JSON.parse(messageOutput.body)
                startSabotageNotificationAnimation(output.saboteur, output.sabotageType, $("#gameEventNotificationScreen").get())
            })
        })
    });


}

