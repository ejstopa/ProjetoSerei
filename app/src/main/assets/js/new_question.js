window.addEventListener("load", function(){
    updateLabelQuestionAction();
});

document.querySelector("#form_new_question").addEventListener("submit", function(event){

    event.preventDefault();
    window.history.back();

    let txtQuestion = document.querySelector("#txt_question").value;
    let txtAnswer = document.querySelector("#txt_answer").value;

    NewQuestionActivity.FinishSetResult(txtQuestion, txtAnswer);

});

document.querySelector("#btn_cancel").addEventListener("click", function(event){

    NewQuestionActivity.CloseActivity();

});

document.querySelector("#btn_open_image").addEventListener("click", function(){

    let filePath = NewQuestionActivity.OpenFile();

});

function updateQuestionImageName(imagePath){

    let startIndex = imagePath.lastIndexOf("/") + 1;
    let fileName =  startIndex > 1? imagePath.substring(startIndex) : imagePath

    document.querySelector("#txt_image_path").placeholder = fileName;
}

function loadFields(editQuestionDataJson){

    let editQuestionData = JSON.parse(editQuestionDataJson);

    document.querySelector("#txt_question").innerHTML = editQuestionData.question;
    document.querySelector("#txt_answer").innerHTML = editQuestionData.answer;
}

function updateLabelQuestionAction(){

    if (NewQuestionActivity.GetEditQuestion()){
        document.querySelector("#label_question_action").innerHTML = "Editar Pergunta";
    }
    else{
        document.querySelector("#label_question_action").innerHTML = "Nova Pergunta";
    }
}