// layui.use(['form','jquery','jquery_cookie'], function () {
//     var form = layui.form,
//         layer = layui.layer,
//         $ = layui.jquery,
//         $ = layui.jquery_cookie($);
//
//     //用户登录表单提交
//     form.on("submit(saveBtn)", function(data){
//
//         //获取表单元素的内容
//         var fieldData=data.field;
//
//         //发送Ajax请求
//         $.ajax({
//            type: "post",
//            url: ctx+"/user/updatePassword",
//            data: {
//                "oldPassword":fieldData.old_password,
//                "newPassword":fieldData.new_password,
//                "confirmPassword":fieldData.again_password
//            },
//             dataType:"json",
//             success:function (msg){
//                if(msg.code==200){
//                    //修改成功
//                    layer.msg("修改成功，三秒钟之后进入登录界面",function (){
//                        //清除Cookie
//                        $.removeCookie("userName",{Domain:"localhost",Path:"/crm"});
//                        $.removeCookie("trueName",{Domain:"localhost",Path:"/crm"});
//                        $.removeCookie("userIdStr",{Domain:"localhost",Path:"/crm"});
//
//                        //跳转
//                        window.location.href=ctx+"/index";
//                    })
//                }else{
//                    layer.msg(msg.msg);
//                }
//
//             }
//
//         });
//
//
//         //阻止表单提交
//         return false;
//     });
// });
//
layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    /**
     * 用户密码修改 表单提交
     */
    form.on("submit(saveBtn)", function(data) {
// 获取表单元素的内容
        var fieldData = data.field;
// 发送ajax请求，修改用户密码
        $.ajax({
            type:"post",
            url:ctx + "/user/updatePassword",
            data:{
            oldPassword:fieldData.old_password,
            newPassword:fieldData.new_password,
            confirmPassword:fieldData.again_password
    },
        dataType:"json",
            success:function (data) {
// 判断是否成功
            if (data.code == 200) {
// 修改成功后，用户自动退出系统
                layer.msg("用户密码修改成功，系统将在3秒钟后退出...", function () {
// 退出系统后，删除对应的cookie
                    $.removeCookie("userIdStr",
                        {domain:"localhost",path:"/crm"});
                    $.removeCookie("userName",
                        {domain:"localhost",path:"/crm"});
                    $.removeCookie("trueName",
                        {domain:"localhost",path:"/crm"});
// 跳转到登录页面 (父窗口跳转)
                    window.parent.location.href = ctx + "/index";
                });
            } else {
                layer.msg(data.msg);
            }
        }
    });
    });
});