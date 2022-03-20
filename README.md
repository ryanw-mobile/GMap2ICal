# GMap2ICS
A small utility written in Kotlin to convert Google Maps activity timeline to iCal (.ics) files for calendar import. 

This is not an Android App, you may just open the project using IntelliJ IDEA and run it on your desktop environment.

As this is my first non-Android Kotlin project, I would allow myself to utilize it as a playground, and to make all kind of mistakes in order to learn and improve.

## Background
I travelled quite a lot every year before the pandemic. Very often my daily itinerary changes at the last minute. This made me a headache recalling where I have been on a particular day.

I use Google Maps for navigation heavily, and I have it tracked my movement for quite many years.

I had an idea to extract my activity timeline from the Google Maps, but unfortunately there is no public API available. It might be possible to achieve that using some web scrapping techniques, but this approach is not likely to be an elegant one.

That is why I am trying another way round: By setting Google Maps to regularly export my activity uk.ryanwong.gmap2ics.data as JSON files, I can then process them using this Kotlin utility, and generate iCal (.ICS) files which I can then import to my calendars. 

By doing so, I can keep a full record of the places I have actually been, and also the rough mileages I have spent on the road.

## Status
This project is still making progress and is not ready for production use. I have been pretty occupied at work recently, therefore the primary goal is to have something that is good enough to solve my problem.

## Todo list

- Including more details for ActivitySegment
- Exception handling 
- More test cases
- Compose Desktop UI