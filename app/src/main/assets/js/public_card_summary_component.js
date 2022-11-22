
const cardTemplate = document.createElement('template');

cardTemplate.innerHTML = `

<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/all.min.css">
<link rel="stylesheet" href="css/app.css">

 <div class="card mx-0 mb-3 p-1 bg-light">
        <div class="row">
            <div class="col fw-bold align-self-center" id="txt_card_name">
                Card name
            </div>

            <div class="col-auto align-self-end pe-0 pb-1 text-primary fw-bold" id="txt_question_quantity">
                10
            </div>

            <div class="col-auto align-self-end px-1 pb-1">
                <img src="images/total_cards.png" class="img-total-cards">
            </div>

            <div class="col-auto ps-1 pe-2 text-center">
                <div class="dropdown" id="select_option">
                    <button type="button" class="btn dropdown py-0" id="btn_option" data-bs-toggle="dropdown">
                        <img src="" class="img-select-options-black img-fluid">
                    </button>

                    <ul class="dropdown-menu">
                        <li><a href="" class="dropdown-item" id="btn_study_card">Estudar baralho</a></li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col" id="txt_card_description">
                Teste descrição baralho, para ver como o texto vai ficar no card.
            </div>
        </div>
 </div>

<script src="js/bootstrap.bundle.min.js"></script>

`;


class PublicCardSummaryComponent extends HTMLElement{

    static get observedAttributes() { return ["card_id", "card_name", "card_description", "question_quantity",]; };

    constructor(){

        super();
    }

    connectedCallback() {

        this.innerHTML = cardTemplate.innerHTML;
        defineComponentData(this);
        let cardId = this.getAttribute("card_id");

        this.querySelector("#btn_study_card").addEventListener("click", function(event){

            event.preventDefault();
            window.history.back();

            PublicCardsActivity.AddToStudyingCards(cardId);
        });

    }

    attributeChangedCallback(name, oldValue, newValue) {

    updateComponentAttribute(this, name, newValue);
    }

}

function defineComponentData(element){

    element.querySelector("#txt_card_name").innerHTML =  element.getAttribute("card_name");
    element.querySelector("#txt_question_quantity").innerHTML =  element.getAttribute("question_quantity");
    element.querySelector("#txt_card_description").innerHTML =  element.getAttribute("card_description");

}

function updateComponentAttribute(element, attributeName, attributeNewValue){

    let componentId = "#txt_" + attributeName;
    let elementToUpdate = element.querySelector(componentId);

    if (elementToUpdate != null){
        element.querySelector(componentId).innerHTML =  attributeNewValue;
    }
}


customElements.define('public-card-summary', PublicCardSummaryComponent);


