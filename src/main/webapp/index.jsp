<html>
<head>

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>

    <link rel="stylesheet" type="text/css" href="formDesign.css">

    <script>
        $(document).ready(function(){
            $('#toggle-login').click(function(){
                $('#login').toggle();
            });
        });
    </script>

</head>

<body>

<span href="#" class="button" id="toggle-login">Log in</span>

<div id="login">
    <div id="triangle"></div>
    <h1>Log in</h1>
    <form action="LoginServlet" method="post">
        <input class="email" type="text" name="email" placeholder="Email" />
        <span>@novarto.com</span>
        <input type="password" name="password" placeholder="Password" />
        <input type="submit" value="Log in" />
    </form>
    <form id="signUpForm" action="signUp.html" method="post">
        <input type="submit" value="Sign up">
    </form>
</div>
</body>
</html>
