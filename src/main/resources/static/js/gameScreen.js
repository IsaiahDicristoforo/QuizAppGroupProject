
let row = 1;
let column = 0;

$(document).ready(function(){

    createGrid(5, 5)

    $(document).on('keydown', (event) => {

        handleLetterEntered(event);

    })


})

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

function nextWordle(){
    createGrid($("#word").val().length, $("#totalGuesses").val())
}



