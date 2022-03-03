function hostGame(id){

    let quizId = id
    console.log(id)
        $.post({
            url: "/games/newGame/" + quizId,
        }, function (data){

            console.log(data)

            window.location.href = "/gameView/host/" + data.gameCode

        })
}