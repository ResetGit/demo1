var token = sessionStorage.token;
// var session = sessionStorage.setItem("session","1111111111111111");
$.ajax({
    url: 'user/GetSession',
    type: "post",
    data: {
        tokens:token
    },
    success: function (res) {
        // $("#name").html(res[0].sh_name);
    }
});