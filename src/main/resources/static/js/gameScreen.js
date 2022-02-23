
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

function handleLetterEntered(event){

    if(wordleGridActive){

        if(event.key === "Enter"){

            column = 0;


            let targetsArray = []


            for (let i = 1; i <= wordLength; i++){
                targetsArray.push("#row" + row + "column" + i)
            }

            anime({
                targets: targetsArray,
                direction: "normal",
                easing: 'easeInOutSine',
                delay: function (el, i, l){
                    return i * 100;
                },
                duration: 500,
                background: "#409E51",
                rotate: '1turn'
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

function tickTimer(){

   let newNumber =  parseInt($("#timerText").text()) - 1

    $("#timerText").text(newNumber.toString())

  }




function hideAll(){
    $("#mainGameArea").children().hide()
}

function showWaitingScreen(){
    $("#Waiting").show();
}





