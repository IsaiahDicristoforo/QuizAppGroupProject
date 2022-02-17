
let row = 1;
let column = 0;

$(document).ready(function(){

    $.get("/")

    displayWaitingScreen()


    $(document).on('keydown', (event) => {

        handleLetterEntered(event);

    })

    $("#joinGame").click(function (){
        $('#myModal').modal('hide');

        connect();
    })

})


$(window).on('load', function() {
    $('#myModal').modal('show');
});

function handleLetterEntered(event){

    if(event.key === "Enter"){
        row++;
        column = 0;

        let targetsArray = ["#row1column1", '#row1column2', '#row1column3', '#row1column4', '#row1column5']

        let colors =  ["#d1ccd8", '#409E51', '#D9D426' ]

        let color = Math.floor(Math.random() * 11);

        anime({
            targets: targetsArray,
            direction: "normal",
            easing: 'easeInOutSine',
            delay: function (el, i, l){
                return i * 100;
            },
            duration: 500,
            rotate: '1turn'
        })
    }
    else if(event.key === "Backspace"){

        $("#row" + row + "column" + column).empty()
        column--;

    }else if(event.keyCode >= 60 && event.keyCode <= 90){ //Checking to see if the user enters a letter.

        column++;

        $("#row" + row + "column" + column).text(event.key.toUpperCase())

        anime({
            targets:  $("#row" + row + "column" + column).get(),
            scale: ["100%", "120%"],
            border: ["1px solid white", "1px solid #1bba3d"],
            direction: "alternate",
            easing: 'easeInOutSine',
            duration: 250

        })
    }




}
function createGrid(wordLength, totalGuesses){

    $("#Correct").hide();
    $("#Incorrect").hide();
    $("#Waiting").hide();
    row = 1;
    column = 0;
    $("#wordleGridContainer").empty();  
    $("#wordleGridContainer").show();
    $("#wordleGridContainer").css("grid-template-columns", "repeat(" + wordLength + ", 0.062fr")

    for(let i = 1; i  <= totalGuesses; i++){

        for(let j = 1; j <= wordLength; j++){
            let newElement = document.createElement("div");
            newElement.innerHTML = "&nbsp"
            newElement.classList.add("letter");

            newElement.id = ("row" + i + "column" + j);


            anime({
                targets: newElement,
                opacity: ['0','1'],
                duration: 700,
                scale: ["0%", "100%"],
                easing: 'easeInOutSine'
            })

            document.getElementById("wordleGridContainer").appendChild(newElement)
        }

    }


}


function connect() {
    var socket = new SockJS('/chat');
    let stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {

            stompClient.subscribe('/game1/messages', function(messageOutput) {
                let playerName = JSON.parse(messageOutput.body)["playerName"];
                $("#playerList").append("<li class=\"list-group-item d-flex justify-content-between align-items-center\">" + playerName +  "<span class=\"badge bg-primary rounded-pill\">0</span></li>")
            });

            stompClient.subscribe('/game1/gameUpdate', function(messageOutput) {
                console.log(messageOutput)
            });

            stompClient.send("/app/chat", {}, JSON.stringify({'playerName': $("#playerUserNameSelection").val(), 'gameId': $("#gameCode").text()  }));



            var newSocket = new SockJS('/chat1');
            let newStomClient = Stomp.over(newSocket);

            newStomClient.connect({}, function(frame) {

                newStomClient.subscribe('/game1/newQuestion', function(messageOutput) {
                    createGrid(Math.floor(Math.random() * 10),5)
                });
                });
            });
        }




        function displayWaitingScreen(){
            $("#wordleGridContainer").hide();
            $("#Correct").hide();
            $("#Incorrect").hide();
            $("#Waiting").show();
        }



function displayWaitingScreen(){
    $("#wordleGridContainer").hide();
    $("#Correct").hide();
    $("#Incorrect").hide();
    $("#Waiting").show();
}

function displayCorrectScreen(){
    $("#wordleGridContainer").hide();
    $("#Correct").show();
    $("#Incorrect").hide();
    $("#Waiting").hide();
}


function displayIncorrectScreen(){
    $("#wordleGridContainer").hide();
    $("#Correct").hide();
    $("#Incorrect").show();
    $("#Waiting").hide();
}




