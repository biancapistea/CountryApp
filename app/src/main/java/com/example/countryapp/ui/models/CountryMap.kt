package com.example.countryapp.ui.models

import com.example.domain.model.Country

object CountryMapper {
    fun map(countryDto: Country): com.example.countryapp.ui.models.Country {
        countryDto.apply {
            return Country(
                name = Name(
                    common = countryDto.name.common,
                    official = countryDto.name.official
                ),
                flags = ImageData(
                    png = countryDto.flags?.png,
                    svg = countryDto.flags?.svg
                ),
                coatOfArms = ImageData(
                    png = countryDto.coatOfArms?.png,
                    svg = countryDto.coatOfArms?.svg
                ),
                capital = countryDto.capital,
                region = countryDto.region,
                population = countryDto.population,
                subregion = countryDto.subregion,
                independent = countryDto.independent ?: false
            )
        }
    }
}