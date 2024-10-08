package com.example.weatherapp.data.model.city2

data class Country(
    val name: Name,
    val tld: List<String>,
    val cca2: String,
    val ccn3: String,
    val cca3: String,
    val independent: Boolean,
    val status: String,
    val unMember: Boolean,
    val currencies: Map<String, Currency>,
    val idd: IDD,
    val capital: List<String>,
    val altSpellings: List<String>,
    val region: String,
    val languages: Map<String, String>,
    val translations: Map<String, Translation>,
    val latlng: List<Double>,
    val landlocked: Boolean,
    val area: Double,
    val demonyms: Demonyms,
    val flag: String,
    val maps: Maps,
    val population: Int,
    val car: Car,
    val timezones: List<String>,
    val continents: List<String>,
    val flags: Flags,
    val startOfWeek: String,
    val capitalInfo: CapitalInfo
)