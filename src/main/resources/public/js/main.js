layui.use(['element', 'layer', 'layuimini','jquery','jquery_cookie'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        $ = layui.jquery_cookie($);

    // 菜单初始化
    $('#layuiminiHomeTabIframe').html('<iframe width="100%" height="100%" frameborder="0"  src="welcome"></iframe>')
    layuimini.initTab();


    //退出操作
    $(".login-out").click(function(){
       //清空Cookie
       $.removeCookie("userIdStr",{Domain:"localhost",Path:"/crm"});
       $.removeCookie("userName",{Domain:"localhost",Path:"/crm"});
       $.removeCookie("trueName",{Domain:"localhost",Path:"/crm"});

       //跳转
        window.location.href=ctx+"/index";

    });

});