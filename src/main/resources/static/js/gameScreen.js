
let row = 1;
let column = 1;

$(document).ready(function(){

    createGrid(5, 5)

    $(document).on('keydown', (event) => {

        if(event.key === "Backspace"){
            $("#row" + row + "column" + column).empty()
            column--;

        }else{
            $("#row" + row + "column" + column).text(event.key.toUpperCase())

            anime({
                targets:  $("#row" + row + "column" + column).get(),
                scale: ["100%", "120%"],
                direction: "alternate",
                easing: 'easeInOutSine',
                duration: 250

            })
            column++;


        }

    })


})



function createGrid(wordLength, totalGuesses){


    $("#wordleGridContainer").empty();
    $("#wordleGridContainer").css("grid-template-columns", "repeat(" + wordLength + ", 0.08fr")

    for(let i = 1; i  <= wordLength; i++){

        for(let j = 1; j <= totalGuesses; j++){
            let newElement = document.createElement("div");
            newElement.innerHTML = "&nbsp"
            newElement.classList.add("letter");
            newElement.id = ("row" + i + "column" + j);


            anime({
                targets: newElement,
                opacity: ['0','1'],
                duration: 2000,
            })

            document.getElementById("wordleGridContainer").appendChild(newElement)
        }

    }


}

function nextWordle(){
    createGrid($("#word").val().length, $("#totalGuesses").val())
    console.log($("#word").val())
}



