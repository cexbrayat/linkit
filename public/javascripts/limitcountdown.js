/*
 * Gère l'affichage d'un compteur indiquant le nombre de caractères restants dans un input de formulaire.
 * Usage : registerLimited(fieldSelector, countdownSelector, limitValue), avec
 *         - fieldSelector (string) : selector jQuery unique du champs à taille limitée
 *         - countdownSelector (string) : selector jQuery unique de l'élément HTML contenant le compteur
 *         - limit (int) : taille autorisée pour le champs
 *         - label (string) : libellé "caractères restants" devant lequel sera indiqué le nombre de caractères restants
 */
function updateCountdown(event) {
    var remaining = event.data.limit - $(event.data.fieldSelector).val().length;
    $(event.data.countdownSelector).text(remaining + ' ' + event.data.label);
}
function registerLimited(fieldSelector, countdownSelector, limitValue, label) {
    var eventData = {fieldSelector: fieldSelector, countdownSelector: countdownSelector, limit: limitValue, label: label};
    updateCountdown({data: eventData});
    $(fieldSelector).change(eventData, updateCountdown);
    $(fieldSelector).keyup(eventData, updateCountdown);
}


