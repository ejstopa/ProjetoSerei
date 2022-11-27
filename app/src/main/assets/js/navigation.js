
window.addEventListener("load", function(){

    updateCategoryOptions();
    updateUserText();

});

document.querySelector("#btn_logout").addEventListener("click", function(){
    AccountModel.Logout();
    MainActivity.ShowLoginActivity();
});

document.querySelector("#btn_new_card").addEventListener("click", function(){

    MainActivity.ShowNewCardActivity();
});

document.querySelector("#btn_public_cards").addEventListener("click", function(){

    MainActivity.ShowPublicCardsActivity();
});

document.querySelector("#select_category").addEventListener("change", function(event){

    let selectCategories = event.target;
    updateSelectCategoryWidth(selectCategories);

    MainActivity.FilterStudyingCards(selectCategories.selectedOptions[0].innerHTML);
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

function updateSelectCategoryWidth(selectCategories){

    let selectedOptions = selectCategories.selectedOptions;
    let selectedOption = selectedOptions[0];

    let option = document.createElement("option");
    option.innerHTML = selectedOption.innerHTML;
    option.selected = true;

    let selectTemp = document.createElement("select");
    selectTemp.classList.add("select-categories");
    selectTemp.id = "select_temp";
    selectTemp.appendChild(option);
    selectCategories.after(selectTemp);

    selectCategories.style.width = String(selectTemp.offsetWidth) + "px";

    selectTemp.remove();
}

function updateUserText(){

    let userName = AccountModel.GetUserName();
    let text = "Olá, " + userName;

    let textUser = document.querySelector("#txt_user");
    textUser.innerHTML = text;

}

function highLightBtnMarker(markerId){

    let markers = document.querySelectorAll(".btn-marker");
    for (const marker of markers) {
        marker.classList.remove("bg-primary");
    }

    let markerToHighLight = "#" + markerId;
    document.querySelector(markerToHighLight).classList.add("bg-primary");

}

function blockUI(){
    let dialogBlockUI =document.querySelector("#dialog_block_ui");
    dialogBlockUI.showModal()
}

function unblockUI(){
    let dialogBlockUI =document.querySelector("#dialog_block_ui");
    dialogBlockUI.close();
}







