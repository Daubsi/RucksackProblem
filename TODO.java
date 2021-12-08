// TODO Code flexibler für tests gestalten
// bsp booelan test = true; oder so
// beispielsweise die Ausgabe von Handlungsschritten unterbinden
// Oder auch die überschprungenen Pakete nicht im Detail anzeigen
// Generelle Informationen über die Platzierten und Übersprungenen Pakete machen

// TODO Lösungsansatz für Platzierung (Brute Force)
// Einfach mit den beiden Drehungen alle punkte testen und den mit den meisten anliegenden Seiten wird genommen 
// Dh. es wird die Position gespeichert, wenn nAnliegenderFlächen < vorheriger ist
// Optimierung: Falls alle Seiten anliegen nicht weitersuchen sondern break
// Return ist die Position als int Liste also int[] pos = {y, x}

// TODO Textausgabe für die Handlungsschritte des Algorithmus
// Die sortierte Liste {P4, P2, P7, ...} ausgeben
// Dann die einzelnen Schritte zb Normaldrehung: 5 freie Flächen / Gedreht: 2 freie Flächen
// Text anzeige zb "P5 muss an (3|4) in Normaldrehung"

// TODO Funktion um die Lösung zu bewerten
// also (Wert aller Pakete) / (Fläche aller Pakete) Verglichen mit (Lösungswert) / (Fläche)