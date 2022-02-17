package uk.ryanwong.gmap2ics.domain

enum class PlaceType {
    ACCOUNTING,
    AIRPORT,
    AMUSEMENT_PARK,
    AQUARIUM,
    ART_GALLERY,
    ATM,
    BAKERY,
    BANK,
    BAR,
    BEAUTY_SALON,
    BICYCLE_STORE,
    BOOK_STORE,
    BOWLING_ALLEY,
    BUS_STATION,
    CAFE,
    CAMPGROUND,
    CAR_DEALER,
    CAR_RENTAL,
    CAR_REPAIR,
    CAR_WASH,
    CASINO,
    CEMETERY,
    CHURCH,
    CITY_HALL,
    CLOTHING_STORE,
    CONVENIENCE_STORE,
    COURTHOUSE,
    DENTIST,
    DEPARTMENT_STORE,
    DOCTOR,
    DRUGSTORE,
    ELECTRICIAN,
    ELECTRONICS_STORE,
    EMBASSY,
    FIRE_STATION,
    FLORIST,
    FUNERAL_HOME,
    FURNITURE_STORE,
    GAS_STATION,
    GYM,
    HAIR_CARE,
    HARDWARE_STORE,
    HINDU_TEMPLE,
    HOME_GOODS_STORE,
    HOSPITAL,
    INSURANCE_AGENCY,
    JEWELRY_STORE,
    LAUNDRY,
    LAWYER,
    LIBRARY,
    LIGHT_RAIL_STATION,
    LIQUOR_STORE,
    LOCAL_GOVERNMENT_OFFICE,
    LOCKSMITH,
    LODGING,
    MEAL_DELIVERY,
    MEAL_TAKEAWAY,
    MOSQUE,
    MOVIE_RENTAL,
    MOVIE_THEATER,
    MOVING_COMPANY,
    MUSEUM,
    NIGHT_CLUB,
    PAINTER,
    PARK,
    PARKING,
    PET_STORE,
    PHARMACY,
    PHYSIOTHERAPIST,
    PLUMBER,
    POLICE,
    POST_OFFICE,
    PRIMARY_SCHOOL,
    REAL_ESTATE_AGENCY,
    RESTAURANT,
    ROOFING_CONTRACTOR,
    RV_PARK,
    SCHOOL,
    SECONDARY_SCHOOL,
    SHOE_STORE,
    SHOPPING_MALL,
    SPA,
    STADIUM,
    STORAGE,
    STORE,
    SUBWAY_STATION,
    SUPERMARKET,
    SYNAGOGUE,
    TAXI_STAND,
    TOURIST_ATTRACTION,
    TRAIN_STATION,
    TRANSIT_STATION,
    TRAVEL_AGENCY,
    UNIVERSITY,
    VETERINARY_CARE,
    ZOO
}

