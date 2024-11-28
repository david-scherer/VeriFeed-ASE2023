package at.ac.tuwien.verifeed.core.email;

public class StaticHTMLProvider {

    public static String confirmationMail(String link) {
        return """
        <!DOCTYPE html>
        <html>
            <head>
                <title>VeriFeed: Account verification</title>
            </head>
            <body>
                <table style="max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif; background-color: #f9f9f9;">
                    <tr>
                        <td>
                            <h1 style="color: #333;">VeriFeed: Welcome to our Platform!</h1>
                            <p style="color: #555;">Stay informed with our journalistic commentaries on important political topics.</p>
                            <p style="color: #555;">Please verify your email by clicking the button below:</p>
                            <p><a href="
                            """
                            +
                            link
                            +
                            """                          
                            " target="_blank" style="display: inline-block; font-size: 16px; padding: 10px 20px; text-decoration: none; color: #fff; background-color: #007bff; border-radius: 5px;">Verify Your Email</a></p>
                        </td>
                    </tr>
                </table>
            </body>
        </html>
        """;
    }

    public static String composeRedirectPage(String redirectURL) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Delayed Redirect</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f9f9f9;
                            text-align: center;
                            margin: 0;
                            padding: 20px;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #fff;
                            padding: 20px;
                            border-radius: 5px;
                            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                        }
                        h1 {
                            color: #333;
                        }
                        p {
                            color: #555;
                        }
                        .redirect-message {
                            font-size: 20px;
                            margin-bottom: 20px;
                        }
                    </style>
                    <meta http-equiv="refresh" content="5;url=
                    """
                    +
                    redirectURL
                    +
                    """
                    ">
                    </head>
                    <body>
                        <div class="container">
                            <h1>VeriFeed: Confirmation successful!</h1>
                            <div class="redirect-message">
                                <p>You're redirected to our landing page in <span id="countdown">5</span> seconds...</p>
                            </div>
                            <p>If the redirect doesn't happen, click <a href="
                            """
                            +
                            redirectURL
                            +
                            """
                            ">here</a>.</p>
                        </div>
                        <script>
                           var countdownElement = document.getElementById("countdown");
                           var countdown = 5; // Initial countdown value
                           var countdownInterval = setInterval(function() {
                           countdown--;
                           countdownElement.textContent = countdown;
                           if (countdown <= 0) {
                              clearInterval(countdownInterval);
                              //window.location.href = "/actualRedirect"; // Redirect after countdown
                            }
                           }, 1000); // Update the countdown every second (1000 milliseconds)
                        </script>
                </body>
        </html>
       """;
    }
}
