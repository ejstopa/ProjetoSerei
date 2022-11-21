let formLogin = document.querySelector("#form_login");
formLogin.addEventListener("submit", function(event){

    let txt_email = (document.querySelector("#txt_email").value).trim();
    let txt_password = document.querySelector("#txt_password").value;

    event.preventDefault();
    window.history.back();

    if (AccountModel.Login(txt_email, txt_password)){
        LoginActivity.MessageLoginOk();
        LoginActivity.ShowMainActivity();
    }
    else{
        document.querySelector("#txt_email").value = "";
        document.querySelector("#txt_password").value = "";

        LoginActivity.MessageLoginFailed();
    }
}, true);


let label_cadastrar = document.querySelector("#label_cadastrar");
label_cadastrar.addEventListener("click", function(){

    LoginActivity.ShowNewUserActivity();
});




