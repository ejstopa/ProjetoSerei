window.addEventListener("load", function(){

    let studyingCardsMapJson = MainActivity.GetStudyingCardsJson();
    updateStudyingCards(studyingCardsMapJson);

    });

function updateStudyingCards(studyingCardsMapJson){

        let studyingCardsMap = JSON.parse(studyingCardsMapJson);
        let studyingCardsContainer = document.querySelector("#studying_cards_container");
        studyingCardsContainer.innerHTML = "";

        let studyingCardsSortedKeys = Object.keys(studyingCardsMap).sort(function(a,b){

            if (studyingCardsMap[a].OwnCard < studyingCardsMap[b].OwnCard){
                return 1;
            }
            else if (studyingCardsMap[a].OwnCard > studyingCardsMap[b].OwnCard){
                return -1;
            }
            else{
                  if (studyingCardsMap[a].Name > studyingCardsMap[b].Name){
                    return 1;
                  }
                  else if (studyingCardsMap[a].Name < studyingCardsMap[b].Name){
                      return -1;
                  }
                  else{
                      return 0;
                  }
            }

        });

        let userId = MainActivity.GetCurrentUserId();
        let ownCard;

        for (key of studyingCardsSortedKeys){

            let element;

            if (studyingCardsMap[key].OwnCard){
                element = document.createElement("studying-card-summary");
            }
            else{
                 element = document.createElement("studying-public-card-summary");
            }

            element.setAttribute("card_id", key);
            element.setAttribute("card_name", studyingCardsMap[key].Name);
            element.setAttribute("question_quantity", studyingCardsMap[key].QuestionQuantity);
            element.setAttribute("target_questions", studyingCardsMap[key].TargetQuestions);
            studyingCardsContainer.appendChild(element);

        }
    }