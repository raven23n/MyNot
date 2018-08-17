package org.frocu.news.mynot.POJO

val newspaperEmpty = Newspaper(
        nameNewspaper = "",
        stateNewspaper = 0,
        imageNewspaper = "",
        nameClaseParserNewspaper = "",
        urlNewspaper= "")

class Newspaper (var nameNewspaper : String = "",
                 var stateNewspaper : Long = 0,
                 var imageNewspaper: String = "",
                 var nameClaseParserNewspaper: String = "",
                 var urlNewspaper: String = "")