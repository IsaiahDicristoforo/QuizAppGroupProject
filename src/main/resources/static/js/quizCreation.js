var quizQuestions = [];
var question;
var counterValue = 0;
var questionNumber = 1;

function quizAttributes(){
    document.getElementById('myQuiz').readOnly=true;
    document.getElementById('quizDescription').disabled=true;
    document.getElementById('yes').disabled=true;
    document.getElementById('no').disabled=true;
    document.getElementById('question').removeAttribute('readonly');
    document.getElementById('hint').removeAttribute('readonly');
    document.getElementById('hint2').removeAttribute('readonly');
    document.getElementById('createQuiz').removeAttribute('disabled');
    document.getElementById('timeLimit').removeAttribute('readonly');
    document.getElementById('allowedGuesses').removeAttribute('readonly');
    document.getElementById('save').disabled=true;
}

function addQuestions(){

    let questionValue = document.getElementById('question').value;


    let hintValue = document.getElementById('hint').value;
    let secondHintValue = document.getElementById('hint2').value;
    let hints = [{"hint": hintValue}, {"hint": secondHintValue}]
    hints = hints.filter(hint => hint["hint"].trim().length > 0)



    let guessesAllowed = document.getElementById("allowedGuesses").value
    let timeLimit = document.getElementById("timeLimit").value

    question = questionValue
    quizQuestions.push({"wordle": questionValue, "questionTimeLimitSeconds": timeLimit, "totalGuessesAllowed": guessesAllowed, "hints": hints});

    document.getElementById('questionCreation').reset();
    console.log(quizQuestions);
    return false;
}

function updateCounter(){
    counterValue++;
    if (counterValue == 1){
        document.getElementById('numOfQuestions').innerHTML = "Total Questions: " + counterValue
    }
    else{
        document.getElementById('numOfQuestions').innerHTML = "Total Questions: " + counterValue
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
        let theQuizDescription = $("#quizDescription").val();
        $.post({
            url: "/quiz",
            contentType: "application/json",
            data: JSON.stringify({quizName: theQuizTitle, description: theQuizDescription})
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
        window.location.href = "/quizzes"
    }
}