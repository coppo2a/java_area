function api_call(api_name) {
    var string = document.URL;
    string = string.split("?");
    var stringArray = new Array();
    for (var i = 0; i < string.length; i++) {
        stringArray.push(string[i]);
    }
    OAuth.initialize('QvbOZF0OBufRvhZR--09UG81NtA');

    OAuth.popup(api_name).done(function (api) {
        console.log(api);
        if (api_name === "twitter") {
            console.log(api.oauth_token);
            console.log(api.oauth_token_secret);
            window.location.replace("http://localhost:8080/home/connected/" +
                "api_request?token=" + api.oauth_token + "&user=" + string[1] + "&api_name=" + String(api_name)
            + "&token_secret=" + api.oauth_token_secret);
        }
        else if (api_name == "yammer"){
            console.log("token : " + api.access_token.token);
            window.location.replace("http://localhost:8080/home/connected/" +
                "api_request?token=" + api.access_token.token + "&user=" + string[1] + "&api_name=" + String(api_name)
             + "&token_secret=" + "" );
        }
        else {
            console.log(api.access_token);
            console.log("token : " + api.access_token.token);
            window.location.replace("http://localhost:8080/home/connected/" +
                "api_request?token=" + api.access_token + "&user=" + string[1] + "&api_name=" + String(api_name) +
            "&token_secret=" + "");
        }
    }).fail(function (err) {
        console.log("error with api_call")
        //todo when the OAuth flow failed
    });
}

function twitter_fb() {
    var string = document.URL;
    string = string.split("?");
    var stringArray = new Array();
    for (var i = 0; i < string.length; i++) {
        stringArray.push(string[i]);
    }
    window.location.replace("http://localhost:8080/home/connected/twitter_fb?user=" + string[1]);
}

function intra_mail() {
    var string = document.URL;
    string = string.split("?");
    var stringArray = new Array();
    for (var i = 0; i < string.length; i++) {
        stringArray.push(string[i]);
    }
    window.location.replace("http://localhost:8080/home/connected/intra_mail?user=" + string[1]);
}

function insta_fb() {
    var string = document.URL;
    string = string.split("?");
    var stringArray = new Array();
    for (var i = 0; i < string.length; i++) {
        stringArray.push(string[i]);
    }
    window.location.replace("http://localhost:8080/home/connected/insta_fb?user=" + string[1]);
}

function intra_yammer(){
    var string = document.URL;
    string = string.split("?");
    var stringArray = new Array();
    for (var i = 0; i < string.length; i++) {
        stringArray.push(string[i]);
    }
    window.location.replace("http://localhost:8080/home/connected/intra_yammer?user=" + string[1]);
}