$(document).ready(function () {

    $("#hostGame").click(function () {
        $.post("/games/newGame/1", function (data, status) { //Start a new game. Once the game code is generated, redirect the host to their game screen.
            window.location.href = "/gameView/host/" + data.gameCode
        })
    })

})