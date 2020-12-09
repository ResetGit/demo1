var token = sessionStorage.token;
$.ajax({
    url: 'user/loginGetSession',
    type: "post",
    data: {
        tokens:token
    },
    success: function (res) {
        $("#name").html(res[0].name);
    }
});