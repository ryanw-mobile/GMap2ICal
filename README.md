# GMap2ICS
Small tool in Kotlin to convert Google Maps timeline to ICS for Calendar import

## Background
I travel quite a lot every year. Very often my daily iternary changes at the last minute, making it difficult to track my activities.

I had an idea to extract my activity timeline from the Google Map, but unfortunately there is no public API available. It might be possible to achieve that using web scrapping techniques, but this approach is not likely to be an elegant one.

That is why I am trying another way round: By setting Google Maps to regularly check out my activity data into JSON files, then feed them into this Kotlin program, and generate iCal (.ICS) files which I can then import to my calendars. By doing so, I can keep a full record of the places I have been, and the mileage I have drived.

