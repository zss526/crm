layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //用户登录表单提交
    form.on("submit(saveBtn)", function(data){

        //发送Ajax请求
        $.ajax({
            type:"post",
            url: ctx+"/user/updateInfo",
            data: {
                userName:data.field.username,
                phone:data.field.phone,
                email:data.field.email,
                trueName:data.field.trueName,
                id:data.field.id
            },
            datatype:"json",
            success:function (msg){
                if(msg.code==200){
                    layer.msg("修改成功",function(){

                        //清除Cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});

                        //跳转
                        window.location.href=ctx+"/index";
                    })
                }else{
                    layer.msg(msg.msg);
                }
            }
        })

        //阻止表单提交
        return false;
    });
});

