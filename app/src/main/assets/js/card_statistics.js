window.addEventListener("load", function(){

    let studyingCardJson = CardStatisticsActivity.GetStudyingCardJson();
    let studyingCard = JSON.parse(studyingCardJson);

    updateCardStatics(studyingCard)


});

function updateCardStatics(StudyingCard){

    document.querySelector("#txt_card_name").innerHTML = StudyingCard.Name;
    document.querySelector("#txt_add_when").innerHTML = convertTimestampToShortDate(StudyingCard.AddedWhen);
    document.querySelector("#txt_studies_quantity").innerHTML = StudyingCard.StudyingCardPlaysList.length;





}

function convertTimestampToShortDate(timeStamp){

     let date = new Date(timeStamp.seconds * 1000);
     let shortDate = date.toLocaleDateString();

     return shortDate;
}

function getStudyingCardRightQuestions(StudyingCard){


}