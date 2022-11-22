window.addEventListener("load", function(){
    updateCategoryOptions();
    updateCards();
});


document.querySelector("#btn_my_cards").addEventListener("click", function(){

    PublicCardsActivity.ShowStudyingCardsActivity();
});

document.querySelector("#select_category").addEventListener("change", function(event){

    updateSelectCategoryStyle();

    let category = event.target.selectedOptions[0].innerText;
    let filteredCardsMapJson = PublicCardsActivity.GetCardsMapByCategoryJson(category, 20);
    let filteredCardsMap = JSON.parse(filteredCardsMapJson);

    if ( Object.keys(filteredCardsMap).length > 0){
        updateCards(filteredCardsMap);
    }
    else{
        document.querySelector("#public_cards_container").classList.add("text-black-50");
        document.querySelector("#public_cards_container").innerHTML = "Nenhum resultado encontrado."
    }

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

function updateCards(filteredCardsMap){

   document.querySelector("#public_cards_container").classList.remove("text-black-50");
   let publicCardsContainer = document.querySelector("#public_cards_container");
   publicCardsContainer.innerHTML = "";

    for (key in filteredCardsMap){

        let element = document.createElement("public-card-summary");
        element.setAttribute("card_id", key);
        element.setAttribute("card_name", filteredCardsMap[key].Name);
        element.setAttribute("question_quantity", filteredCardsMap[key].QuestionQuantity);
        element.setAttribute("card_description", filteredCardsMap[key].Description);

        publicCardsContainer.appendChild(element);
    }

}

function updateSelectCategoryStyle(){

    let selectCategory = document.querySelector("#select_category");

       if (selectCategory.value != ""){
             selectCategory.classList.remove("select-category-new-card");
        }
}



