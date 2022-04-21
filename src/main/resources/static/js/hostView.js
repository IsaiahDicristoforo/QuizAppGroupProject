
let currentQuestionNumber = 0;
let roundInProgress = false
$(document).ready(function(){

    connect()

    $("#joinGameLink").append("Go To: " + window.location.host + " and enter the game code...")
})


function connect() {
    var socket = new SockJS('/chat');
    let stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {

        $("#nextQuestion").click(function(){
            roundInProgress = true
            $("#nextQuestion").hide()
            stompClient.send("/app/chat1/nextQuestion", {}, JSON.stringify({ "gameId": $("#hostScreenGameCode").text()  }));
        })

        stompClient.subscribe('/game1/messages/' + $("#hostScreenGameCode").text() , function(messageOutput) {
            let playerName = JSON.parse(messageOutput.body)["playerName"];
            $(".playing").append("<li id=\"" + playerName +  "\" class=\"list-group-item d-flex justify-content-between align-items-center\">" + playerName +  "</li>")
        });

        stompClient.subscribe('/game1/gameOver/' + $("#hostScreenGameCode").text(), function(messageOutput){
            $("#nextQuestion").show()
            $("#nextQuestion").text("View Final Results")
            $("#nextQuestion").off('click');
            $("#nextQuestion").attr('id', "ViewResults")
            $("#ViewResults").click(function (){

                stompClient.send("/app/gameOver", {},  $("#hostScreenGameCode").text());

            })


        })

    });

    var newSocket = new SockJS('/chat1');
    let newStomClient = Stomp.over(newSocket);

    newStomClient.connect({}, function(frame) {

        newStomClient.subscribe("/game1/roundOver/" + $("#hostScreenGameCode").text(), function(messageOutput){

            roundInProgress = false
            $("#nextQuestion").show()
            $(".complete").empty()
            $(".playing").children("*").css("visibility", "visible")

        });


        })

    var socket2 = new SockJS('/playerUpdate');
    let stompClient2 = Stomp.over(socket2);

    stompClient2.connect({}, function(frame){
        stompClient2.subscribe('/game1/playerUpdate/'+$("#hostScreenGameCode").text(), function (messageOutput){
            let name = JSON.parse(messageOutput.body)["playerUsername"];
            el = $("#"+name)
            el.css("visibility", "hidden")

            if(roundInProgress){
                $(".complete").append("<li id=\"" + name +  "\" class=\"list-group-item d-flex justify-content-between align-items-center\">" + name + "</li>")
            }


        })
            })

}
function nextQuestionClicked(){
    $("#complete").empty()
    currentQuestionNumber += 1
    $("#questionCount").text("Question " + currentQuestionNumber + " / " + totalQuestions)
}


