<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuovo Evento</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            text-align: center;
            color: #005CB9;
        }
        h1 {
            color: #005CB9;
        }
        ul {
            list-style: none;
            padding: 0;
        }

        /* Stili per schermi larghi (desktop) */
        @media screen and (min-width: 768px) {
            body {
                font-size: 18px;
            }
            h1 {
                font-size: 36px;
            }
            ul {
                font-size: 18px;
            }
        }
    </style>
</head>
<body>
<div>
    <h1>Ciao, ${nomeTurista}!</h1>
    <p>È stata appena richiesta una nuova password per il tuo account.</p>
    <p>Adesso puoi accedere con la seguente password: ${nuovaPassword}</p>
    <p>Ci vediamo sulla nostra piattaforma!</p>
    <p><strong>Buon divertimento,</strong><br>Il team EventGURU</p>
</div>
</body>
</html>
