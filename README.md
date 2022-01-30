# GMap2ICS
Small utility in Kotlin to convert Google Maps timeline data to ICS files for calendar import

## Background
I travelled quite a lot every year before the pandemic. Very often my daily itinerary changes at the last minute. This made me a headache recalling where I have been on a particular day.

I use Google Maps for navigation heavily, and I have it tracked my movement for quite many years.

I had an idea to extract my activity timeline from the Google Maps, but unfortunately there is no public API available. It might be possible to achieve that using some web scrapping techniques, but this approach is not likely to be an elegant one.

That is why I am trying another way round: By setting Google Maps to regularly export my activity data as JSON files, I can then process them using this Kotlin utility, and generate iCal (.ICS) files which I can then import to my calendars. 

By doing so, I can keep a full record of the places I have actually been, and also the rough mileages I have spent on the road.
