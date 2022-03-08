
let row = 1;
let column = 0;
let wordLength = 8;
let wordleGridActive = false;
let interval = null;


$(document).on('keydown', (event) => {handleLetterEntered(event);})

$(document).ready(function(){

    hideAll()
    showWaitingScreen()

    wordleGridActive = false;

    $.get("/")

    $("#joinGame").click(function (){
        $('#myModal').modal('hide');
        wordleGridActive = true;
    })

    anime({
        targets: $("#timer").get(),
        easing: 'linear',
        direction: "alternate",
        duration: 2000,
        scale: ["125%", "100%"],
        loop: true
    })

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


function getLetterFlipAnimation(target, color){
   return {
        targets: target,
        direction: "normal", easing: 'easeInOutSine',
        duration: 300,
        background: color,
        rotate: '1turn'

    }
}

function handleLetterEntered(event){

    if(wordleGridActive){

        if(event.key === "Enter"){

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

                let guessResults = data.guessResults
                let animeTimeline1 = anime.timeline({autoplay: false, duration: 500});


                for(let i = 0; i < targetsArray.length; i++){

                    let color = ""

                    if(guessResults[i] == "Correct"){
                        color = "#65c465"
                    }else if(guessResults[i] == "WrongLocation"){
                        color = "#FFD700"
                    }else{
                        color = "#c7c9c1"
                    }
                    animeTimeline1.add(getLetterFlipAnimation(targetsArray[i], color))
                }

                animeTimeline1.play()


            })

            row++;

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

function rotateGridSabotage(){

    anime({
        targets: $("#wordleGridContainer").get(),
        duration: 5000,
        easing: "linear",
        rotate: '1turn',
        loop: true
    })
}

function tickTimer(){

   let newNumber =  parseInt($("#timerText").text()) - 1

    if(newNumber == 0){
        doneWithQuestion();
        clearInterval(interval)
    }

    $("#timerText").text(newNumber.toString())
  }


  function doneWithQuestion(){
    hideAll();
    showCorrectScreen();
  }


function hideAll(){
    $("#mainGameArea").children().hide()
}

function showWaitingScreen(){
    $("#Waiting").show();
}

function showCorrectScreen(){
    $("#Correct").show()
}





