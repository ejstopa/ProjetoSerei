window.addEventListener("load", function(){
    updateCategoryOptions();
    updateCards();
});


document.querySelector("#btn_my_cards").addEventListener("click", function(){

    PublicCardsActivity.ShowStudyingCardsActivity();
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

function updateCards(){

   let element = document.createElement("public-card-summary");
   element.setAttribute("own_card", false);
   element.setAttribute("card_id", "");
   element.setAttribute("card_name", "Card Name");
   element.setAttribute("question_quantity", 10);
   element.setAttribute("target_questions", 10);

   studyingCardsContainer.appendChild(element);
}



