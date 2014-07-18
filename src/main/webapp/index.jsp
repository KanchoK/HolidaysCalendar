<html>
<head>

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>

    <link rel="stylesheet" type="text/css" href="LoginDesign.css">

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
        <input type="text" name="username" placeholder="Username" />
        <input type="password" name="password" placeholder="Password" />
        <input type="submit" value="Log in" />
    </form>
</div>
</body>
</html>
