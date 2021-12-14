
layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //用户登录表单提交
    form.on("submit(login)", function(data){
        //获取表单元素的值
        var filedData = data.field;
        //判定用户名和密码
        if(filedData.username=="undefined" || filedData.username.trim()==""){
            layer.msg("用户名不能为空");
            return false;
        }
        if(filedData.password=="undefined" ||filedData.password.trim()==""){
            layer.msg("用户密码不能为空");
            return false;
        }

        //发送AJAX请求
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:filedData.username,
                userPwd:filedData.password
            },
            dataType:"json",
            success:function(msg){
                if(msg.code==200){
                    layer.msg("登陆成功",function(){
                        //将登陆对象存储到Cookie中
                        $.cookie("userIdStr",msg.result.userIdStr);
                        $.cookie("userName",msg.result.userName);
                        $.cookie("trueName",msg.result.trueName);

                        //判断是否选中记住我
                        if($("input[type='checkbox']").is(":checked")){
                            $.cookie("userIdStr",msg.result.userIdStr,{expires:7});
                            $.cookie("userName",msg.result.userName,{expires:7});
                            $.cookie("trueName",msg.result.trueName,{expires:7});
                        }

                        //跳转
                        window.location.href=ctx+"/main";
                    })
                }else{
                    layer.msg(msg.msg);
                }
            }

        })
        //阻止表单跳转
        return false;
    });
});

