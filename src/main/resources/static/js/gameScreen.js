
let row = 1;
let column = 0;
let wordLength = 8;
let wordleGridActive = false;
let interval = null;
let totalAllowedGuesses = 0
let finalStandings = null

$(document).on('keydown', (event) => {handleKeyPressEvent(event);})

$(document).ready(function(){

    hideAll()
    showWaitingScreen()

    wordleGridActive = false;

    $.get("/")

    $("#joinGame").click(function (){
        $('#myModal').modal('hide');
        wordleGridActive = true;
    })

    startTimerAnimation($("#timer").get())

})


$(window).on('load', function() {
    $('#myModal').modal('show');
});

function getGuess(rowNumber, wordLength){
    let guess = "";

    for(let i = 1; i <= wordLength; i++){
        guess += $("#row" + rowNumber + "column" + i).text().toString().toLowerCase()

    }
    return guess;
}



function handleKeyPressEvent(event){

    if(wordleGridActive){

        if(event.key === "Enter"){
         wordSubmitted()
        }
        else if(event.key === "Backspace"){

         backspacePressed()

        }else if(event.keyCode >= 60 && event.keyCode <= 90){ //Checking to see if the user enters a letter.
            letterEntered()
        }
    }
}

function letterEntered(){
    column++;
    $("#row" + row + "column" + column).text(event.key.toUpperCase())
    let targetLetter = $("#row" + row + "column" + column).get()
    startLetterEnteredAnimation(targetLetter)
}

function backspacePressed(){
    $("#row" + row + "column" + column).empty()
    column--;
}
function wordSubmitted(){
    column = 0;
    let targetsArray = []
    for (let i = 1; i <= wordLength; i++){
        targetsArray.push("#row" + row + "column" + i)
    }

    $.post({
        url: "/games/checkGuess",
        contentType: "application/json",
        data: JSON.stringify({guess: getGuess(row, wordLength), questionId: currentQuestionId, gameCode: $("#gameCode").text(), playerName: playerName})
    }, function(data){

        let guessResults = data.guessResults;
        let wordCorrect = data.wordCorrect
        let rotateLetterAnimation = anime.timeline({
            autoplay: false,
            easing: 'easeInOutQuad',
        })
        for(let i = 0; i < targetsArray.length; i++){

            let color = ""

            if(guessResults[i] == "Correct"){
                color = "#65c465"
            }else if(guessResults[i] == "WrongLocation"){
                color = "#FFD700"
            }else{
                color = "#83867c"
            }
            rotateLetterAnimation.add({'targets': targetsArray[i],border: "0px solid white", rotate: '1turn', easing: 'easeInOutSine', 'background': color}, '-=500')

        }
            rotateLetterAnimation.play()

        if(wordCorrect){
            stopTimer()
            rotateLetterAnimation.complete = function(anim){

              let gridAnimation =  {
                    targets: '#wordleGridContainer .letter',
                    scale: [
                        {value: .1, easing: 'easeOutSine', duration: 500},
                        {value: 1, easing: 'easeInOutQuad', duration: 1200}
                    ],
                    opacity: [1,0],
                    delay: anime.stagger(200, {grid: [totalAllowedGuesses, wordLength], from: 'center'})

                };
                gridAnimation.complete = function(anim){
                    hideAll()
                    showCorrectScreen()
                    anime({
                        targets: '#Correct',
                        easing: 'spring(1, 80, 10, 0)',
                        scale: [0,1],

                    });

                    anime({
                        targets: "#pointsEarned",
                        value: [0, 1000],
                        round: 1,
                        easing: 'easeInOutExpo'
                    })
                }
                anime(gridAnimation)

            }


        }
        else if(!wordCorrect && row > totalAllowedGuesses){
            rotateLetterAnimation.complete = function(anim){
                hideAll()
                showFailScreen()
                stopTimer()
            }
        }
        rotateLetterAnimation.play()

    })

    row++;
}


function createGrid(wordLength, totalGuesses){

    $("#Correct").hide();
    $("#Incorrect").hide();
    $("#Waiting").hide();
    $("#wordleGridContainer").show()
    row = 1;
    column = 0;
    $("#wordleGridContainer").empty();  
    $("#wordleGridContainer").css("grid-template-columns", "repeat(" + wordLength + ", 0.062fr")

    for(let i = 1; i  <= totalGuesses; i++){

        for(let j = 1; j <= wordLength; j++){
            let newElement = document.createElement("div");
            newElement.innerHTML = "&nbsp"
            newElement.classList.add("letter");
            newElement.id = ("row" + i + "column" + j);

            fadeInAnimation(newElement)

            document.getElementById("wordleGridContainer").appendChild(newElement)
        }

    }


}


function tickTimer(){

   let newNumber =  parseInt($("#timerText").text()) - 1

    if(newNumber == 0){
        doneWithQuestion();
        stopTimer()
    }

    $("#timerText").text(newNumber.toString())
  }

  function stopTimer(){
    clearInterval(interval)
}



  function doneWithQuestion(){
    hideAll();
    showFailScreen();
    $.post({
        url: "/games/" + $("#gameCode").text() + "/timeUp?playerName=" + playerName,
    }, function(data){

    })


  }

  function displayLeaderboard(players){

    $("#leaderboardTableBody").empty()

      let counter = 1
    players.forEach(player => {
        $("#leaderboardTableBody").append("<tr> <th scope=\"row\">" + counter + "</th> <td>" + player.playerUsername  + "</td> <td><span className=\"badge bg-primary rounded-pill\">" + player.totalPoints +  "</span></td> </tr>");
        counter++
    });
  }


function hideAll(){
    $("#mainGameArea").children().hide()
    $("#resultsScreen").hide()
}

function showWaitingScreen(){
    $("#Waiting").show();
}

function showCorrectScreen(){
    $("#Correct").show()
}

function showFailScreen(){
    $("#Incorrect").show()
}

function endGame(){

    $("#resultsScreen").children().hide()

    anime({
        targets: "#mainGameScreen",
        opacity: [1, 0],
        easing: 'linear',
        background: "#030000",
        duration: 1000
    }).complete = function(){
        $("#mainGameScreen").hide()
        $("#resultsScreen").show()
        anime({
            targets: "#resultsScreen",
            background: "#030000",
            easing: 'linear',
            opacity: [0, 1],
            duration: 2000
        }).complete = function (){

            $("#resultsScreen").children().show()


            var textWrapper = document.querySelector('.ml6 .letters');
            textWrapper.innerHTML = textWrapper.textContent.replace(/\S/g, "<span class='resultLetter'>$&</span>");

            anime.timeline({loop: false})
                .add({
                    targets: '.ml6 .resultLetter',
                    translateY: ["1.1em", 0],
                    translateZ: 0,
                    duration: 750,
                    delay: (el, i) => 50 * i
                }).add({
                targets: '.ml6',
                duration: 1000,
                easing: "easeOutExpo",
                delay: 1000
            }).complete = function (){

                $("#resultsScreen").append("<h1 style='color: white'>Final Standings</h1>")

                JSON.parse(finalStandings).forEach(player => {
                    $("#resultsScreen").append("<div style='color: white'>" + player.playerUsername + "  " + player.totalPoints +  "</div>")

                })
            };
        }
    }





}


