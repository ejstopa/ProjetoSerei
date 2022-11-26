let playingQuestion;
let rightAnswers = 0;
let wrongAnswers = 0;
let speakQuestionOn = true;

window.addEventListener("load", function(){

    setCardInformation();
    setTargetQuestionsInformation();
    setPlayingQuestion();
    setQuestionInformation();
});


document.querySelector("#btn_turn_question").addEventListener("click", function(event){

    let questionCardInner = document.querySelector("#card_question_inner_container");
    questionCardInner.classList.add("flip-card");

    document.querySelector("#btn_turn_question").hidden = true;
    document.querySelector("#btn_right_answer").hidden = false;
    document.querySelector("#btn_wrong_answer").hidden = false;

    setAnswerInformation();
});

document.querySelector("#btn_pause_card").addEventListener("click", function(){

    finishStudying();
});

document.querySelector("#btn_speak_question").addEventListener("click", function(event){

    let btnSpeakQuestion = event.target;

    if (speakQuestionOn){
        speakQuestionOn = false;
        btnSpeakQuestion.src = "images/volume_off.png"
    }
    else{
        speakQuestionOn = true;
        btnSpeakQuestion.src = "images/volume_up.png"
    }
});

document.querySelector("#btn_card_finished").addEventListener("click", function(){

    finishStudying();
});

document.querySelector("#btn_right_answer").addEventListener("click", function(){

    rightAnswers ++;

    if (MainActivity.SetNextQuestion(true)){

        let questionCardInner = document.querySelector("#card_question_inner_container");

        document.querySelector("#btn_turn_question").hidden = false;
        document.querySelector("#btn_right_answer").hidden = true;
        document.querySelector("#btn_wrong_answer").hidden = true;

        questionCardInner.classList.remove("question-card-inner");
        questionCardInner.classList.remove("flip-card");
        setTimeout(function(){
            questionCardInner.classList.add("question-card-inner");
        },100)

        setTargetQuestionsInformation();
        setPlayingQuestion();
        setQuestionInformation();
    }
    else{
        showCardFinishedMessage();
    }

});

document.querySelector("#btn_wrong_answer").addEventListener("click", function(){

    wrongAnswers ++;

    if (MainActivity.SetNextQuestion(false)){

        let questionCardInner = document.querySelector("#card_question_inner_container");

        document.querySelector("#btn_turn_question").hidden = false;
        document.querySelector("#btn_right_answer").hidden = true;
        document.querySelector("#btn_wrong_answer").hidden = true;

        questionCardInner.classList.remove("question-card-inner");
        questionCardInner.classList.remove("flip-card");
        setTimeout(function(){
            questionCardInner.classList.add("question-card-inner");
        },100)

        setTargetQuestionsInformation();
        setPlayingQuestion();
        setQuestionInformation();
    }
    else{
        showCardFinishedMessage();
    }

});

function setCardInformation(){

    let studyingCard = JSON.parse(MainActivity.GetPlayingCardJson());
    let imgLockerSrc = studyingCard.OwnCard? "images/lock.png" : "images/unlock.png";

    document.querySelector("#txt_card_name").innerHTML = studyingCard.Name;
    document.querySelector("#txt_question_quantity").innerHTML = studyingCard.QuestionQuantity;
    document.querySelector("#img_locker").src = imgLockerSrc;

}

function setTargetQuestionsInformation(){

    let targetQuestionsRemaining = MainActivity.GetTargetQuestionsRemaining();
    document.querySelector("#txt_target_questions").innerHTML = targetQuestionsRemaining;
}

function setPlayingQuestion(){

    playingQuestion = JSON.parse(MainActivity.GetPlayingQuestionDataJson());
}

function setQuestionInformation(){

    let questionNumber = String(MainActivity.GetPlayingQuestionIndex() + 1) + " - ";
    document.querySelector("#txt_question_number").innerHTML = questionNumber;
    document.querySelector("#txt_question").innerHTML = playingQuestion.Question;

    if (playingQuestion.QuestionImage != null){
        let imageBytesString = MainActivity.GetImageAsBytesDatabase(playingQuestion.QuestionImage);

        document.querySelector("#question_image").style.backgroundImage = "url(data:image/*;base64," + imageBytesString;
    }
    else{
        document.querySelector("#question_image").style.backgroundImage = "";
    }

    if (speakQuestionOn){
        if (speakQuestionOn){
            setTimeout(function(){
                MainActivity.SpeakText(playingQuestion.Question);
            },500)
        }
    }
}

function setAnswerInformation(){

    document.querySelector("#txt_answer").innerHTML = "<b>Resposta:</b>  " + playingQuestion.Answer;

        if (speakQuestionOn){
            setTimeout(function(){
                MainActivity.SpeakText(document.querySelector("#txt_answer").innerText);
            },1000)
        }
}

function showCardFinishedMessage(){

    document.querySelector("#txt_right_answers").innerHTML = "Acertos: " + rightAnswers;
    document.querySelector("#txt_wrong_answers").innerHTML = "Erros: " + wrongAnswers;
    document.querySelector("#dialog_card_finished").showModal();

    if (speakQuestionOn){
        let cardFinishMessage = `Parabéns!   Você terminou seu estudo diário,
                                 Seu número de acertos foi ${rightAnswers}, e o número de erros foi ${wrongAnswers}`;

        MainActivity.SpeakText(cardFinishMessage);
    }

}

function finishStudying(){

    MainActivity.UpdateStudyingCardDatabase();
    MainActivity.ShowStudyingCardsContent();

}