fun PlaceType.getLabel(): String {
    return when (this) {
        PlaceType.ACCOUNTING -> "🧾"
        PlaceType.AIRPORT -> "🛫"
        PlaceType.AMUSEMENT_PARK -> "🎡"
        PlaceType.AQUARIUM -> "🐠"
        PlaceType.ART_GALLERY -> "🖼"
        PlaceType.ATM -> "🏧"
        PlaceType.BAKERY -> "🍞"
        PlaceType.BANK -> "💰"
        PlaceType.BAR -> "🍷"
        PlaceType.BEAUTY_SALON -> "💆"
        PlaceType.BICYCLE_STORE -> "🚲"
        PlaceType.BOOK_STORE -> "📚"
        PlaceType.BOWLING_ALLEY -> "🎳"
        PlaceType.BUS_STATION -> "🚏"
        PlaceType.CAFE -> "☕️"
        PlaceType.CAMPGROUND -> "🏕"
        PlaceType.CAR_DEALER -> "🚗"
        PlaceType.CAR_RENTAL -> "🚗"
        PlaceType.CAR_REPAIR -> "👨‍🔧"
        PlaceType.CAR_WASH -> "🚗"
        PlaceType.CASINO -> "🎰"
        PlaceType.CEMETERY -> "🪦"
        PlaceType.CHURCH -> "⛪️"
        PlaceType.CITY_HALL -> "🏛"
        PlaceType.CLOTHING_STORE -> "👔"
        PlaceType.CONVENIENCE_STORE -> "🏪"
        PlaceType.COURTHOUSE -> "⚖️"
        PlaceType.DENTIST -> "🦷"
        PlaceType.DEPARTMENT_STORE -> "🛍"
        PlaceType.DOCTOR -> "🩺"
        PlaceType.DRUGSTORE -> "💊"
        PlaceType.ELECTRICIAN -> "👨‍🔧"
        PlaceType.ELECTRONICS_STORE -> "🔌"
        PlaceType.EMBASSY -> "🧳"
        PlaceType.FIRE_STATION -> "👩‍🚒"
        PlaceType.FLORIST -> "💐"
        PlaceType.FUNERAL_HOME -> "⚰️"
        PlaceType.FURNITURE_STORE -> "🪑"
        PlaceType.GAS_STATION -> "⛽️"
        PlaceType.GYM -> "💪"
        PlaceType.HAIR_CARE -> "💇‍♂️"
        PlaceType.HARDWARE_STORE -> "🛠"
        PlaceType.HINDU_TEMPLE -> "🛕"
        PlaceType.HOME_GOODS_STORE -> "🪴"
        PlaceType.HOSPITAL -> "🏥"
        PlaceType.INSURANCE_AGENCY -> "👨‍💼"
        PlaceType.JEWELRY_STORE -> "💎"
        PlaceType.LAUNDRY -> "🧺"
        PlaceType.LAWYER -> "⚖️"
        PlaceType.LIBRARY -> "📖"
        PlaceType.LIGHT_RAIL_STATION -> "🚉"
        PlaceType.LIQUOR_STORE -> "🥃"
        PlaceType.LOCAL_GOVERNMENT_OFFICE -> "🏢"
        PlaceType.LOCKSMITH -> "🔐"
        PlaceType.LODGING -> "🏨"
        PlaceType.MEAL_DELIVERY -> "🍴"
        PlaceType.MEAL_TAKEAWAY -> "🥡"
        PlaceType.MOSQUE -> "🕌"
        PlaceType.MOVIE_RENTAL -> "🎞"
        PlaceType.MOVIE_THEATER -> "🎬"
        PlaceType.MOVING_COMPANY -> "📦"
        PlaceType.MUSEUM -> "🛖"
        PlaceType.NIGHT_CLUB -> "🎤"
        PlaceType.PAINTER -> "🎨"
        PlaceType.PARK -> "🏞"
        PlaceType.PARKING -> "🅿️"
        PlaceType.PET_STORE -> "🦮"
        PlaceType.PHARMACY -> "💊"
        PlaceType.PHYSIOTHERAPIST -> "👨‍⚕️"
        PlaceType.PLUMBER -> "👨‍🔧"
        PlaceType.POLICE -> "👮‍♀️"
        PlaceType.POST_OFFICE -> "📮"
        PlaceType.PRIMARY_SCHOOL -> "🏫"
        PlaceType.REAL_ESTATE_AGENCY -> "🏠"
        PlaceType.RESTAURANT -> "👨‍🍳"
        PlaceType.ROOFING_CONTRACTOR -> "👨‍🔧"
        PlaceType.RV_PARK -> "🛻"
        PlaceType.SCHOOL -> "🏫"
        PlaceType.SECONDARY_SCHOOL -> "🏫"
        PlaceType.SHOE_STORE -> "👞"
        PlaceType.SHOPPING_MALL -> "🏬"
        PlaceType.SPA -> "🧖‍♂️"
        PlaceType.STADIUM -> "🏟"
        PlaceType.STORAGE -> "📦"
        PlaceType.STORE -> "🏬"
        PlaceType.SUBWAY_STATION -> "🚇"
        PlaceType.SUPERMARKET -> "🛒"
        PlaceType.SYNAGOGUE -> "🕍"
        PlaceType.TAXI_STAND -> "🚖"
        PlaceType.TOURIST_ATTRACTION -> "🏞"
        PlaceType.TRAIN_STATION -> "🚆"
        PlaceType.TRANSIT_STATION -> "🚉"
        PlaceType.TRAVEL_AGENCY -> "🏝"
        PlaceType.UNIVERSITY -> "🏫"
        PlaceType.VETERINARY_CARE -> "🐶"
        PlaceType.ZOO -> "🦁"
    }
}