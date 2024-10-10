<html lang="EN">
<head>
    <link rel="stylesheet" href="/assets/style.css">
    <title>Kotlin webapp - Posts</title>
</head>
<body>
    <h1>Posts</h1>
    <h4>Made by Hyunbin</h4>
    <p>This is a very simple Kotlin webapp.</p>

    <style>
        label {
            display: inline-block;
            min-width: 70px;
        }
    </style>

    <form action="/post" method="post">
        <label for="title">Title :</label>
        <input type="text" id="title" name="title" style="width: 180px;" required>
        <br>
        <br>
        <label for="content">Content :</label>
        <input type="text" id="content" name="content" style="width: 380px;" required>
        <br>
        <br>
        <input type="submit" value="Submit" style="background-color: #c660f1;
               color: white;
               padding: 4px 10px;
               text-align: center;
               font-size: 14px;
               cursor: grab;">
        <br>
    </form>

    <ul>
        <#list posts as x>
            <li>${x.title} : ${x.body}</li>
        </#list>
    </ul>

    <a href="/index"><button style="background-color: #c660f1;
                       color: white;
                       padding: 4px 10px;
                       text-align: center;
                       font-size: 14px;
                       cursor: grab;">Back to Home</button></a>

</body>
</html>