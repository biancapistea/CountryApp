package com.example.domain.model

import com.example.data.dto.CountryDto

object CountryNameDtoToModelMapper {
    fun map(countryNameDto: CountryDto): Country {
        countryNameDto.apply {
            return Country(
                name = Name(
                    common = countryNameDto.name.common,
                    official = countryNameDto.name.official
                ),
                flags = ImageData(png = countryNameDto.flags?.png, svg = countryNameDto.flags?.svg),
                coatOfArms = ImageData(png = countryNameDto.flags?.png, svg = countryNameDto.flags?.svg),
                capital = countryNameDto.capital,
                region = countryNameDto.region
            )
        }
    }
}