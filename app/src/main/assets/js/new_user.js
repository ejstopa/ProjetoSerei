let formNewUser = document.querySelector("#form_new_user");
formNewUser.addEventListener("submit", function(event){

     let input_name = document.querySelector("#txt_name");
     let input_email = document.querySelector("#txt_email");
     let input_password1 = document.querySelector("#txt_password_1");
     let input_password2 = document.querySelector("#txt_password_2");

     let txt_name = input_name.value.trim();
     let txt_email = input_email.value.trim();
     let txt_password1 = input_password1.value;
     let txt_password2 = input_password2.value;

     event.preventDefault();
     window.history.back();

     if (!validatePassword(input_password1, input_password2)){
        return;
     }

     resultAccount = createAccount(txt_email, txt_password1, txt_name);

     if (resultAccount == null){
        return;
     }

     resultUser = createUser(resultAccount.user_id, txt_name, txt_email);

     if (resultUser == null){
        return;
     }

     NewUserActivity.MessageAccountCreated();
     NewUserActivity.ShowMainActivity();

 }, true);

function validatePassword(input_password1, input_password2){

    if (input_password1.value == input_password2.value){
        return true;
    }
    else{
        input_password1.value = "";
        input_password2.value = "";
        NewUserActivity.ShowPasswordValidationError();
        return false;
    }
}

function createAccount(txt_email, txt_password1, txt_name){

  let resultAccount = JSON.parse(AccountModel.CreateAccount(txt_email, txt_password1, txt_name));

  if (resultAccount.success == null){
     NewUserActivity.MessageAccountCreationError();
     return null;
  }

  if (!resultAccount.success == true){
     NewUserActivity.ShowMessage(resultAccount.error_message);
     return null;
  }

  return resultAccount;

}

function createUser(user_id, txt_name, txt_email){

    let resultUser = JSON.parse(UserModel.CreateUser(user_id, txt_name, txt_email));

     if (resultUser.success == null){
        NewUserActivity.MessageAccountCreationError();
        return null;
     }

     if (!resultUser.success == true){
        NewUserActivity.ShowMessage(resultUser.error_message);
        return null;
     }

return resultUser

}









