
const cardTemplate = document.createElement('template');

cardTemplate.innerHTML = `

<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/all.min.css">
<link rel="stylesheet" href="css/app.css">

<div class="card mx-0 mb-1 p-1 bg-light">
    <div class="row">
        <div class="col-auto pe-0" id="question_number">
            ""
        </div>

        <div class="col-auto px-1">
            -         
        </div>

        <div class="col ps-0" id="question">             
            ""
        </div>

        <div class="col-auto text-center py-0">
            <div class="dropdown py-0" id="select_option">
                <button type="button" class="btn dropdown py-0" id="btn_option" data-bs-toggle="dropdown">
                    <img src="" class="img-select-options-black img-fluid pb-1">
                </button>

                <ul class="dropdown-menu">
                    <li><a href="" class="dropdown-item" id="btn_edit_question">Editar pergunta</a></li>
                    <li><a href="" class="dropdown-item" id="btn_delete_question">Excluir pergunta</a></li>
                </ul>
            </div>
        </div>
    </div>
   
</div>

<script src="js/bootstrap.bundle.min.js"></script>

`;

class QuestionSummaryCard extends HTMLElement{

    static get observedAttributes() { return ["id", "questionNumber", "question"]; }

    constructor(){

        super();

    }

    connectedCallback() {
        
        // const shadowRoot = this.attachShadow({ mode: 'open' });
        // shadowRoot.appendChild(cardTemplate.content.cloneNode(true));
        // let question = shadowRoot.querySelector("#question");
        // question.innerHTML = this.getAttribute("question");

        this.innerHTML = cardTemplate.innerHTML;
        updateComponent(this);

        let questionNumber = this.getAttribute("question_number");

        this.querySelector("#btn_edit_question").addEventListener("click", function(event){

            event.preventDefault();
            window.history.back();
            NewCardActivity.ShowEditQuestionActivity(Number(questionNumber));
        });

        this.querySelector("#btn_delete_question").addEventListener("click", function(event){

                    event.preventDefault();
                    window.history.back();
                    NewCardActivity.DeleteQuestion(Number(questionNumber));
        });
        
    }

    attributeChangedCallback(name, oldValue, newValue) {

    updateComponent(this);
    }
}

function updateComponent(element){

    let questionNumber = element.querySelector("#question_number");
    if (questionNumber != null){
        questionNumber.innerHTML = element.getAttribute("question_number");
    }

    let question = element.querySelector("#question");
    if (question != null){
    question.innerHTML = element.getAttribute("question");
    }

}

customElements.define('question-summary', QuestionSummaryCard);

    

