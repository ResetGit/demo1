var token = sessionStorage.token;
$.ajax({
    url: 'loginGetSession',
    type: "post",
    data: {
        tokens:token
    },
    success: function (res) {
        // $("#name").html(res[0].sh_name);
    }
});