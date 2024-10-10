<html lang="EN">
    <head>
        <link rel="stylesheet" href="/assets/style.css">
        <title>Kotlin webapp - Home</title>
    </head>
    <body>
        <h1>Home - User Input</h1>
        <h4>Made by Hyunbin</h4>
        <p>This is a very simple Kotlin webapp.</p>
        <style>
            label {
                display: inline-block;
                min-width: 80px;
            }
        </style>

        <form action="/submit" method="post">
            <label for="input1">1st input :</label>
            <input type="text" id="input1" name="input1">
            <br>
            <br>
            <label for="input2">2nd input :</label>
            <input type="text" id="input2" name="input2">
            <br>
            <br>
            <label for="input3">3rd input :</label>
            <input type="text" id="input3" name="input3">
            <br>
            <br>
            <label for="input4">4th input :</label>
            <input type="text" id="input4" name="input4">
            <br>
            <br>
            <label for="input5">5th input :</label>
            <input type="text" id="input5" name="input5">
            <br>
            <br>
            <input type="submit" value="Submit" style="background-color: #c660f1;
                       color: white;
                       padding: 4px 10px;
                       text-align: center;
                       font-size: 14px;
                       cursor: grab;">
        </form>

        <a href="/post"><button style="background-color: #c660f1;
                       color: white;
                       padding: 4px 10px;
                       text-align: center;
                       font-size: 14px;
                       cursor: grab;">Posts</button></a>
    </body>
</html>