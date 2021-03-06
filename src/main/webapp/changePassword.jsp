<!DOCTYPE html>
<html>
<head lang="en">

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>

    <link rel="stylesheet" type="text/css" href="formDesign.css">

    <meta charset="UTF-8">
    <title></title>

    <style>
        .initialPass{
            background: #3399cc;
            margin:0 auto;
            margin-top:1%;
            padding:10px;
            text-align:center;
            text-decoration:none;
            color:#fff;
        }
    </style>

    <script>
        $(document).ready(function(){
            var isInitial = <%=session.getAttribute("accountStatus")%>
            if (isInitial == 0){
                $('p').show();
            } else {
                $('p').hide();
            }
        });
    </script>

</head>
<body>
<p class="initialPass">
    It is good to change your password because it is initial right now and your account is insecure.
</p>

<div id="changePass">
    <div id="triangle"></div>
    <h1>Change your password</h1>
    <form name="changePass" action="ChangePasswordServlet" method="post">
        <input type="password" name="oldPassword" placeholder="Old password" />
        <input type="password" name="newPassword" placeholder="New password" />
        <input type="password" name="confirmNewPassword" placeholder="Confirm new password" />
        <input type="submit" value="Update password" />
    </form>
</div>
</body>
</html>