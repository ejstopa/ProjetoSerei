window.addEventListener("load", function(){

    let studyingCardsMapJson = MainActivity.GetStudyingCardsJson();
    updateStudyingCards(studyingCardsMapJson);

    });

function updateStudyingCards(studyingCardsMapJson){

        let studyingCardsMap = JSON.parse(studyingCardsMapJson);
        let studyingCardsContainer = document.querySelector("#studying_cards_container");
        studyingCardsContainer.innerHTML = "";

        let studyingCardsSortedKeys = Object.keys(studyingCardsMap).sort(function(a,b){
            return (studyingCardsMap[a].Name > studyingCardsMap[b].Name) ? 1 : ((studyingCardsMap[b].Name > studyingCardsMap[a].Name) ? -1 : 0)
            });

        let userId = MainActivity.GetCurrentUserId();
        let ownCard;

        for (key of studyingCardsSortedKeys){

            let element = document.createElement("studying-card-summary");
            element.setAttribute("own_card", studyingCardsMap[key].OwnCard);
            element.setAttribute("card_id", key);
            element.setAttribute("card_name", studyingCardsMap[key].Name);
            element.setAttribute("question_quantity", studyingCardsMap[key].QuestionQuantity);
            element.setAttribute("target_questions", studyingCardsMap[key].TargetQuestions);

            studyingCardsContainer.appendChild(element);
        }
    }