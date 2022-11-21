window.addEventListener("load", function(){

    updateCategoryOptions();
    loadFields();
    updateSelectCategoryStyle();

});

document.querySelector("#form_new_card").addEventListener("submit", function(event){

    event.preventDefault();
    window.history.back();

    let isQuestionListEmpty = NewCardActivity.isQuestionListEmpty();

    if (isQuestionListEmpty){
        NewCardActivity.MessageQuestionListEmpty();
        return;
    }

    let txtCardName = document.querySelector("#txt_card_name").value.trim();
    let txtCardDescription = document.querySelector("#txt_card_description").value.trim();
    let txtCardCategory = document.querySelector("#select_category").value.trim();
    let checkPublicCard =  document.querySelector("#check_public_card").checked;

    let cardId = NewCardActivity.getCardId();

    if (cardId == null){
        NewCardActivity.FinishSaveNewCard(txtCardName, txtCardDescription, txtCardCategory, checkPublicCard);
    }
    else{
        NewCardActivity.FinishSaveEditCard(cardId, txtCardName, txtCardDescription, txtCardCategory, checkPublicCard);
    }

});

document.querySelector("#btn_cancel").addEventListener("click", function(){

    NewCardActivity.CloseActivity();
});

document.querySelector("#btn_new_question").addEventListener("click", function(event){

    event.preventDefault();
    window.history.back();

    NewCardActivity.ShowNewQuestionActivity();

});

document.querySelector("#select_category").addEventListener("change", function(event){

    updateSelectCategoryStyle();
});


function updateCategoryOptions(){

    let selectCategory = document.querySelector("#select_category");
    let categories = JSON.parse(CategoryModel.GetAllCategoryNames());

    for (const category of categories) {

        let option = document.createElement("option");
        option.value = String(category);
        option.innerHTML = String(category);

        selectCategory.appendChild(option);
    }
}

function updateSelectCategoryStyle(){

    let selectCategory = document.querySelector("#select_category");

       if (selectCategory.value != ""){
             selectCategory.classList.remove("select-category-new-card");
        }

}

function loadFields(){

    let cardJson = NewCardActivity.GetEditingCardData();

    if (cardJson != null){

        let card = JSON.parse(cardJson);

        document.querySelector("#txt_card_name").value = card.Name;
        document.querySelector("#txt_card_description").innerHTML = card.Description;
        document.querySelector("#select_category").value = card.Category;
        document.querySelector("#check_public_card").checked = card.PublicCard;

        let questionsList = NewCardActivity.GetQuestionsList();

        if (questionsList != null){
            updateQuestions(questionsList);
        }
    }

}

function updateQuestions(questionListJson){

    let questionList = JSON.parse(questionListJson);
    let questionContainer = document.querySelector("#question_container");
    questionContainer.innerHTML = "";

    for (const question of questionList) {

        let questionNumber = questionList.indexOf(question) + 1;
        let questionId = "question_summary_" + String(questionNumber);

        let element = document.createElement("question-summary");
        element.setAttribute("question", question.Question);
        element.setAttribute("question_number", questionNumber);
        element.setAttribute("id", questionId);

        questionContainer.appendChild(element);
    }

}












