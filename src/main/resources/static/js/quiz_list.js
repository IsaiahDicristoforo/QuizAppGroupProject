function hostGame(id){

    let quizId = id
    console.log(id)
        $.post({
            url: "/games/newGame/" + quizId,
        }, function (data){
            window.location.href = "/gameView/host/" + data.gameCode
        })
}

function deleteQuiz(id){

    $.ajax({
        type: "DELETE",
        url: "/quiz/" + id,
        success: function(data){
            window.location.href = window.location.href;
        }
    })

}