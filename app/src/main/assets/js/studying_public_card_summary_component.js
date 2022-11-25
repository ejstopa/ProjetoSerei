
const publicCardTemplate = document.createElement('template');

publicCardTemplate.innerHTML = `

<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/all.min.css">
<link rel="stylesheet" href="css/app.css">

 <div class="card mx-0 mb-2 p-1 bg-light">
        <div class="row">
            <div class="col-auto pb-2" id="card_public_locker">
                <img src="images/unlock.png" class="img-locker" id="img_locker">
            </div>

            <div class="col px-0 fw-bold align-self-center" id="txt_card_name">
                Card name
            </div>

            <div class="col-auto text-center">
                <div class="dropdown" id="select_option">
                    <button type="button" class="btn dropdown py-0" id="btn_option" data-bs-toggle="dropdown">
                        <img src="" class="img-select-options-black img-fluid">
                    </button>

                    <ul class="dropdown-menu">
                        <li><a href="" class="dropdown-item" id="btn_statistics">Ver Estatísticas</a></li>
                        <li><a href="" class="dropdown-item" id="btn_stop_studying_card">Deixar de estudar</a></li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col px-4 py-1">
                <img src="images/play.png" class="img-btn-play" id="btn_play_card">
            </div>

            <div class="col-auto align-self-end pe-0 pb-1 text-primary fw-bold" id="txt_question_quantity">
                10
            </div>

            <div class="col-auto align-self-end ps-1 pb-1">
                <img src="images/total_cards.png" class="img-total-cards">
            </div>

            <div class="col-auto align-self-end pe-0 pb-1 text-primary fw-bold" id="txt_target_quesitons">
                10
            </div>

            <div class="col-auto align-self-end ps-1">
                <img src="images/alvo.png" class="img-target" id="img_target_questions">
            </div>
        </div>
 </div>

<script src="js/bootstrap.bundle.min.js"></script>

`;


class StudyingPublicCardSummaryComponent extends HTMLElement{

    static get observedAttributes() { return ["card_id", "card_name", "question_quantity", "target_questions"]; };

    constructor(){

        super();
    }

    connectedCallback() {

        this.innerHTML = publicCardTemplate.innerHTML;
        defineComponentData(this);
        let cardId = this.getAttribute("card_id");

        this.querySelector("#btn_stop_studying_card").addEventListener("click", function(event){

            event.preventDefault();

            MainActivity.DeleteStudyingCardDatabase(cardId);
        });

        this.querySelector("#btn_play_card").addEventListener("click", function(event){

            event.preventDefault();
            window.history.back();

            MainActivity.ShowPlayCardContent(cardId);
        });

        this.querySelector("#img_target_questions").addEventListener("click", function(event){

            event.preventDefault();
            window.history.back();

            MainActivity.ResetStudyingCardTargetQuestionsDatabase(cardId);
        });

    }

    attributeChangedCallback(name, oldValue, newValue) {

    updateComponentAttribute(this, name, newValue);
    }

}

function defineComponentData(element){

    element.querySelector("#txt_card_name").innerHTML =  element.getAttribute("card_name");
    element.querySelector("#txt_question_quantity").innerHTML =  element.getAttribute("question_quantity");
    element.querySelector("#txt_target_quesitons").innerHTML =  element.getAttribute("target_questions");

}

function updateComponentAttribute(element, attributeName, attributeNewValue){

    let componentId = "#txt_" + attributeName;
    let elementToUpdate = element.querySelector(componentId);

    if (elementToUpdate != null){
        element.querySelector(componentId).innerHTML =  attributeNewValue;
    }
}


customElements.define('studying-public-card-summary', StudyingPublicCardSummaryComponent);


