var token = sessionStorage.token;
// var session = sessionStorage.setItem("session","1111111111111111");
$.ajax({
    url: 'user/GetSession',
    type: "post",
    // headers: {
    //     Authorization:token
    // },
    data: {
        tokens:token
    },
    success: function (res) {
        console.log(res)
        $("#name").html(res[0].sh_name);
    }
});