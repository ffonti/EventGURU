<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Newsletter EventGURU</title>
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

            /* Per desktop */
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
            <h1>Ciao, ${nomeDestinatario}!</h1>

            <p>
                L'organizzatore ${nomeOrganizzatore} ${cognomeOrganizzatore} ha creato un nuovo evento,
                di seguito tutte le informazioni necessarie:
            </p>

            <ul>
                <li><strong>Titolo:</strong> ${titolo}</li>
                <li><strong>Descrizione:</strong> ${descrizione}</li>
                <li><strong>Data e ora d'inizio:</strong> ${dataInizio}</li>
                <li><strong>Data e ora di fine:</strong> ${dataFine}</li>
            </ul>

            <p>
                Se non vuoi più ricevere email della newsletter, puoi tranquillamente disattivare l'opzione
                nella sezione "Account" sulla piattaforma.
            </p>

            <p>Corri a iscriverti all'evento tramite la nostra piattaforma!</p>

            <p>
                <strong>Buon divertimento,</strong>
                <br>Il team EventGURU
            </p>
        </div>
    </body>
</html>
