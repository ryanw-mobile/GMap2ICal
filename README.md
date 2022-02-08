# GMap2ICS
A small utility in Kotlin to convert Google Maps activity timeline to iCal (.ics) files for calendar import

## Background
I travelled quite a lot every year before the pandemic. Very often my daily itinerary changes at the last minute. This made me a headache recalling where I have been on a particular day.

I use Google Maps for navigation heavily, and I have it tracked my movement for quite many years.

I had an idea to extract my activity timeline from the Google Maps, but unfortunately there is no public API available. It might be possible to achieve that using some web scrapping techniques, but this approach is not likely to be an elegant one.

That is why I am trying another way round: By setting Google Maps to regularly export my activity uk.ryanwong.gmap2ics.data as JSON files, I can then process them using this Kotlin utility, and generate iCal (.ICS) files which I can then import to my calendars. 

By doing so, I can keep a full record of the places I have actually been, and also the rough mileages I have spent on the road.

## Status
This project is still making progress and is not ready for production use.

## Todo list

- Fix timezone setting (now applying resolved time zones to UTC timestamps makes everything wrong)
- Improve location (address) formatting
- Getting the notes field to work 
- Connect to Google Maps API to resolve places ID for driving events
- Assign current timestamp to the last modified time field
- Smarter configuration and detection to skip events staying at home
- Exception handling 
- More test cases