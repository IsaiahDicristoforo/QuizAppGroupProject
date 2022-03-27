var quizQuestions = [];
var question;
var counterValue = 0;
var questionNumber = 1;

function quizAttributes(){
    document.getElementById('myQuiz').readOnly=true;
    document.getElementById('yes').disabled=true;
    document.getElementById('no').disabled=true;
    document.getElementById('question').removeAttribute('readonly');
    document.getElementById('hint').removeAttribute('readonly');
    document.getElementById('createQuiz').removeAttribute('disabled');
    document.getElementById('timeLimit').removeAttribute('readonly');
    document.getElementById('allowedGuesses').removeAttribute('readonly');
    document.getElementById('save').disabled=true;
}

function addQuestions(){

    questionValue =  document.getElementById('question').value;
    hintValue = document.getElementById('hint').value;

    guessesAllowed = document.getElementById("allowedGuesses").value
    timeLimit = document.getElementById("timeLimit").value

    question = questionValue
    quizQuestions.push({"wordle": questionValue, "questionTimeLimitSeconds": timeLimit, "totalGuessesAllowed": guessesAllowed});

    document.getElementById('questionCreation').reset();
    console.log(quizQuestions);
    return false;
}

function updateCounter(){
    counterValue++;
    if (counterValue == 1){
        document.getElementById('numOfQuestions').innerHTML = "You have " + counterValue + " question added."
    }
    else{
        document.getElementById('numOfQuestions').innerHTML = "You have " + counterValue + " questions added."
    }
}

function updateQuestionCounter(){
    questionNumber++;
    document.getElementById('questionNumber').innerHTML = "Question " + questionNumber + ".";
}

function saveQuiz(){
    if (counterValue == 0){
        alert("Please add at least one question to create quiz.")
    }
    else{

        let theQuizTitle = $("#myQuiz").val();
        $.post({
            url: "/quiz",
            contentType: "application/json",
            data: JSON.stringify({quizName: theQuizTitle})
        }, function (data){
            let theQuizId = data["quizId"];

            quizQuestions = quizQuestions.map(quiz => ({...quiz, quizId: theQuizId}))

            $.post({
                url: "/questions",
                contentType: "application/json",
                data: JSON.stringify(quizQuestions)

            })

        })
        alert("Quiz Created!");
    }
}